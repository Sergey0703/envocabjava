package com.step.envocab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class TrainActivity extends BaseActivity {
    private String theme="";
    private String TAG="Train";
    private String passedDestination="", passedTechName="", passedName="";
    private LocalDate today, dateList;
    private LocalDateTime startOfDate;
    private LocalDateTime endOfDate;
    private Long startOfDay;
    private Long endOfDay;
    private List<Dbwords> listWords;
    TextView textNameTrain,wordTrain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        Boolean s1 = sh.getBoolean("themeApp", true);

        if(s1) {
            setContentView(R.layout.activity_train);
            //layoutIdForListItem=R.layout.exercises_roster_item2;
            theme="light";
        }else{
            setContentView(R.layout.activity_train);
            //layoutIdForListItem=R.layout.exercises_roster_item2;
            theme="dark";
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.action_toolbar);
        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStop();
                finish();
            }
        });

        Log.d(TAG,"start!!");
        Intent intent =getIntent();

        if(intent.getExtras()!=null){
            Bundle extras = intent.getExtras();
            passedName=extras.getString("passedName");
            passedTechName=extras.getString("passedTechName");
            passedDestination=extras.getString("passedDestination");
//            passedId=intent.getStringExtra("data");
//            passedName=intent.getStringExtra("passedName");
            Log.d(TAG,"pass="+passedTechName+" "+passedDestination );
        }
        today = LocalDate.now();
        dateList = today;
        textNameTrain=findViewById(R.id.text_name_train);
        wordTrain=findViewById(R.id.word_train);
        textNameTrain.setText(passedName);
        startTrain();
    }

    public void startTrain(){
        startOfDate = dateList.atStartOfDay();
        endOfDate = LocalTime.MAX.atDate(dateList);

        ZonedDateTime zdtStart = ZonedDateTime.of(startOfDate, ZoneId.systemDefault());
        ZonedDateTime zdtEnd = ZonedDateTime.of(endOfDate, ZoneId.systemDefault());
        startOfDay = zdtStart.toInstant().toEpochMilli();
        endOfDay = zdtEnd.toInstant().toEpochMilli();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
//                if (allStudyWords.isChecked()) {
//                    Log.d(TAG, "All BAD!!!!");
//                    listWords = AppDatabase.getInstance(getApplicationContext())
//                            .wordDao()
//                            .wordsForListAll(0);
//                    //System.out.println("Size=" + listWords.size());
//                } else if (speechCategory.isChecked()) {
//                    Log.d(TAG, "All word!!!!");
//                    listWords = AppDatabase.getInstance(getApplicationContext())
//                            .wordDao()
//                            .wordsForListAll(startOfDay, endOfDay);
//
//                } else {
                    Log.d(TAG, "Only BAD!!!!");
                    listWords = AppDatabase.getInstance(getApplicationContext())
                            .wordDao()
                            .wordsForList(startOfDay, endOfDay, 0);
                    //.wordsForListAllTest();
//
//                }
//                listWordsForAdd = listWords;
//                if (listWords.size() < 4) {
//                    listWords.addAll(listWordsForAdd);
//                }
            }
        });
        thread.start();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                if (listWords.size() != 0) {

                        wordTrain.setText(listWords.get(0).getWord());

                }
                }
        }, 500);
    }

}