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
        String agentID = (String) getIntent().getSerializableExtra("agentID");

        btn_cashDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDepositWindow(accountModel, instance_type, agentID);
            }
        });

        btn_cashWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWithdrawalWindow(accountModel, instance_type, agentID);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openOpeningWindow();}
        });

    }

    public void openDepositWindow(AccountModel accountModel, String instance_type, String agentID){
        Intent depositIntent = new Intent(this, DepositFragment.class);
        depositIntent.putExtra("Account", accountModel);
        depositIntent.putExtra("i_type", instance_type);
        depositIntent.putExtra("agentID", agentID );
        startActivity(depositIntent);
    }

    public void openWithdrawalWindow(AccountModel accountModel, String instance_type, String agentID){
        Intent withdrawIntent = new Intent(this, WithdrawFragment.class);
        withdrawIntent.putExtra("Account", accountModel);
        withdrawIntent.putExtra("i_type", instance_type);
        withdrawIntent.putExtra("agentID", agentID);
        startActivity(withdrawIntent);
    }

    public void openOpeningWindow(){
        Intent openingIntent = new Intent(this, OpeningWindow.class);
        startActivity(openingIntent);
    }

}