package com.step.envocab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AlarmReceiver extends BroadcastReceiver {
    LocalDateTime localDate;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
        Log.d("Alarm","Alarm!!!"+localDate.now());
        System.out.println("Alarmmmmmmmmmmmm");


    }
}