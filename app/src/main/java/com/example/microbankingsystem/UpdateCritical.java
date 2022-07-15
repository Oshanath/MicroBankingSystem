package com.example.microbankingsystem;

import android.content.Intent;
import android.os.AsyncTask;

import com.example.microbankingsystem.TransactionModel;
import com.example.microbankingsystem.ui.OptionsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class UpdateCritical extends AsyncTask {

    private TransactionModel transactionModel;
    private String agentID;

    public UpdateCritical(TransactionModel transactionModel, String agentID) {
        this.transactionModel = transactionModel;
        this.agentID = agentID;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        RequestBody formBody = new FormBody.Builder()
                .add("agentID", this.agentID)
                .add("acc_no", transactionModel.getAccNo())
                .add("amount", String.valueOf(transactionModel.getAmount()))
                .add("type",transactionModel.getType())
                .add("date", transactionModel.getDate())
                .build();

        String url = "http://10.0.2.2:8083/criticalTransaction";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).post(formBody).build();

        okhttp3.Response response = null;

        try {
            response = client.newCall(request).execute();
            JSONObject jsonObject = new JSONObject(String.valueOf(response.body().string()));
            return jsonObject.getString("message");

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
