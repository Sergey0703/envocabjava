package com.step.envocab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TimePicker;

public class PreferenceActivity extends BaseActivity {
    SwitchCompat switchPref;
    TimePicker simpleTimePicker;

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

        switchPref=findViewById(R.id.switch_preference);

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

    simpleTimePicker= (TimePicker)findViewById(R.id.time_picker); // initiate a time picker
    simpleTimePicker.setIs24HourView(true);

    simpleTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
               Log.d(TAG,"Time");
            }
        });

    }
}