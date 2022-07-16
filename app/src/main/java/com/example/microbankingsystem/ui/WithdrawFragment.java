package com.example.microbankingsystem.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;


public class WithdrawFragment extends AppCompatActivity {

    Button make_withdrawal;

    String accNo,type;
    String date;
    EditText amount;
    TextView viewAccNo;

    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_fragment);

        make_withdrawal = findViewById(R.id.btn_makeTransWithdraw);
        amount = findViewById(R.id.et_amount);

        AccountModel accountModel = (AccountModel) getIntent().getSerializableExtra("Account");
        String instance_type = (String) getIntent().getSerializableExtra("i_type");
        String agentID = (String) getIntent().getSerializableExtra("agentID");

        accNo = accountModel.getAccountNo();
        type = "Withdraw";

        viewAccNo = findViewById(R.id.textView4);
        viewAccNo.setText(accNo);

        make_withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double requested_amount = Double.parseDouble(amount.getText().toString());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = sdf.format(new Date());
                TransactionModel transactionModel = new TransactionModel(1, accNo, requested_amount, type, date);

                boolean success = false;

                if(instance_type.equals("n")) {

                    DatabaseHelper withdrawDBHelper = new DatabaseHelper(WithdrawFragment.this);

                    if ( requested_amount < withdrawDBHelper.getAccountBalance(accNo)){

                        success = withdrawDBHelper.record_transaction(transactionModel);

                        if(withdrawDBHelper.getLastID() >= 2){

                            UpdateCloud updateCloud =new UpdateCloud(withdrawDBHelper.getAllTransactions(), agentID);
                            updateCloud.execute();

                            withdrawDBHelper.clearTransactions();

                        }

                        if (success) {
                            Toast.makeText(WithdrawFragment.this, "Success", Toast.LENGTH_SHORT).show();
                            openOptionsFragment(accountModel, instance_type, agentID);
                        }
                    }
                    else{
                        Toast.makeText(WithdrawFragment.this, "Not sufficient balance", Toast.LENGTH_SHORT).show();
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

                    if (success) {
                        Toast.makeText(WithdrawFragment.this, "Success", Toast.LENGTH_SHORT).show();
                        openOptionsFragment(accountModel, instance_type, agentID);
                    }
                    else{
                        Toast.makeText(WithdrawFragment.this, "Error", Toast.LENGTH_SHORT).show();
                    }
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