package com.example.microbankingsystem.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.microbankingsystem.R;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class VerificationPage extends AppCompatActivity {

    Button btn_verification_check, btn_sync;
    TextView tv_nic, tv_acc, tv_pin;
    String nic, acc_no, pin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_page);

        btn_verification_check = findViewById(R.id.btn_verification_check);
        btn_sync = findViewById(R.id.btn_sync);
        tv_acc = findViewById(R.id.txt_acc_no);
        tv_nic = findViewById(R.id.txt_nic_num);
        tv_pin = findViewById(R.id.txt_pin);

        btn_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Sync sync = new Sync();
                sync.execute();

            }
        });

        btn_verification_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nic = (String) tv_nic.getText();
                acc_no = String tv_


                openOptionsFragment();
            }
        });
    }

    public class Sync extends AsyncTask {

        @Override
        protected String doInBackground(Object[] objects) {
            OkHttpClient client = new OkHttpClient();

            String agentID = "190488J";

            String url = "http://10.0.2.2:8083/syncAgent/" + agentID;

            Request request = new Request.Builder().url(url).build();

            okhttp3.Response response = null;
            try {
                response = client.newCall(request).execute();
                System.out.println(response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void openOptionsFragment(){
        Intent intent = new Intent(this, OptionsFragment.class);
        startActivity(intent);
    }

}
