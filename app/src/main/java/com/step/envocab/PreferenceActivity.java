package com.step.envocab;

import static android.app.AlarmManager.INTERVAL_DAY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class PreferenceActivity extends BaseActivity {
    SwitchCompat switchPref;
    TimePicker simpleTimePicker;
    TextView textTime;
    Context context;
    MaterialButton btnSetTimer;
    private PendingIntent alarmIntent;

    private String TAG="Preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        Toolbar toolbar = (Toolbar) findViewById(R.id.action_toolbar);
        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        textTime=findViewById(R.id.text_time);
        switchPref=findViewById(R.id.switch_preference);
        btnSetTimer=findViewById(R.id.btn_set_timer);

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

        AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        btnSetTimer.setOnClickListener(v->{
            MaterialTimePicker materialTimePicker=new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("Set Time")
                    .build();

            materialTimePicker.addOnPositiveButtonClickListener(view->{
                Calendar calendar=Calendar.getInstance();
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);
                calendar.set(Calendar.MINUTE,materialTimePicker.getMinute());
                calendar.set(Calendar.HOUR,materialTimePicker.getHour());

                textTime.setText(materialTimePicker.getHour()+" : "+materialTimePicker.getMinute());
                //Context context;
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
//                        calendar.getTimeInMillis(),
//                        alarmIntent);

//                alarmManager.set(AlarmManager.RTC_WAKEUP,
//                        calendar.getTimeInMillis(),
//
               System.out.println("      time="+calendar.getTimeInMillis());
               System.out.println("timeSystem="+System.currentTimeMillis());

                //AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

// Создать Intent, который будет запущен
               // Intent intent = new Intent(this, MyBroadcastReceiver.class);

// Создать PendingIntent, который будет запущен Alarm Manager
             //   PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

// Настроить Alarm Manager для запуска PendingIntent через 10 секунд
                //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (2 * 1000), alarmIntent);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , alarmIntent);

//                alarmManager.setInexactRepeating(
//                        AlarmManager.RTC_WAKEUP,
//                        calendar.getTimeInMillis(),
//                        AlarmManager.INTERVAL_DAY,
//                         alarmIntent
//                );
                System.out.println("Next="+alarmManager.getNextAlarmClock());
                String nextAlarm = Settings.System.getString(getContentResolver(),
                        Settings.System.NEXT_ALARM_FORMATTED);

                System.out.println("Next2="+nextAlarm);
            });
            materialTimePicker.show(getSupportFragmentManager(),"tag_picker");
        });

//    simpleTimePicker= (TimePicker)findViewById(R.id.time_picker); // initiate a time picker
//    simpleTimePicker.setIs24HourView(true);
//    textTime.setText(simpleTimePicker.getHour()+" : "+simpleTimePicker.getMinute());
//
//    simpleTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//            @Override
//            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//               Log.d(TAG,"Time");
//            }
//        });

    }
}