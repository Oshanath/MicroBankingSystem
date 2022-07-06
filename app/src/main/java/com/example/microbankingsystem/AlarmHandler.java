package com.example.microbankingsystem;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmHandler {

    private Context context;

    public AlarmHandler(Context context){
        this.context = context;
    }

    public void setAlarm(Class _class){
        Intent intent = new Intent(context, _class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 2, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        if (alarmManager != null){
            long firstMillis = System.currentTimeMillis();
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pIntent);
            Toast.makeText(context, "Alarm is set. will be sync every 15 min", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelAlarm(Class _class){
        Intent intent = new Intent(context, _class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 2, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        if (alarmManager != null){
            alarmManager.cancel(pIntent);
        }
    }

}
