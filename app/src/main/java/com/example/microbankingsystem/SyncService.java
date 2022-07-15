package com.example.microbankingsystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.microbankingsystem.ui.UpdateCloud;
import com.example.microbankingsystem.ui.VerificationPage;

public class SyncService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "working", Toast.LENGTH_SHORT).show();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        String agentID = (String) intent.getSerializableExtra("agentID");

        UpdateCloud updateCloud = new UpdateCloud(dbHelper.getAllTransactions(),agentID);
        updateCloud.execute();

        dbHelper.clearTransactions();
    }
}
