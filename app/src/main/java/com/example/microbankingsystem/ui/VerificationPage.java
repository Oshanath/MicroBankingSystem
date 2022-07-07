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
import android.widget.Toast;

import com.example.microbankingsystem.AccountModel;
import com.example.microbankingsystem.DatabaseHelper;
import com.example.microbankingsystem.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.*;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class VerificationPage extends AppCompatActivity {

    Button btn_verification_check, btn_sync;
    EditText tv_nic, tv_acc, tv_pin;
    String nic, acc_no, pin;
    OkHttpClient client;
    String agentID, url;
    DatabaseHelper verify_databaseHelper;
    String instance_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_page);

        findByViews();

        client = new OkHttpClient();
        agentID = "190488J";

        instance_type = getIntent().getExtras().getString("i_type");

        verify_databaseHelper = new DatabaseHelper(VerificationPage.this);

        verify_databaseHelper.addAccount(new AccountModel("102", 32000.99, "adult", 999));

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

                if( instance_type.equals("c")) {
                    Verify verify = new Verify();
                    verify.execute();

                }
                else if (instance_type.equals("n")){
                    boolean exist = checkLocalDB();
                    if(exist){
                        makeToast("Verified");
                        openOptionsFragment(verify_databaseHelper.getAccount(acc_no), instance_type);
                    }
                    else{
                        makeToast("Unverified");
                    }
                }
            }
        });
    }


    public class Sync extends AsyncTask {

        @Override
        protected String doInBackground(Object[] objects) {

            url = "http://10.0.2.2:8083/syncAgent/" + agentID;
            Request request = new Request.Builder().url(url).build();

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
                        String acc_type = jsonArray.getJSONObject(i).getString("type");
                        int pin = jsonArray.getJSONObject(i).getInt("pin");

                        verify_databaseHelper.addAccount(new AccountModel(account_number, balance, acc_type, pin));
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

            getEditTextValues();

            url = "http://10.0.2.2:8083/criticalVerify";

            RequestBody formBody = new FormBody.Builder()
                    .add("nic", nic)
                    .add("acc_no", acc_no)
                    .add("pin", pin)
                    .add("agentID", agentID)
                    .build();

            Request request = new Request.Builder().url(url).post(formBody).build();

            Response response = null;

            try {
                response = client.newCall(request).execute();
                System.out.println(response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //openOptionsFragment();

            return null;
        }
    }

    private void openOptionsFragment(AccountModel accountModel, String instance_type){
        Intent intent = new Intent(this, OptionsFragment.class);
        intent.putExtra("Account", accountModel);
        intent.putExtra("i_type", instance_type);
        startActivity(intent);
    }

    public static String hash(String s){

        StringBuilder stringBuilder = new StringBuilder();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(s.getBytes(StandardCharsets.UTF_8));

            for(int i = 0; i < hash.length; i++){
                stringBuilder.append((char)(hash[i] > 0 ? hash[i] : hash[i] + 256));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
    
        private void makeToast(String message){
        Toast.makeText(VerificationPage.this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean checkLocalDB() {

        getEditTextValues();

        List<String> existing_accounts = verify_databaseHelper.getAllAccounts();

        if( existing_accounts.contains(acc_no)){
            return true;
        }
        else{
            return false;
        }
    }

    private void getEditTextValues() {
        nic = String.valueOf(tv_nic.getText());
        acc_no = String.valueOf(tv_acc.getText());
        pin = String.valueOf(tv_pin.getText());
    }

    private void findByViews() {
        btn_verification_check = findViewById(R.id.btn_verification_check);
        btn_sync = findViewById(R.id.btn_sync);
        tv_acc = findViewById(R.id.txt_acc_no);
        tv_nic = findViewById(R.id.txt_nic_num);
        tv_pin = findViewById(R.id.txt_pin);
    }
}
