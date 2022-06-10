package com.example.microbankingsystem.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.microbankingsystem.R;

public class VerificationPage extends AppCompatActivity {

    Button btn_verification_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_page);

        btn_verification_check = findViewById(R.id.btn_verification_check);

        btn_verification_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOptionsFragment();
            }
        });
    }

    private void openOptionsFragment(){
        Intent intent = new Intent(this, OptionsFragment.class);
        startActivity(intent);
    }
    // to make a change to commit
}