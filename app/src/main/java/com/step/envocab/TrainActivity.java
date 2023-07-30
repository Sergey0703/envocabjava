package com.step.envocab;

import static java.security.AccessController.getContext;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class TrainActivity extends BaseActivity {
    private Boolean markWords[];
    private SwitchCompat onlyMarkedWords;
    private ArrayAdapter<String> adapter;
    private TextInputLayout textSpinner2;
    private AutoCompleteTextView spinner2;
    private List<String> listGroups;

    private TextToSpeech textToSpeech;
    private Date currentTime;
    private Spinner spinner;
    private int color;
    private int id_random;
    private String theme = "";
    private String TAG = "Train";
    private String passedDestination, passedIdItem;
    private String id_word = "", id_word1, id_word2, id_word3, id_word4;
    private MaterialButton btnWord1, btnWord2, btnWord3, btnWord4, btnOk, btnSkip;
    private String  passedTechName = "", passedName = "";
    private int id_exercise;
    private LocalDate today, dateList;
    private int id_group=0;
    private LocalDateTime startOfDate;
    private LocalDateTime endOfDate;
    private Long startOfDay;
    private Long endOfDay;
    private Integer filterWord=null;
    private ImageButton btnSoundTr;
    private List<Dbwords> listWords, listCheckWords;
    private TextView textNameTrain, wordTrain, wordTranscript;
    private ImageView countIm1, countIm2, countIm3, countIm4, countIm5, countIm6, countIm7, countIm8, countIm9, countIm10;
    private boolean checkOk;
    private Animation animAlpha;
    private int offset=0;
    private String[] countries;

    private int checkCounter, limit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        Boolean s1 = sh.getBoolean("themeApp", true);

        if (s1) {
            setContentView(R.layout.activity_train2);
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
            passedIdItem = extras.getString("passedIdItem");
            passedName = extras.getString("passedName");
            passedTechName = extras.getString("passedTechName");
            passedDestination=extras.getString("passedDestination");
//            passedId=intent.getStringExtra("data");
//            passedName=intent.getStringExtra("passedName");
            Log.d(TAG, "pass=" + passedTechName + " " + id_exercise+" passedDestination="+passedDestination+" id="+passedIdItem);
        }
        id_exercise = Integer.parseInt(passedIdItem);
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
        btnSkip = findViewById(R.id.btn_word_skip);

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

        onlyMarkedWords = findViewById(R.id.only_marked_words);


       // spinner = findViewById(R.id.spinner_tr);
        spinner2 = findViewById(R.id.spinner_tr2);
        textSpinner2=findViewById(R.id.text_spinner2);

        id_group=checkLastGroup();
        Log.d(TAG, "LastGrT3==" + id_group);

        spinner2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Object item = parent.getItemAtPosition(position);
//                if (item instanceof StudentInfo){
//                    StudentInfo student=(StudentInfo) item;
//                    doSomethingWith(student);
//                }

                String item2 = (String)parent.getItemAtPosition(position);
                Long id_group0 = parent.getItemIdAtPosition(position);
                id_group=id_group0==null?0:id_group0.intValue();

//                try {
//
//                    }
//                }catch(NullPointerException e){
//                id_group=0;
//               }
//                if(id_group==null){
//                    id_group=0L;
//                }
                Log.d(TAG, "item2="+item2+" id_item="+String.valueOf(id_group)+" position="+position);
                startTrain();
            }
        });
        onlyMarkedWords.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //onStop();
                if (onlyMarkedWords.isChecked()) {
                    filterWord=1;
                    Log.d(TAG, "Only words for study");
                } else {
                    filterWord=null;
                    Log.d(TAG, "All words ");
                }
                startTrain();
            }
        });

//        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                // Получаем выбранный объект
//                String item = (String)parent.getItemAtPosition(position);
//                //if(parent.getItemIdAtPosition(position)==null)
//                Long id_group2 = parent.getItemIdAtPosition(position);
//                Log.d(TAG, "item="+item+" id_item="+String.valueOf(id_group));
//                startTrain();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        };
//        spinner.setOnItemSelectedListener(itemSelectedListener);


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
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // view.startAnimation(animAlpha);
                Log.d(TAG, "btnSkip!="+btnSkip.getText());

                //checkTrain(id_word1, btnWord1);
                if(btnSkip.getText().equals("Next")||btnSkip.getText().equals("NEXT")){
                    Log.d(TAG,"NEXT! "+checkCounter+" "+limit);

                        Log.d(TAG,"makeScreen");
                        checkCounter++;
                    if(checkCounter<limit){
                        Log.d(TAG,"ch0="+checkCounter);
                        makeScreen();

                    }else{
                        checkCounter=0;
                        showSimpleDialog(limit);
                        //btn.setBackgroundColor(Color.WHITE);


                    }
                }else{
                    //makeScreen();
                    btnSkip.setText("NEXT");
                    Log.d(TAG,"ch="+checkCounter);
                    setColorCounter(checkCounter, R.color.red);
                    setCount(id_word, false);
                    markWords[checkCounter]=false;
                    wordTrain.setVisibility(View.VISIBLE);
                    wordTranscript.setVisibility(View.VISIBLE);
                    btnWord1.setEnabled(false);
                    btnWord1.setAlpha(0.7f);

                    btnWord2.setEnabled(false);
                    btnWord2.setAlpha(0.7f);

                    btnWord3.setEnabled(false);
                    btnWord3.setAlpha(0.7f);

                    btnWord4.setEnabled(false);
                    btnWord4.setAlpha(0.7f);
                    if(id_random==0){
                        btnWord1.setBackgroundColor(Color.GREEN);
                    }else if(id_random==1){
                        btnWord2.setBackgroundColor(Color.GREEN);
                    } else if(id_random==2){
                        btnWord3.setBackgroundColor(Color.GREEN);
                    }else if(id_random==3){
                        btnWord4.setBackgroundColor(Color.GREEN);
                    }


                }
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
        makeSpin();
        startTrain();
    }

    public int checkLastGroup() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                id_group= AppDatabase.getInstance(getApplicationContext())
                        .countDao()
                        .lastGroup(id_exercise);

                Log.d(TAG, "LastGrT==" + id_group);
//                if(id_group==null){
//                    id_group=0L;
//                }

            }

        }).start();
        Log.d(TAG, "LastGrT2==" + id_group);
        return id_group;
    }
    public void playSpeech(String txtSpeech) {
        textToSpeech.speak((String) txtSpeech, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void setCount(String ind, boolean resTrain){
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentTime = Calendar.getInstance().getTime();
                AppDatabase.getInstance(getApplicationContext())
                        .countDao()
                        .insertOrUpdate(id_exercise, Integer.parseInt(ind), id_group, resTrain, currentTime);
                Log.d(TAG, "Update count=" + ind+" word="+wordTrain.getText()+" train="+resTrain);
            }

        }).start();

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
    public void checkTrain(String id, MaterialButton btn){
        btnWord1.setEnabled(false);
        btnWord2.setEnabled(false);
        btnWord3.setEnabled(false);
        btnWord4.setEnabled(false);

        btnWord1.setAlpha(0.7f);
        btnWord2.setAlpha(0.7f);
        btnWord3.setAlpha(0.7f);
        btnWord4.setAlpha(0.7f);

        if(passedTechName.equals("heartheword")){
            wordTrain.setVisibility(View.VISIBLE);
            wordTranscript.setVisibility(View.VISIBLE);
        }


        if(id_word.equals(id)){
            Log.d(TAG,"Win!!");
           // btn.setBackgroundColor(Color.GREEN);
           //btn.setBackgroundResource(R.drawable.green_stroke);
           // GradientDrawable drawable = (GradientDrawable)btn.getBackground();
           // drawable.setStroke(3, Color.GREEN);
           // btn.setStrokeWidth(10);
            btn.setStrokeColor(ColorStateList.valueOf(Color.GREEN));

            color=R.color.green;
            checkOk=true;

        }else{
            Log.d(TAG,"Lost!!!");
           // btn.setBackgroundColor(Color.RED);
           // btn.setStrokeWidth(10);
            btn.setStrokeColor(ColorStateList.valueOf(Color.RED));
           // btnOk.setBackgroundColor(Color.GREEN);
            btnOk.setStrokeColor(ColorStateList.valueOf(Color.GREEN));
            color=R.color.red;
            checkOk=false;

        }

        setColorCounter(checkCounter,color);
        setCount(id_word,checkOk);
        markWords[checkCounter]=checkOk;


        //checkCounter++;
        btnSkip.setText("NEXT");
        /*
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
        } */
    }
    public void makeSpin(){
        Log.d(TAG,"Make spin!");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                listGroups = AppDatabase.getInstance(getApplicationContext())
                        .groupDao()
                        .getGroupsForSpinner();
                listGroups.add(0,"Without groups");
                if(listGroups!=null) {
                    Log.d(TAG, "Make spin!" + listGroups.size());
                }

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                //String[] countries = { "Бразилия", "Аргентина", "Колумбия", "Чили", "Уругвай"};
                adapter = new ArrayAdapter(TrainActivity.this, R.layout.spinner_item_tr, listGroups);
                //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //adapter.setDropDownViewResource(R.layout.spinner_item_tr);
                // Применяем адаптер к элементу spinner
                spinner2.setAdapter(adapter);
//                if(id_group==null){
//                    id_group=0L;
//                }
                //int id_gr=id_group.intValue();
                //spinner2.setSelection(2);
                //spinner2.setText(spinner2.getAdapter().getItem(id_group).toString(), false);
                if(listGroups!=null && listGroups.size()>0) {
                    spinner2.setText(listGroups.get(id_group), false);
                }
                spinner2.setTextColor(Color.rgb(255, 165, 0));
                spinner2.setTextSize(22);
                textSpinner2.setHint("Select Group");
                if(listGroups!=null) {
                    Log.d(TAG, "Make spin3!" + listGroups.size());
                }else{
                    Log.d(TAG, "Make spin3!=null");
                }
               // startTrain();
            }
        }, 0);

            }
        });
        thread.start();

    }

    public void startTrain(){
        Log.d(TAG,"StartTrain===============================================================================");
        //if (listWords.size() != 0) {
            for(int i=0; i<limit; i++) {
                setColorCounter(i, R.color.yellow);
            }
            if(markWords!=null) {
                Arrays.fill(markWords, null);
            }

        checkCounter=0;
//        startOfDate = dateList.atStartOfDay();
//        endOfDate = LocalTime.MAX.atDate(dateList);
//
//        ZonedDateTime zdtStart = ZonedDateTime.of(startOfDate, ZoneId.systemDefault());
//        ZonedDateTime zdtEnd = ZonedDateTime.of(endOfDate, ZoneId.systemDefault());
//        startOfDay = zdtStart.toInstant().toEpochMilli();
//        endOfDay = zdtEnd.toInstant().toEpochMilli();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
               // id_group=spinner2.getSele();
               // id_group=spinner2.getSelectedItemId();
               // id_group=1L;
//                if(id_group==null){
//                    id_group=0L;
//                }
                Log.d(TAG, "id_gr="+id_group);

                if(id_group==0) {
                    Log.d(TAG, "id_gr2="+id_group+" "+id_exercise);
                    listWords = AppDatabase.getInstance(getApplicationContext())
                            .wordDao()
                            .getWordsTrainWithoutGroup2(id_exercise, limit, filterWord, false);
                }else {
                    listWords = AppDatabase.getInstance(getApplicationContext())
                            .wordDao()
                            .getWordsTrain2(id_exercise, id_group, limit, filterWord, false);

                }

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (listWords != null && listWords.size() > 0 && listWords.size() < limit) {

                            List<Dbwords> listWordsAdd = AppDatabase.getInstance(getApplicationContext())
                                    .wordDao()
                                    .getWordsTrainWithoutGroup2(id_exercise, limit - listWords.size(), filterWord, false);
                            listWords.addAll(listWordsAdd);

                        }
                        if (listWords != null) {
                            Log.d(TAG, "size=" + listWords.size() + " limit=" + limit + " offset=" + offset);
                            for (Dbwords w : listWords) {
                                Log.d(TAG, w.getWord() + " trainDateT=" + w.getTrainDate() + " train=" + w.getTrain1());
                            }
                        }
                        //markWords=new Boolean[listWords.size()];
                        markWords = new Boolean[limit];

                        makeScreen();

                    }
                }, 0);

            }
        });
        thread.start();


    }

    public void makeScreen(){

        if (listWords!=null && listWords.size() != 0) {

            btnSkip.setText("I don't know");
            btnSkip.setEnabled(true);
            btnSkip.setAlpha(1f);
            //listCheckWords=listWords;
            //Collections.copy(listCheckWords, listWords);

            //listCheckWords.addAll(listWords);
            listCheckWords = new ArrayList<>(listWords);
            if(passedTechName.equals("heartheword")){
                wordTrain.setVisibility(View.INVISIBLE);
                wordTranscript.setVisibility(View.INVISIBLE);
            }else{
                wordTranscript.setVisibility(View.VISIBLE);
            }
            Log.d(TAG,"Err="+checkCounter);
            wordTrain.setText(listWords.get(checkCounter).getWord());
            Log.d(TAG,"W="+listWords.get(checkCounter).getWord());
            wordTranscript.setText("["+listWords.get(checkCounter).getTranscript()+"]");
            id_word=String.valueOf(listWords.get(checkCounter).getId());
            listCheckWords.remove(checkCounter);

            Collections.shuffle(listCheckWords);
            //new Random().nextInt((max - min) + 1) + min
            id_random= getRandom(0,3);
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

           // val csl = ColorStateList.valueOf("#ffffff");
           // my_button.strokeColor = csl

            btnWord1.setText(listCheckWords.get(0).getTranslate());
            //btnWord1.setStroke(2, Color.WHITE);
            //btnWord1.setStrokeColor(ContextCompat.getColor(this, R.color.your_color_xml));
//            ShapeDrawable gradientDrawable = (ShapeDrawable)btnWord1.getBackground();
//            gradientDrawable.setStroke(2, your_color);
            btnWord1.setBackgroundColor(Color.WHITE);
            btnWord1.setStrokeColor(ColorStateList.valueOf(Color.WHITE));

            id_word1=String.valueOf(listCheckWords.get(0).getId());

            btnWord2.setText(listCheckWords.get(1).getTranslate());
            btnWord2.setBackgroundColor(Color.WHITE);
            btnWord2.setStrokeColor(ColorStateList.valueOf(Color.WHITE));
            id_word2=String.valueOf(listCheckWords.get(1).getId());

            btnWord3.setText(listCheckWords.get(2).getTranslate());
            btnWord3.setBackgroundColor(Color.WHITE);
            btnWord3.setStrokeColor(ColorStateList.valueOf(Color.WHITE));
            id_word3=String.valueOf(listCheckWords.get(2).getId());

            btnWord4.setText(listCheckWords.get(3).getTranslate());
            btnWord4.setBackgroundColor(Color.WHITE);
            btnWord4.setStrokeColor(ColorStateList.valueOf(Color.WHITE));
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
            btnWord1.setAlpha(1);
            btnWord2.setEnabled(true);
            btnWord2.setAlpha(1);
            btnWord3.setEnabled(true);
            btnWord3.setAlpha(1);
            btnWord4.setEnabled(true);
            btnWord4.setAlpha(1);

        }else{
            btnWord1.setEnabled(false);
            btnWord1.setAlpha(0f);
            btnWord2.setEnabled(false);
            btnWord2.setAlpha(0f);
            btnWord3.setEnabled(false);
            btnWord3.setAlpha(0f);
            btnWord4.setEnabled(false);
            btnWord4.setAlpha(0f);
            btnSkip.setEnabled(false);
            btnSkip.setAlpha(0f);
            wordTranscript.setVisibility(View.INVISIBLE);
            wordTrain.setVisibility(View.VISIBLE);
            wordTrain.setText("No words found, try another group");
           // btnSoundTr.setVisibility(View.INVISIBLE);

        }
    }

    private void showSimpleDialog(int position){
        //currentTime = Calendar.getInstance().getTime();
        //TextView text = (TextView) findViewById(R.id.text_dialog2);


        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_label_editor, null);

        ListView textList =  dialogView.findViewById(R.id.text_list);
        TextView textRes =  dialogView.findViewById(R.id.text_dialog);
        //TextView wordsResult3 = (TextView) dialogView.findViewById(R.id.text_dialog3);
        String textWords[]=new String[listWords.size()];
        String translWords[]=new String[listWords.size()];

        int countM=0;
        for(boolean b:markWords){
            Log.d(TAG,"b="+b);
            if (b==true){
                countM++;
            }
        }
        if(countM==10){
            textRes.setText("Excellent!");
        }else if(countM>7 & countM<10){
            textRes.setText("Very good!");
        }else if(countM>4 & countM<7){
            textRes.setText("Try again!");
        }else{
            textRes.setText("Exercises will help!");
        }

       int i=0;
        for(Dbwords w: listWords){

            textWords[i]=w.getWord() ;
            translWords[i]=" - "+w.getTranslate() ;
            i++;
       }

        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), textWords, translWords, markWords);
        textList.setAdapter(customAdapter);
//        ArrayAdapter<String> arr;
//       // arr = new ArrayAdapter<String>(this,android.R.id.list,tutorials);
//        arr = new ArrayAdapter<String>(this,R.layout.list_view,R.id.textView,textWords);
//        textList.setAdapter(arr);

//        if (word.getTrain1() != null && word.getTrain1() == true) {
//            dashWord.setCompoundDrawablesWithIntrinsicBounds(R.drawable.green_circle, 0, 0, 0);
//        } else {
//            dashWord.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_circle, 0, 0, 0);
//        }
 //       String res="";
//        for(Dbwords w: listWords){
//           // w.setCompoundDrawablesWithIntrinsicBounds(R.drawable.green_circle, 0, 0, 0);
//            res=res+w.getWord()+"-"+w.getTranslate() +"\n";
//        }
//        wordsResult2.setText(res);
        builder.setView(dialogView);
        //builder.setMessage("Do you want to delete this word from group?");
        //Setting message manually and performing action on button click
       // builder.setMessage("Do you want to continue this exercise?")
        builder.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'Yes' Button
//                        for(int i=0; i<limit; i++) {
//                            setColorCounter(i, R.color.yellow);
//                        }
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