package com.example.microbankingsystem.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.microbankingsystem.AccountModel;
import com.example.microbankingsystem.DatabaseHelper;
import com.example.microbankingsystem.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class VerificationPage extends AppCompatActivity {

    Button btn_verification_check, btn_sync;
    EditText tv_nic, tv_acc, tv_pin;
    String nic, acc_no, pin;
    OkHttpClient client;
    String agentID, url;
    Request request;
    DatabaseHelper verify_databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_page);

        btn_verification_check = findViewById(R.id.btn_verification_check);
        btn_sync = findViewById(R.id.btn_sync);
        tv_acc = findViewById(R.id.txt_acc_no);
        tv_nic = findViewById(R.id.txt_nic_num);
        tv_pin = findViewById(R.id.txt_pin);

        client = new OkHttpClient();
        agentID = "190488J";

        verify_databaseHelper = new DatabaseHelper(VerificationPage.this);

        //verify_databaseHelper.addAccount(new AccountModel("101", 32.99, 1));

        btn_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Sync sync = new Sync();
                sync.execute();

            }
        });

        btn_verification_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            Verify verify = new Verify();
            verify.execute();
            }
        });
    }

    public class Sync extends AsyncTask {

        @Override
        protected String doInBackground(Object[] objects) {

            url = "http://10.0.2.2:8083/syncAgent/" + agentID;
            request = new Request.Builder().url(url).build();

            List<String> existing_accounts = verify_databaseHelper.getAllAccounts();

            okhttp3.Response response = null;

            try {
                response = client.newCall(request).execute();
                String response_json = response.body().string();

                JSONArray jsonArray = new JSONArray(response_json);

                for(int i=0; i<jsonArray.length(); i++){
                    String account_number = jsonArray.getJSONObject(i).getString("number");

                    if ( !existing_accounts.contains(account_number)){
                        double balance = jsonArray.getJSONObject(i).getDouble("balance");
                        int joint = jsonArray.getJSONObject(i).getInt("joint");

                        verify_databaseHelper.addAccount(new AccountModel(account_number, balance, joint));
                    }
                }


            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class Verify extends AsyncTask{

        @Override
        protected String doInBackground(Object[] objects){

            nic = String.valueOf(tv_nic.getText());
            acc_no = String.valueOf(tv_acc.getText());
            pin = String.valueOf(tv_pin.getText());

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("nic", nic);
                jsonObject.put("acc_no", acc_no);
                jsonObject.put("pin", pin);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println(jsonObject);

            //URL to verify
            url = "http://10.0.2.2:8083/syncAgent/" + agentID;

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

            request = new Request.Builder().url(url).post(requestBody).build();

            okhttp3.Response response = null;

            try {
                response = client.newCall(request).execute();
                System.out.println(response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }


            openOptionsFragment();

            return null;
        }
    }

    private void openOptionsFragment(){
        Intent intent = new Intent(this, OptionsFragment.class);
        startActivity(intent);
    }

}
