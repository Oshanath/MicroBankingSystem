package com.example.microbankingsystem;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Sync extends AsyncTask {

    private String agentID;
    private DatabaseHelper databaseHelper;

    public Sync(String agentID, DatabaseHelper databaseHelper) {
        this.agentID = agentID;
        this.databaseHelper = databaseHelper;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        String url = "http://10.0.2.2:8083/syncAgent/" + agentID;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        List<String> existing_accounts = databaseHelper.getAllAccounts();

        okhttp3.Response response = null;

        try {
            response = client.newCall(request).execute();
            String response_json = response.body().string();

            JSONArray jsonArray = new JSONArray(response_json);

            for(int i=0; i<jsonArray.length(); i++){
                String account_number = jsonArray.getJSONObject(i).getString("number");

                if ( existing_accounts==null || !existing_accounts.contains(account_number)){
                    double balance = jsonArray.getJSONObject(i).getDouble("balance");
                    String acc_type = jsonArray.getJSONObject(i).getString("type");
                    JSONArray hashJson = jsonArray.getJSONObject(i).getJSONArray("pin");

                    byte[] hash = new byte[hashJson.length()];
                    for(int j = 0; j < hashJson.length(); j++){
                        hash[j] = (byte)(hashJson.getInt(j));
                    }

                    databaseHelper.addAccount(new AccountModel(account_number, balance, acc_type, hash));
                }
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
