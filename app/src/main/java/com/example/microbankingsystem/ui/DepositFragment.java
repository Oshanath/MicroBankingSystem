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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
        String agentID = (String) getIntent().getSerializableExtra("agentID");

        accNo = accountModel.getAccountNo();
        type = "Deposit";

        viewAccNo = findViewById(R.id.textView4);
        viewAccNo.setText(accNo);

        make_deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = sdf.format(new Date());
                TransactionModel transactionModel = new TransactionModel(1, accNo, Double.parseDouble(amount.getText().toString()), type, date);

                boolean success = false;

                if(instance_type.equals("n")) {

                    deposit_DBHelper = new DatabaseHelper(DepositFragment.this);

                    success = deposit_DBHelper.record_transaction(transactionModel);
                    Toast.makeText(DepositFragment.this, ""+success, Toast.LENGTH_SHORT).show();

                    if(deposit_DBHelper.getLastID() >= 2){

                        UpdateCloud updateCloud =new UpdateCloud(deposit_DBHelper.getAllTransactions(), agentID);
                        updateCloud.execute();

                        deposit_DBHelper.clearTransactions();

                    }
                }
                else{

                    UpdateCritical updateCritical = new UpdateCritical(transactionModel, agentID);
                    try {
                        success = (boolean) updateCritical.execute().get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (success) {
                    Toast.makeText(DepositFragment.this, "Success", Toast.LENGTH_SHORT).show();
                    openOptionsFragment(accountModel, instance_type, agentID);
                }
                else{
                    Toast.makeText(DepositFragment.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void openOptionsFragment(AccountModel accountModel, String instance_type, String agentID){
        Intent intent = new Intent(this, OptionsFragment.class);
        intent.putExtra("Account", accountModel);
        intent.putExtra("i_type", instance_type);
        intent.putExtra("agentID",agentID);
        startActivity(intent);
    }

}