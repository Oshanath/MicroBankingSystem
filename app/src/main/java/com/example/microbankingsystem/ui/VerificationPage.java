package com.example.microbankingsystem.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.microbankingsystem.AccountModel;
import com.example.microbankingsystem.DatabaseHelper;
import com.example.microbankingsystem.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.*;


import java.io.IOException;
import java.net.SocketOption;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLOutput;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VerificationPage extends AppCompatActivity {

    Button btn_verification_check, btn_sync;
    EditText tv_nic, tv_acc, tv_pin;
    String nic = "", acc_no = "", pin = "";
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

        verify_databaseHelper.addAccount(new AccountModel("102", 20000.50, "adult", hash("999")));

        Bundle extras;
        extras = getIntent().getExtras();
        String transaction_type = extras.getString("i_type");
        if (transaction_type.equals("c")){
            btn_sync.setVisibility(View.INVISIBLE);
        } else if (transaction_type.equals("n")){
            tv_nic.setVisibility(View.INVISIBLE);
        }

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
                else if (instance_type.equals("n")) {

                    if (getEditTextValues()) {

                        boolean exist = checkLocalDB(acc_no, pin);

                        if (exist) {
                            makeToast("Verified");
                            openOptionsFragment(verify_databaseHelper.getAccount(acc_no), instance_type);
                        } else {
                            makeToast("Unverified");
                        }
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
                        JSONArray hashJson = jsonArray.getJSONObject(i).getJSONArray("pin");

                        byte[] hash = new byte[hashJson.length()];
                        for(int j = 0; j < hashJson.length(); j++){
                            hash[i] = (byte)(hashJson.getInt(j));
                        }

                        verify_databaseHelper.addAccount(new AccountModel(account_number, balance, acc_type, hash));
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

            if(getEditTextValues()){

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
                    JSONObject jsonObject = new JSONObject(String.valueOf(response.body().string()));
                    String success = jsonObject.getString("message");

                    if( success.equals("success")){
                        double balance = jsonObject.getDouble("balance");
                        String type = jsonObject.getString("type");
                        AccountModel critical_acc = new AccountModel(acc_no, balance, type);
                        openOptionsFragment(critical_acc, "c");
                    }
                    else if ( success.equals("wrong pin")) {
                        makeToast("Incorrect Pin");
                    }
                    else if ( success.equals("unregistered")){
                        makeToast("Account not registered in critical service");
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            //openOptionsFragment();

            return null;
        }
    }

    private void openOptionsFragment(AccountModel accountModel, String instance_type){
        Intent intent = new Intent(this, OptionsFragment.class);
        intent.putExtra("Account", accountModel);
        intent.putExtra("i_type", instance_type);
        intent.putExtra("agentID", agentID);
        startActivity(intent);
    }

    public static byte[] hash(String s){

        byte[] hash = {};

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(s.getBytes(StandardCharsets.UTF_8));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hash;
    }

    public static boolean compareHash(byte[] hash1, byte[] hash2){

        boolean equals = true;

        for(int i = 0; i < hash1.length; i++){
            if(hash1[i] != hash2[i]){
                equals = false;
                break;
            }
        }

        return equals;

    }
    
    private void makeToast(String message){
        runOnUiThread(() -> Toast.makeText(VerificationPage.this, message, Toast.LENGTH_SHORT).show());
    }

    private boolean checkLocalDB(String acc_no, String pin) {
//            List<String> existing_accounts = verify_databaseHelper.getAllAccounts();
//
//            if (existing_accounts.contains(acc_no)) {
//                return true;
//            } else {
//                return false;
//            }
        System.out.println("=+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++=");
        System.out.println("given:"+acc_no+"|"+pin);
        System.out.println("========================");

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.readAccNoAndPin(acc_no);

        if (cursor.moveToFirst()){
            do{
                String acc = cursor.getString(0);
                byte byte_pin[] = cursor.getBlob(1);
                System.out.println(acc);
                System.out.println("++++++++++++==================+++++++++++++++");
            }while(cursor.moveToNext());
        }

//        System.out.println(cursor.getString(0));
//        System.out.println(accPin);

        if (cursor == null){
            Toast.makeText(this, "Unverified", Toast.LENGTH_SHORT).show();
            cursor.close();
            databaseHelper.close();
            return false;
        }
//        else{
//            cursor.moveToFirst();
//            byte accPinByteArray[] = cursor.getBlob(1);
//            String tempAccPin = new String(accPinByteArray, StandardCharsets.UTF_8);
//            String accPin = tempAccPin.substring(0,tempAccPin.length()-1);
//
//            if(acc_no.equals(cursor.getString(0))  && pin.equals(accPin)){
//                Toast.makeText(this, "Verified", Toast.LENGTH_SHORT).show();
////                =======
//                System.out.println(cursor.getString(0));
//                System.out.println(accPin);
////                =======
//                cursor.close();
//                databaseHelper.close();
//                return true;
//            }else{
//                Toast.makeText(this, "Unverified", Toast.LENGTH_SHORT).show();
//                cursor.close();
//                databaseHelper.close();
//                return false;
//            }
//        }

        return true;

    }

    private boolean getEditTextValues() {
        nic = String.valueOf(tv_nic.getText());
        acc_no = String.valueOf(tv_acc.getText());
        pin = String.valueOf(tv_pin.getText());

        if ( acc_no.equals("") || pin.equals("")){
            makeToast("Enter all details");
            return false;
        }
        else{
            return true;
        }
    }

    private void findByViews() {
        btn_verification_check = findViewById(R.id.btn_verification_check);
        btn_sync = findViewById(R.id.btn_sync);
        tv_acc = findViewById(R.id.tv_pw);
        tv_nic = findViewById(R.id.tv_usr_name);
        tv_pin = findViewById(R.id.txt_pin);
    }
}
