package com.example.microbankingsystem.ui;

import android.os.AsyncTask;

import com.example.microbankingsystem.TransactionModel;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class UpdateCritical extends AsyncTask {

    private TransactionModel transactionModel;

    public UpdateCritical(TransactionModel transactionModel) {
        this.transactionModel = transactionModel;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        RequestBody formBody = new FormBody.Builder()
                .add("acc_no", transactionModel.getAccNo())
                .add("amount", String.valueOf(transactionModel.getAmount()))
                .add("type",transactionModel.getType())
                .add("date", transactionModel.getDate())
                .build();

        System.out.println(formBody);

        String url = "http://10.0.2.2:8083/criticalTransaction";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).post(formBody).build();

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
