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

        accNo = accountModel.getAccountNo();
        type = "Withdraw";
        date = "2022/07/05";

        viewAccNo = findViewById(R.id.textView4);
        viewAccNo.setText(accNo);

        make_withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double requested_amount = Double.parseDouble(amount.getText().toString());
                System.out.println(requested_amount);

                TransactionModel transactionModel = new TransactionModel(1, accNo, requested_amount, type, date);

                if(instance_type.equals("n")) {

                    DatabaseHelper withdrawDBHelper = new DatabaseHelper(WithdrawFragment.this);

                    if ( requested_amount < withdrawDBHelper.getAccountBalance(accNo)){

                        boolean success = withdrawDBHelper.record_transaction(transactionModel);
                        Toast.makeText(WithdrawFragment.this, ""+success, Toast.LENGTH_SHORT).show();

                        if(withdrawDBHelper.getLastID() >= 5){

                            UpdateCloud updateCloud =new UpdateCloud(withdrawDBHelper.getAllTransactions());
                            updateCloud.execute();

                            withdrawDBHelper.clearTransactions();

                        }

                        if (success) {
                            openOptionsFragment(accountModel, instance_type);
                        }
                    }
                    else{
                        Toast.makeText(WithdrawFragment.this, "Not sufficient balance", Toast.LENGTH_SHORT).show();
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