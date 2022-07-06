package com.example.microbankingsystem.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.microbankingsystem.AccountModel;
import com.example.microbankingsystem.DatabaseHelper;
import com.example.microbankingsystem.R;
import com.example.microbankingsystem.TransactionModel;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class WithdrawFragment extends AppCompatActivity {

    Button make_withdrawal;

    String accNo,type;
    String date;
    EditText amount;

    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_fragment);

        make_withdrawal = findViewById(R.id.btn_makeTransWithdraw);
        amount = findViewById(R.id.et_amount);

        accNo = "0001";
        type = "Withdraw";
        date = "2022/07/05";

        AccountModel accountModel = (AccountModel) getIntent().getSerializableExtra("Account");
        String instance_type = (String) getIntent().getSerializableExtra("i_type");


        make_withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransactionModel transactionModel = new TransactionModel(1, accNo, Double.parseDouble(amount.getText().toString()), type, date);

                if(instance_type.equals("n")) {

                    DatabaseHelper withdrawDBHelper = new DatabaseHelper(WithdrawFragment.this);

                    Double requested_amount = Double.parseDouble(amount.getText().toString());

                    if ( requested_amount < withdrawDBHelper.getAccountBalance(accNo)){

                        boolean success = withdrawDBHelper.record_transaction(transactionModel);
                        Toast.makeText(WithdrawFragment.this, ""+success, Toast.LENGTH_SHORT).show();

                        if (success) {
                            openOptionsFragment(accountModel, instance_type);
                        }
                    }
                    else{
                        Toast.makeText(WithdrawFragment.this, "Not sufficient balance", Toast.LENGTH_SHORT).show();
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

                    //URL to verify
                    String url = "http://10.0.2.2:8083/syncAgent/";

                    client = new OkHttpClient();

                    //RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

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
}