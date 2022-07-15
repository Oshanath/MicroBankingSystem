package com.example.microbankingsystem.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.os.Bundle;
import android.widget.TextView;

import com.example.microbankingsystem.AccountModel;
import com.example.microbankingsystem.R;

public class TransferFragment extends AppCompatActivity {
    TextView viewAccNo;
    String accNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_fragment);

        AccountModel account = (AccountModel) getIntent().getSerializableExtra("Account");
        accNo = account.getAccountNo();
        viewAccNo = findViewById(R.id.textView4);
        viewAccNo.setText(accNo);
    }
}