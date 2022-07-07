package com.example.microbankingsystem.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.microbankingsystem.R;


public class OpeningWindow extends AppCompatActivity {

    Button btn_transaction_normal;
    Button btn_transaction_critical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_window);

        btn_transaction_normal = findViewById(R.id.btn_transaction_normal);
        btn_transaction_critical = findViewById(R.id.btn_transaction_critical);

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

    }

    public void openVerificationWindow(String instance_type){
        Intent intent = new Intent(this, VerificationPage.class);
        intent.putExtra("i_type", instance_type);

        startActivity(intent);
    }

}