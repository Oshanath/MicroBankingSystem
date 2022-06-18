package com.example.microbankingsystem.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.microbankingsystem.R;



public class WithdrawFragment extends AppCompatActivity {

    Button make_withdrawal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_fragment);

        make_withdrawal = findViewById(R.id.btn_makeTransWithdraw);

        make_withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOptionsWindow();
            }
        });
    }

    public void openOptionsWindow(){
        Intent normalWithdraw = new Intent(this, OptionsFragment.class);
        startActivity(normalWithdraw);
    }
}