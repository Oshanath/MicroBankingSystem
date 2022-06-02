package com.example.microbankingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionsFragment extends AppCompatActivity {

    //Reference to Buttons

    Button btn_cashTransfer, btn_cashDeposit, btn_cashWithdrawal, btn_logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_fragment);

        btn_cashTransfer = findViewById(R.id.btn_cashTrans);
        btn_cashDeposit = findViewById(R.id.btn_deposit);
        btn_cashWithdrawal = findViewById(R.id.btn_withdraw);
        btn_logout = findViewById(R.id.btn_logout);

        btn_cashTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTransferWindow();
            }
        });

        btn_cashDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDepositWindow();
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
            public void onClick(View v) {

            }
        });

    }

    public void openTransferWindow(){
        Intent transferIntent =  new Intent(this, TransferFragment.class);
        startActivity(transferIntent);
    }

    public void openDepositWindow(){
        Intent depositIntent = new Intent(this, DepositFragment.class);
        startActivity(depositIntent);
    }

    public void openWithdrawalWindow(){
        Intent withdrawIntent = new Intent(this, WithdrawFragment.class);
        startActivity(withdrawIntent);
    }

}