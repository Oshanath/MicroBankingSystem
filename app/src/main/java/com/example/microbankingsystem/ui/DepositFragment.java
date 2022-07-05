package com.example.microbankingsystem.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class DepositFragment extends AppCompatActivity {

    Button make_deposit;
    String accNo,type;
    String date;
    EditText amount;

    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_fragment);

        make_deposit = findViewById(R.id.btn_makeTransaction2);
        amount = findViewById(R.id.et_amount);

        accNo = "000102034";
        type = "Deposit";
        date = "2022/06/18";

        String instance_type = "Normal";
//        String instance_type = "Critical";


        make_deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DepositFragment.this, "Making transaction", Toast.LENGTH_SHORT).show();
                TransactionModel transactionModel = new TransactionModel(1, accNo, Double.parseDouble(amount.getText().toString()), type, date);


                if(instance_type.equals("Normal")) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(DepositFragment.this);

                    boolean success = databaseHelper.record_transaction(transactionModel);
                    //Toast.makeText(DepositFragment.this, ""+success, Toast.LENGTH_SHORT).show();

                    if (success) {
                        openOptionsWindow();
                    } else {
                        Toast.makeText(DepositFragment.this, "" + success, Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("acc_no", transactionModel.getAccNo());
                        jsonObject.put("amount", transactionModel.getAmount());
                        jsonObject.put("type", transactionModel.getType());
                        jsonObject.put("date", transactionModel.getDate());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //URL to verify
                    String url = "http://10.0.2.2:8083/syncAgent/";

                    client = new OkHttpClient();

                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

                    Request request = new Request.Builder().url(url).post(requestBody).build();

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

    public void openOptionsWindow(){
        Intent normalWithdraw = new Intent(this, OptionsFragment.class);
        startActivity(normalWithdraw);
    }
}