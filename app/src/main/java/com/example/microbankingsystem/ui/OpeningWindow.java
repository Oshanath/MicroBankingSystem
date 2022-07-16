package com.example.microbankingsystem.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.example.microbankingsystem.DatabaseHelper;
import com.example.microbankingsystem.MainActivity;
import com.example.microbankingsystem.R;


public class OpeningWindow extends AppCompatActivity {

    Button btn_transaction_normal;
    Button btn_transaction_critical;
    Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_window);

        btn_transaction_normal = findViewById(R.id.btn_transaction_normal);
        btn_transaction_critical = findViewById(R.id.btn_transaction_critical);
        btn_logout = findViewById(R.id.btn_logout);

        btn_transaction_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openVerificationWindow("n");

            }
        });

        btn_transaction_critical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            
                openVerificationWindow("c");

            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

    }

    public void openVerificationWindow(String instance_type){
        Intent intent = new Intent(this, VerificationPage.class);
        intent.putExtra("i_type", instance_type);

        startActivity(intent);
    }

    private void logOut(){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.clearTransactions();
        databaseHelper.clearAccountTable();
        databaseHelper.deleteAgentID();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(getString(R.string.pref_previously_signed_in), Boolean.FALSE);
        edit.commit();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}