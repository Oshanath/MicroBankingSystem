package com.example.microbankingsystem.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.microbankingsystem.AccountModel;
import com.example.microbankingsystem.R;

import java.io.Serializable;

public class OptionsFragment extends AppCompatActivity {

    Button btn_cashDeposit, btn_cashWithdrawal, btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_fragment);

        btn_cashDeposit = findViewById(R.id.btn_deposit);
        btn_cashWithdrawal = findViewById(R.id.btn_withdraw);
        btn_logout = findViewById(R.id.btn_logout);

        AccountModel accountModel = (AccountModel) getIntent().getSerializableExtra("Account");
        String instance_type = (String) getIntent().getSerializableExtra("i_type");

        btn_cashDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDepositWindow(accountModel, instance_type);
            }
        });

        btn_cashWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWithdrawalWindow();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openOpeningWindow();}
        });

    }

    public void openDepositWindow(AccountModel accountModel, String instance_type){
        Intent depositIntent = new Intent(this, DepositFragment.class);
        depositIntent.putExtra("Account", accountModel);
        depositIntent.putExtra("i_type", instance_type);
        startActivity(depositIntent);
    }

    public void openWithdrawalWindow(){
        Intent withdrawIntent = new Intent(this, WithdrawFragment.class);
        startActivity(withdrawIntent);
    }

    public void openOpeningWindow(){
        Intent openingIntent = new Intent(this, OpeningWindow.class);
        startActivity(openingIntent);
    }

}