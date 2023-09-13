package com.step.envocab;

//import static androidx.core.graphics.drawable.IconCompat.getResources;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AlarmReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_PERMISSION = 100;
    // Идентификатор уведомления
    private static final int NOTIFY_ID = 100;

    // Идентификатор канала
    private static String CHANNEL_ID = "timerChannel";
    LocalDateTime localDate;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
        Log.d("Alarm", "Alarm!!!" + localDate.now());
        showNotification(context,intent);
        System.out.println("Alarmmmmmmmmmmmm");

    }

    public void showNotification(Context context, Intent intent) {
//        Intent i=new Intent(context, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.title_pink8);
            builder.setColor(Color.rgb(217,126,121));
        } else {
            builder.setSmallIcon(R.drawable.title_pink7);
        }
                         builder
                      //  .setSmallIcon(R.drawable.icon_notify)
                      //  .setColor(Color.rgb(217,126,121))
                        .setContentTitle("Reminder from EnVocab")
                        .setContentText("Study English every day!")
                        .setAutoCancel(true)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
           //    public void onRequestPermissionsResult(int requestCode, String[] permissions,
           //                                           int[] grantResults)
           //  to handle the case where the user grants the permission. See the documentation
           //  for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION);
            Log.d("Alarm","Return Notify!");
             //return;
        }
        Log.d("Alarm","Notify!");
        notificationManager.notify(NOTIFY_ID, builder.build());

//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
//                new Intent(context, MainActivity.class), PendingIntent.FLAG_IMMUTABLE);
//
//        Notification.Builder mBuilder =
//                new Notification.Builder(context)
//                        .setSmallIcon(R.drawable.title_pink)
//                        .setContentTitle("Reminder from EnVocab")
//                        .setContentText("Study English every day!");
//        mBuilder.setContentIntent(contentIntent);
//        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
//        mBuilder.setAutoCancel(true);
//
//        Notification notification= mBuilder.build();
//
//
//        NotificationManager mNotificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        mNotificationManager.notify(1, notification);

    }
}