package com.example.microbankingsystem.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.microbankingsystem.DatabaseHelper;
import com.example.microbankingsystem.R;
import com.example.microbankingsystem.TransactionModel;

import java.util.Date;

public class DepositFragment extends AppCompatActivity {

    Button make_deposit;
    String accNo,type;
    Date date;
    EditText amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_fragment);

        make_deposit = findViewById(R.id.btn_makeTransaction2);
        amount = findViewById(R.id.editTextNumberDecimal);

        accNo = "Thilina_Pakaya";
        type = "Deposit";
        date = new Date();


        make_deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DepositFragment.this, "Making transaction", Toast.LENGTH_SHORT).show();
                TransactionModel transactionModel = new TransactionModel(1, accNo, Integer.parseInt(amount.getText().toString()), type, date);

                DatabaseHelper databaseHelper = new DatabaseHelper(DepositFragment.this);

                boolean success = databaseHelper.record_transaction(transactionModel);
                //Toast.makeText(DepositFragment.this, ""+success, Toast.LENGTH_SHORT).show();

                if(success){
                    openOptionsWindow();
                }else{
                    Toast.makeText(DepositFragment.this, ""+success, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void openOptionsWindow(){
        Intent normalWithdraw = new Intent(this, OptionsFragment.class);
        startActivity(normalWithdraw);
    }
}