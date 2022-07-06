package com.example.microbankingsystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SyncService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Sync function
        Toast.makeText(context, "it's working", Toast.LENGTH_SHORT).show();
    }
}
