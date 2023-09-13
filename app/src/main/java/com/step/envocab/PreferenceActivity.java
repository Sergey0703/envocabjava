package com.step.envocab;

import static android.app.AlarmManager.INTERVAL_DAY;
import static android.app.AlarmManager.INTERVAL_FIFTEEN_MINUTES;
import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class PreferenceActivity extends BaseActivity {
    AlarmManager alarmManager;
    SwitchCompat switchPref;
    TimePicker simpleTimePicker;
    TextView textTime;
    Context context;
    MaterialButton btnSetTimer;
    MaterialButton btnNotify;
    private PendingIntent pendingIntent;

    private String TAG = "Preferences";
    private static final int NOTIFY_ID = 100;
    private static final int NOTIFICATION_PERMISSION = 100;

    // Идентификатор канала
    private static String CHANNEL_ID = "timerChannel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        Toolbar toolbar = (Toolbar) findViewById(R.id.action_toolbar);
        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        createNotificationChannel();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        textTime = findViewById(R.id.text_time);
        switchPref = findViewById(R.id.switch_preference);
        btnSetTimer = findViewById(R.id.btn_set_timer);
        btnNotify = findViewById(R.id.btn_set_notify);

        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(PreferenceActivity.this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.title_pink7)
                                .setColor(Color.rgb(217,126,121))
                                .setContentTitle("Reminder from EnVocab")
                                .setContentText("Study English every day!")
                                .setAutoCancel(true)
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setPriority(NotificationCompat.PRIORITY_HIGH);

                NotificationManagerCompat notificationManager =
                        NotificationManagerCompat.from(PreferenceActivity.this);
                if (ActivityCompat.checkSelfPermission(PreferenceActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //  public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                         int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(PreferenceActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION);
                    Log.d("Alarm","Return Notify!");
                   // return;
                }
                Log.d("Alarm","Send Notify!");
                notificationManager.notify(NOTIFY_ID, builder.build());
            }
        });

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        Boolean s1 = sh.getBoolean("themeApp", true);
        if(s1) {
            switchPref.setChecked(true);
            switchPref.setText("Light theme");
        }else{
            switchPref.setChecked(false);
            switchPref.setText("Dark theme");
        }

        switchPref.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);

                //SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                //editor.putString(themeApp, valueString);

                if (switchPref.isChecked()) {
                    switchPref.setText("Light theme");
                    Log.d("TAG", "Light theme");
                    editor.putBoolean("themeApp",true);
                } else {
                    switchPref.setText("Dark theme");
                    Log.d("TAG", "Dark theme");
                    editor.putBoolean("themeApp",false);
                }
                editor.commit();

            }
        });

         alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        //PendingIntent alarmIntent2 = PendingIntent.getBroadcast(this, 2, intent, PendingIntent.FLAG_MUTABLE);

        //Intent i=new Intent(this, AlarmReceiver.class);
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
       // PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_IMMUTABLE);

        btnSetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(btnSetTimer.getText().equals("CLEAR TIME")){

                    if(alarmManager==null){
                        Log.d(TAG,"alarmManager==null");
                        alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        Log.d(TAG,"alarmManager==null2222222");
                    }
                    alarmManager.cancel(pendingIntent);
                Log.d(TAG,"alarmManager==Cancel");
                    textTime.setText(" - : - ");
                btnSetTimer.setText("SET TIME");
                Toast.makeText(PreferenceActivity.this, "Reminder cancelled!", Toast.LENGTH_SHORT).show();

            }else {
                MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText("Set Time")
                        .build();

                materialTimePicker.addOnPositiveButtonClickListener(viewT -> {
                    GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    calendar.set(Calendar.MINUTE, materialTimePicker.getMinute());
                    calendar.set(Calendar.HOUR_OF_DAY, materialTimePicker.getHour());

                    textTime.setText(materialTimePicker.getHour() + " : " + materialTimePicker.getMinute());

                    System.out.println("timeSystem=" + System.currentTimeMillis());
                    System.out.println("      time=" + calendar.getTimeInMillis());


                    // Настроить Alarm Manager для запуска PendingIntent через 10 секунд
                    //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (2 * 1000), alarmIntent);
                    //!!!Work alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (2 * 1000) , alarmIntent);

                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(), //System.currentTimeMillis() + (2 * 1000),
                            INTERVAL_FIFTEEN_MINUTES,
                            pendingIntent
                    );

                    btnSetTimer.setText("CLEAR TIME");

                });
                materialTimePicker.show(getSupportFragmentManager(), "tag_picker");
            }
        }
        });



    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name="mainChannel";
            String description="Channel for alarm";
            int importance= NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel=new NotificationChannel("timerChannel",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }
}