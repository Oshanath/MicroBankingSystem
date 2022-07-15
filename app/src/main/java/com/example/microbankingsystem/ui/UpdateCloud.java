package com.example.microbankingsystem.ui;

import android.os.AsyncTask;

import com.example.microbankingsystem.TransactionModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateCloud extends AsyncTask {

    private List<TransactionModel> transactionModels;
    private String agentID;

    public UpdateCloud(List<TransactionModel> transactionModels, String agentID) {
        this.transactionModels = transactionModels;
        this.agentID = agentID;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        OkHttpClient client = new OkHttpClient();

        FormBody.Builder formBody = new FormBody.Builder();
        //formBody.add("agentID", agentID);

        for ( TransactionModel t : transactionModels){

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("accNo", t.getAccNo());
                jsonObject.put("amount", t.getAmount());
                jsonObject.put("type", t.getType());
                jsonObject.put("date", t.getDate());
                jsonObject.put("agentID", agentID);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println(formBody);
            formBody.add(Integer.toString(t.getID()), jsonObject.toString());
        }

        //String url = "http://10.0.2.2:8083/criticalVerify";
        String url = "http://10.0.2.2:8083/normalTransaction";

        RequestBody body = formBody.build();

        Request request = new Request.Builder().url(url).post(body).build();

        Response response = null;

        try {
            response = client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
