package com.step.envocab;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class TrainActivity extends BaseActivity {
    TextToSpeech textToSpeech;
    private int color;
    private String theme = "";
    private String TAG = "Train";
    private String id_word = "", id_word1, id_word2, id_word3, id_word4;
    private Button btnWord1, btnWord2, btnWord3, btnWord4, btnOk;
    private String passedDestination = "", passedTechName = "", passedName = "";
    private LocalDate today, dateList;
    private LocalDateTime startOfDate;
    private LocalDateTime endOfDate;
    private Long startOfDay;
    private Long endOfDay;
    private ImageButton btnSoundTr;
    private List<Dbwords> listWords, listCheckWords;
    private TextView textNameTrain, wordTrain, wordTranscript;
    private ImageView countIm1, countIm2, countIm3, countIm4, countIm5, countIm6, countIm7, countIm8, countIm9, countIm10;
    private boolean checkOk;
    Animation animAlpha;
    private int offset=0;

    private int checkCounter, limit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        Boolean s1 = sh.getBoolean("themeApp", true);

        if (s1) {
            setContentView(R.layout.activity_train);
            //layoutIdForListItem=R.layout.exercises_roster_item2;
            theme = "light";
        } else {
            setContentView(R.layout.activity_train);
            //layoutIdForListItem=R.layout.exercises_roster_item2;
            theme = "dark";
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

        Log.d(TAG, "start!!");
        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            passedName = extras.getString("passedName");
            passedTechName = extras.getString("passedTechName");
            passedDestination = extras.getString("passedDestination");
//            passedId=intent.getStringExtra("data");
//            passedName=intent.getStringExtra("passedName");
            Log.d(TAG, "pass=" + passedTechName + " " + passedDestination);
        }
        today = LocalDate.now();
        dateList = today;
        textNameTrain = findViewById(R.id.text_name_train);
        passedName = '"'+passedName.substring(0, 1).toUpperCase() + passedName.substring(1)+'"';
        textNameTrain.setText(passedName);

        wordTrain = findViewById(R.id.word_train);
        wordTranscript = findViewById(R.id.word_transcript);
        btnWord1 = findViewById(R.id.btn_word1);
        btnWord2 = findViewById(R.id.btn_word2);
        btnWord3 = findViewById(R.id.btn_word3);
        btnWord4 = findViewById(R.id.btn_word4);

        btnSoundTr=findViewById(R.id.btn_sound_tr);

        countIm1 = (ImageView) findViewById(R.id.count1);
        countIm2 = (ImageView) findViewById(R.id.count2);
        countIm3 = (ImageView) findViewById(R.id.count3);
        countIm4 = (ImageView) findViewById(R.id.count4);
        countIm5 = (ImageView) findViewById(R.id.count5);
        countIm6 = (ImageView) findViewById(R.id.count6);
        countIm7 = (ImageView) findViewById(R.id.count7);
        countIm8 = (ImageView) findViewById(R.id.count8);
        countIm9 = (ImageView) findViewById(R.id.count9);
        countIm10 = (ImageView) findViewById(R.id.count10);

        btnSoundTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.startAnimation(animAlpha);
                playSpeech(wordTrain.getText().toString());
            }
        });

        checkCounter = 0;
        btnWord1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // view.startAnimation(animAlpha);
                Log.d(TAG, "btn1");
                checkTrain(id_word1, btnWord1);
            }
        });
        btnWord2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // view.startAnimation(animAlpha);
                Log.d(TAG, "btn2");
                checkTrain(id_word2, btnWord2);
            }
        });
        btnWord3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // view.startAnimation(animAlpha);
                Log.d(TAG, "btn3");
                checkTrain(id_word3, btnWord3);
            }
        });
        btnWord4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // view.startAnimation(animAlpha);
                Log.d(TAG, "btn4");
                checkTrain(id_word4, btnWord4);
            }
        });
        animAlpha= AnimationUtils.loadAnimation(this, R.anim.alpha);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if (i != TextToSpeech.ERROR) {
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });
        startTrain();
    }

    public void playSpeech(String txtSpeech) {
        textToSpeech.speak((String) txtSpeech, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void setColorCounter(int checkCounter, int color){
        Log.d(TAG,"color="+String.valueOf(color));
    switch(checkCounter)

    {
        case 0:
            countIm1.setColorFilter(getResources().getColor(color));
            break;
        case 1:
            countIm2.setColorFilter(getResources().getColor(color));
            break;
        case 2:
            countIm3.setColorFilter(getResources().getColor(color));
            break;
        case 3:
            countIm4.setColorFilter(getResources().getColor(color));
            break;
        case 4:
            countIm5.setColorFilter(getResources().getColor(color));
            break;
        case 5:
            countIm6.setColorFilter(getResources().getColor(color));
            break;
        case 6:
            countIm7.setColorFilter(getResources().getColor(color));
            break;
        case 7:
            countIm8.setColorFilter(getResources().getColor(color));
            break;
        case 8:
            countIm9.setColorFilter(getResources().getColor(color));
            break;
        case 9:
            countIm10.setColorFilter(getResources().getColor(color));
            break;
    }

}
    public void checkTrain(String id,Button btn){
        btnWord1.setEnabled(false);
        btnWord2.setEnabled(false);
        btnWord3.setEnabled(false);
        btnWord4.setEnabled(false);

        if(passedTechName.equals("heartheword")){
            wordTrain.setVisibility(View.VISIBLE);
            wordTranscript.setVisibility(View.VISIBLE);
        }

        int delay=0;
        if(id_word.equals(id)){
            Log.d(TAG,"Win!!");
            btn.setBackgroundColor(Color.GREEN);
            color=R.color.green;
            checkOk=true;
            delay=1000;
        }else{
            Log.d(TAG,"Lost!!!");
            btn.setBackgroundColor(Color.RED);
            btnOk.setBackgroundColor(Color.GREEN);
            color=R.color.red;
            checkOk=false;
            delay=2000;
        }

        setColorCounter(checkCounter,color);

        checkCounter++;
        if(checkCounter<limit){
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    makeScreen();

                }
            }, delay);



        }else{
            //checkCounter=0;
            Log.d(TAG,"Game over!!!");
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                showSimpleDialog(limit);
                btn.setBackgroundColor(Color.WHITE);
                checkCounter=0;

                }
            }, 2000);
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
                    //Log.d(TAG, "Only BAD!!!!");
                    listWords = AppDatabase.getInstance(getApplicationContext())
                            .wordDao()
                            .wordsForListLimit(limit, offset);
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
            if(passedTechName.equals("heartheword")){
                wordTrain.setVisibility(View.INVISIBLE);
                wordTranscript.setVisibility(View.INVISIBLE);
            }
            wordTrain.setText(listWords.get(checkCounter).getWord());
            Log.d(TAG,"W="+listWords.get(checkCounter).getWord());
            wordTranscript.setText("["+listWords.get(checkCounter).getTranscript()+"]");
            id_word=String.valueOf(listWords.get(checkCounter).getId());
            listCheckWords.remove(checkCounter);

            Collections.shuffle(listCheckWords);
            //new Random().nextInt((max - min) + 1) + min
            int id_random= getRandom(0,3);
            if(id_random==0){
                btnOk=btnWord1;
            }else if(id_random==1){
                btnOk=btnWord2;
            } else if(id_random==2){
            btnOk=btnWord3;
            }else if(id_random==3){
                btnOk=btnWord4;
            }



            Log.d(TAG, "id_random="+String.valueOf(id_random)+" checkCounter="+checkCounter+" word="+listWords.get(checkCounter).getWord());
            listCheckWords.set(id_random,listWords.get(checkCounter));

            btnWord1.setText(listCheckWords.get(0).getTranslate());
            btnWord1.setBackgroundColor(Color.WHITE);
            id_word1=String.valueOf(listCheckWords.get(0).getId());

            btnWord2.setText(listCheckWords.get(1).getTranslate());
            btnWord2.setBackgroundColor(Color.WHITE);
            id_word2=String.valueOf(listCheckWords.get(1).getId());

            btnWord3.setText(listCheckWords.get(2).getTranslate());
            btnWord3.setBackgroundColor(Color.WHITE);
            id_word3=String.valueOf(listCheckWords.get(2).getId());

            btnWord4.setText(listCheckWords.get(3).getTranslate());
            btnWord4.setBackgroundColor(Color.WHITE);
            id_word4=String.valueOf(listCheckWords.get(3).getId());

            btnSoundTr.performClick();
            btnSoundTr.setPressed(true);
            btnSoundTr.invalidate();
            // delay completion till animation completes
            btnSoundTr.postDelayed(new Runnable() {  //delay button
                public void run() {
                    btnSoundTr.setPressed(false);
                    btnSoundTr.invalidate();
                    //any other associated action
                }
            }, 100);

            listCheckWords.clear();
            btnWord1.setEnabled(true);
            btnWord2.setEnabled(true);
            btnWord3.setEnabled(true);
            btnWord4.setEnabled(true);

        }
    }

    private void showSimpleDialog(int position){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        //builder.setMessage("Do you want to delete this word from group?");
        //Setting message manually and performing action on button click
        builder.setMessage("Do you want to continue this exercise?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'Yes' Button
                        for(int i=0; i<limit; i++) {
                            setColorCounter(i, R.color.yellow);
                        }
                        offset=offset+limit;
                       // makeScreen();
                        startTrain();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'No' Button
                        //Toast.makeText(getApplicationContext(),"Cancel",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Intent intent = new Intent(TrainActivity.this, ExercisesActivity.class);
//                        Bundle extras = new Bundle();
//                        extras.putString("passedName",name);
//                        extras.putString("passedTechName",techName);
//                        extras.putString("passedDestination",destination);
//
//
//                        intent.putExtras(extras);
                        startActivity(intent);
                      //  return;
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("");
        alert.show();
    }
    private int getRandom(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}