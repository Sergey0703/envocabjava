package com.step.envocab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TrainActivity extends BaseActivity {
    private String theme="";
    private String TAG="Train";
    private String id_word="", id_word1, id_word2, id_word3, id_word4;
    private Button btnWord1, btnWord2, btnWord3, btnWord4;
    private String passedDestination="", passedTechName="", passedName="";
    private LocalDate today, dateList;
    private LocalDateTime startOfDate;
    private LocalDateTime endOfDate;
    private Long startOfDay;
    private Long endOfDay;
    private List<Dbwords> listWords, listCheckWords;
    TextView textNameTrain, wordTrain, wordTranscript;

    private int checkCounter, limit=10;

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
        passedName = passedName.substring(0, 1).toUpperCase() + passedName.substring(1);
        textNameTrain.setText(passedName);

        wordTrain=findViewById(R.id.word_train);
        wordTranscript=findViewById(R.id.word_transcript);
        btnWord1 = findViewById(R.id.btn_word1);
        btnWord2 = findViewById(R.id.btn_word2);
        btnWord3 = findViewById(R.id.btn_word3);
        btnWord4 = findViewById(R.id.btn_word4);
        checkCounter=0;
        btnWord1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // view.startAnimation(animAlpha);
                Log.d(TAG,"btn1");
                checkTrain(id_word1, btnWord1);
            }
        });
        btnWord2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // view.startAnimation(animAlpha);
                Log.d(TAG,"btn2");
                checkTrain(id_word2, btnWord2);
            }
        });
        btnWord3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // view.startAnimation(animAlpha);
                Log.d(TAG,"btn3");
                checkTrain(id_word3, btnWord3);
            }
        });
        btnWord4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // view.startAnimation(animAlpha);
                Log.d(TAG,"btn4");
                checkTrain(id_word4,btnWord4);
            }
        });

        startTrain();
    }
    public void checkTrain(String id,Button btn){
        if(id_word.equals(id)){
            Log.d(TAG,"Win!!");
            btn.setBackgroundColor(Color.GREEN);
        }else{
            Log.d(TAG,"Lost!!!");
            btn.setBackgroundColor(Color.RED);
        }
        checkCounter++;
        if(checkCounter<limit){
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    makeScreen();
                    btn.setBackgroundColor(Color.WHITE);
                }
            }, 1000);



        }else{
            //checkCounter=0;
            Log.d(TAG,"Game over!!!");
        }
    }

    public void startTrain(){
        checkCounter=0;
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
                            .wordsForListLimit(startOfDay, endOfDay, 0,limit);
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
                   makeScreen();

                }
        }, 100);
    }

    public void makeScreen(){
        if (listWords.size() != 0) {
            //listCheckWords=listWords;
            //Collections.copy(listCheckWords, listWords);

            //listCheckWords.addAll(listWords);
            listCheckWords = new ArrayList<>(listWords);
            wordTrain.setText(listWords.get(checkCounter).getWord());
            Log.d(TAG,"W="+listWords.get(checkCounter).getWord());
            wordTranscript.setText("["+listWords.get(checkCounter).getTranscript()+"]");
            id_word=String.valueOf(listWords.get(checkCounter).getId());
            listCheckWords.remove(checkCounter);

            Collections.shuffle(listCheckWords);
            //new Random().nextInt((max - min) + 1) + min
            int id_random= getRandom(0,3);



            Log.d(TAG, "id_random="+String.valueOf(id_random)+" checkCounter="+checkCounter+" word="+listWords.get(checkCounter).getWord());
            listCheckWords.set(id_random,listWords.get(checkCounter));

            btnWord1.setText(listCheckWords.get(0).getTranslate());
            id_word1=String.valueOf(listCheckWords.get(0).getId());

            btnWord2.setText(listCheckWords.get(1).getTranslate());
            id_word2=String.valueOf(listCheckWords.get(1).getId());

            btnWord3.setText(listCheckWords.get(2).getTranslate());
            id_word3=String.valueOf(listCheckWords.get(2).getId());

            btnWord4.setText(listCheckWords.get(3).getTranslate());
            id_word4=String.valueOf(listCheckWords.get(3).getId());

            listCheckWords.clear();

        }
    }
    private int getRandom(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}