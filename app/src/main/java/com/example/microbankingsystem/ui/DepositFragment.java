package com.example.microbankingsystem.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.microbankingsystem.AccountModel;
import com.example.microbankingsystem.DatabaseHelper;
import com.example.microbankingsystem.R;
import com.example.microbankingsystem.TransactionModel;
import com.example.microbankingsystem.UpdateCritical;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DepositFragment extends AppCompatActivity {

    Button make_deposit;
    String accNo,type;
    String date;
    EditText amount;
    TextView viewAccNo;

    OkHttpClient client;

    DatabaseHelper deposit_DBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_fragment);

        make_deposit = findViewById(R.id.btn_makeTransaction2);
        amount = findViewById(R.id.et_amount);

        AccountModel accountModel = (AccountModel) getIntent().getSerializableExtra("Account");
        String instance_type = (String) getIntent().getSerializableExtra("i_type");


        accNo = accountModel.getAccountNo();
        type = "Deposit";
        date = "2022/07/05";

        viewAccNo = findViewById(R.id.textView4);
        viewAccNo.setText(accNo);

        make_deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TransactionModel transactionModel = new TransactionModel(1, accNo, Double.parseDouble(amount.getText().toString()), type, date);

                if(instance_type.equals("n")) {


                    deposit_DBHelper = new DatabaseHelper(DepositFragment.this);

                    boolean success = deposit_DBHelper.record_transaction(transactionModel);
                    Toast.makeText(DepositFragment.this, ""+success, Toast.LENGTH_SHORT).show();

                    if(deposit_DBHelper.getLastID() >= 5){

                        UpdateCloud updateCloud =new UpdateCloud(deposit_DBHelper.getAllTransactions());
                        updateCloud.execute();

                        deposit_DBHelper.clearTransactions();

                    }

                    if (success) {
                        openOptionsFragment(accountModel, instance_type);
                    }
                }
                else{

                        UpdateCritical updateCritical = new UpdateCritical(transactionModel);
                        updateCritical.execute();

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