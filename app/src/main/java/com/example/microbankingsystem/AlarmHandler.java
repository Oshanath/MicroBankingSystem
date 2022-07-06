package com.example.microbankingsystem;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;

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
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 17);
            calendar.set(Calendar.MINUTE, 43);
            calendar.set(Calendar.SECOND, 00);
            long firstMillis = calendar.getTimeInMillis();

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis, 65000, pIntent); //AlarmManager.INTERVAL_DAY
            Toast.makeText(context, "Alarm is set.", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelAlarm(Class _class){
        Intent intent = new Intent(context, _class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 2, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null){
            alarmManager.cancel(pIntent);
        }
    }

}
