package com.example.microbankingsystem.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.microbankingsystem.AccountModel;
import com.example.microbankingsystem.DatabaseHelper;
import com.example.microbankingsystem.R;
import com.example.microbankingsystem.TransactionModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DepositFragment extends AppCompatActivity {

    Button make_deposit;
    String accNo,type;
    String date;
    EditText amount;

    OkHttpClient client;

    DatabaseHelper deposit_DBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_fragment);

        make_deposit = findViewById(R.id.btn_makeTransaction2);
        amount = findViewById(R.id.et_amount);

        accNo = "000102034";
        type = "Deposit";
        date = "2022/06/18";

        AccountModel accountModel = (AccountModel) getIntent().getSerializableExtra("Account");
        String instance_type = (String) getIntent().getSerializableExtra("i_type");

        make_deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TransactionModel transactionModel = new TransactionModel(1, accNo, Double.parseDouble(amount.getText().toString()), type, date);

                if(instance_type.equals("n")) {
                    deposit_DBHelper = new DatabaseHelper(DepositFragment.this);

                    boolean success = deposit_DBHelper.record_transaction(transactionModel);
                    Toast.makeText(DepositFragment.this, ""+success, Toast.LENGTH_SHORT).show();

                    if(deposit_DBHelper.getLastID() >= 5){

                        UpdateCloud updateCloud =new UpdateCloud();
                        updateCloud.execute();

                    }

                    if (success) {
                        openOptionsFragment(accountModel, instance_type);
                    }
                }
                else{

//                    JSONObject jsonObject = new JSONObject();
//
//                    try {
//                        jsonObject.put("acc_no", transactionModel.getAccNo());
//                        jsonObject.put("amount", transactionModel.getAmount());
//                        jsonObject.put("type", transactionModel.getType());
//                        jsonObject.put("date", transactionModel.getDate());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }\\\

                    RequestBody formBody = new FormBody.Builder()
                            .add("acc_no", transactionModel.getAccNo())
                            .add("amount", String.valueOf(transactionModel.getAmount()))
                            .add("type",transactionModel.getType())
                            .add("date", transactionModel.getDate())
                            .build();

                    System.out.println(formBody);

                    String url = "http://10.0.2.2:8083/criticalTransaction";

                    client = new OkHttpClient();

                    Request request = new Request.Builder().url(url).post(formBody).build();

                    okhttp3.Response response = null;

                    try {
                        response = client.newCall(request).execute();
                        System.out.println(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }


    private void openOptionsFragment(AccountModel accountModel, String instance_type){
        Intent intent = new Intent(this, OptionsFragment.class);
        intent.putExtra("Account", accountModel);
        intent.putExtra("i_type", instance_type);
        startActivity(intent);
    }

    public class UpdateCloud extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            client = new OkHttpClient();

            List<TransactionModel> transactions = deposit_DBHelper.getAllTransactions();

            FormBody.Builder formBody = new FormBody.Builder();

            for ( TransactionModel t : transactions){

                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("accNo", t.getAccNo());
                    jsonObject.put("amount", t.getAmount());
                    jsonObject.put("type", t.getType());
                    jsonObject.put("date", t.getDate());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println(formBody);
                formBody.add("transcation" + t.getID(), jsonObject.toString());
            }

            String url = "http://10.0.2.2:8083/criticalVerify";

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
}