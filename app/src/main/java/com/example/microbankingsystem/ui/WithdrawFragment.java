package com.example.microbankingsystem.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.microbankingsystem.DatabaseHelper;
import com.example.microbankingsystem.R;
import com.example.microbankingsystem.TransactionModel;


public class WithdrawFragment extends AppCompatActivity {

    Button make_withdrawal;

    String accNo,type;
    String date;
    EditText amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_fragment);

        make_withdrawal = findViewById(R.id.btn_makeTransWithdraw);
        amount = findViewById(R.id.et_amount);

        accNo = "0001";
        type = "Withdraw";
        date = "2022/07/05";



        make_withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransactionModel transactionModel = new TransactionModel(1, accNo, Double.parseDouble(amount.getText().toString()), type, date);

                DatabaseHelper withdraw_DBHelper = new DatabaseHelper(WithdrawFragment.this);
                boolean success = withdraw_DBHelper.record_transaction(transactionModel);

                if(success){
                    openOptionsWindow();
                }
                else{
                    Toast.makeText(WithdrawFragment.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openOptionsWindow(){
        Intent normalWithdraw = new Intent(this, OptionsFragment.class);
        startActivity(normalWithdraw);
    }
}