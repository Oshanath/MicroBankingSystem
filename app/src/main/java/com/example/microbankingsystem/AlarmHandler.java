package com.example.microbankingsystem;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmHandler {

    private Context context;

    public void setAlarm(Class _class){
        Intent intent = new Intent(context, _class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 2, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        if (alarmManager != null){
            long firstMillis = System.currentTimeMillis();
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pIntent);
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
