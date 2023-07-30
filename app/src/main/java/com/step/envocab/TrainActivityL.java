package com.step.envocab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
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

public class TrainActivityL extends BaseActivity {
    private Boolean markWords[];
    private SwitchCompat onlyMarkedWords;
    private Integer filterWord=null;
    private ArrayAdapter<String> adapter;
    private TextInputLayout textSpinner2;
    private AutoCompleteTextView spinner2;
    private List<String> listGroups;
    private EditText textEditWord;
    private TextInputLayout textInputWord;
    private TextToSpeech textToSpeech;
    private List<String> letters, lettersW;
    private int color;
    private Date currentTime;

    private LinearLayout layoutL, layoutW;
    private String theme = "";
    private String TAG = "TrainL";
    private String passedIdItem="", passedDestination = "", passedTechName = "", passedName = "";
    private int id_exercise;
    private TextView textNameTrain, wordTrain, wordTranscript;
    private List<Dbwords> listWords, listCheckWords;
    private int id_group=0;
    private int offset=0;
    private String id_word = "";
    private TextView textMess;
    private Button btnSkip, btnCheck;
    private ImageButton btnSoundTr;
    private ImageView countIm1, countIm2, countIm3, countIm4, countIm5, countIm6, countIm7, countIm8, countIm9, countIm10;

    private int checkCounter, limit = 10;

    private List<Button> lettersWord=new ArrayList<>();
    private List<Button> lettersButton=new ArrayList<>();
    private Animation animAlpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_train_l);

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        Boolean s1 = sh.getBoolean("themeApp", true);

        if (s1) {
            setContentView(R.layout.activity_train_l2);
            //layoutIdForListItem=R.layout.exercises_roster_item2;
            theme = "light";
        } else {
            setContentView(R.layout.activity_train_l);
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
            passedDestination = extras.getString("passedDestination");
//            passedId=intent.getStringExtra("data");
//            passedName=intent.getStringExtra("passedName");
            Log.d(TAG, "passL=" + passedTechName + " " + passedDestination);
        }
        id_exercise = Integer.parseInt(passedIdItem);

        textNameTrain = findViewById(R.id.text_name_train);
        passedName = '"'+passedName.substring(0, 1).toUpperCase() + passedName.substring(1)+'"';
        textNameTrain.setText(passedName);

        wordTrain = findViewById(R.id.word_train);
        wordTranscript = findViewById(R.id.word_transcript);
        textMess=findViewById(R.id.text_mess);

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

        spinner2 = findViewById(R.id.spinner_trL2);
        textSpinner2=findViewById(R.id.text_spinnerL2);

        checkLastGroup();

        spinner2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Object item = parent.getItemAtPosition(position);
//                if (item instanceof StudentInfo){
//                    StudentInfo student=(StudentInfo) item;
//                    doSomethingWith(student);
//                }
                String item2 = (String)parent.getItemAtPosition(position);
//            try {
//                id_group = (int)parent.getItemIdAtPosition(position);
//            }catch(NullPointerException e){
//                id_group=0;
//            }
                Long id_group0 = parent.getItemIdAtPosition(position);
                id_group=id_group0==null?0:id_group0.intValue();
                Log.d(TAG, "item2="+item2+" id_item="+String.valueOf(id_group));

                for (Button b2 : lettersWord) {
                    ViewGroup layout = (ViewGroup) b2.getParent();
                    if (null != layout) //for safety only  as you are doing onClick
                        layout.removeView(b2);
                }
                lettersWord.clear();


                for (Button b3 : lettersButton) {
                    ViewGroup layout = (ViewGroup) b3.getParent();
                    if (null != layout) //for safety only  as you are doing onClick
                        layout.removeView(b3);
                }
                lettersButton.clear();
                if(lettersW!=null) {
                    letters.clear();
                }
                if(lettersW!=null) {
                    lettersW.clear();
                }

                startTrain();
            }
        });

        onlyMarkedWords = findViewById(R.id.only_marked_words);
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

                for (Button b2 : lettersWord) {
                    ViewGroup layout = (ViewGroup) b2.getParent();
                    if (null != layout) //for safety only  as you are doing onClick
                        layout.removeView(b2);
                }
                lettersWord.clear();


                for (Button b3 : lettersButton) {
                    ViewGroup layout = (ViewGroup) b3.getParent();
                    if (null != layout) //for safety only  as you are doing onClick
                        layout.removeView(b3);
                }
                lettersButton.clear();
                if(lettersW!=null) {
                    letters.clear();
                }
                if(lettersW!=null) {
                    lettersW.clear();
                }

                startTrain();
            }
        });
        textInputWord = findViewById(R.id.text_input_word_w);
        textEditWord=findViewById(R.id.word_w);
        textEditWord.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textMess.setVisibility(View.INVISIBLE);
            }
        });

        btnSoundTr=findViewById(R.id.btn_sound_tr);
        btnCheck = findViewById(R.id.btn_word_check);
        if(passedTechName.equals("writeaword")) {
            textInputWord.setVisibility(View.VISIBLE);
            btnCheck.setVisibility(View.VISIBLE);
            btnSoundTr.setEnabled(true);
        }else{
            btnSoundTr.setEnabled(false);
        }

        btnSkip = findViewById(R.id.btn_word_skip);
        btnCheck.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG, "Checkkkk="+wordTrain.getText()+" textEditWord.getText()="+textEditWord.getText());
            String strWordTrain=String.valueOf(wordTrain.getText()).trim().toLowerCase();
            String strTextEdit=String.valueOf(textEditWord.getText()).trim().toLowerCase();
         if(strWordTrain.equals(strTextEdit)){
             Log.d(TAG, "Win11");
             color = R.color.green;
             setColorCounter(checkCounter, color);
             setCount(id_word, false);
             markWords[checkCounter]=false;
             //textMess.setVisibility(View.INVISIBLE);
             btnSkip.setText("NEXT");

             wordTrain.setVisibility(View.VISIBLE);

         }else{
             Log.d(TAG, "Lost11");
             color = R.color.red;
             textMess.setText("You made a mistake, try again");
             textMess.setVisibility(View.VISIBLE);
         }
        }
        });
        btnSkip.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (passedTechName.equals("writeaword")) {
                if (btnSkip.getText().equals("Next") || btnSkip.getText().equals("NEXT")) {
                    btnSkip.setText("I don't know");
                    textEditWord.setText("");
                    btnCheck.setEnabled(true);
                    btnCheck.setAlpha(1);

                    checkCounter++;
                    if (checkCounter < limit) {
                        Log.d(TAG, "ch0=" + checkCounter);

                        makeScreenLW();

                    } else {
                        checkCounter = 0;
                        showSimpleDialog(limit);
                        //btn.setBackgroundColor(Color.WHITE);

                    }

                }else {
                    btnCheck.setEnabled(false);
                    btnCheck.setAlpha(0.6f);
                    color = R.color.red;
                    setColorCounter(checkCounter, color);
                    setCount(id_word,false);
                    markWords[checkCounter]=false;
                    textMess.setVisibility(View.INVISIBLE);
                    btnSkip.setText("NEXT");

                    wordTrain.setVisibility(View.VISIBLE);



                }

            } else {

                if (btnSkip.getText().equals("Next") || btnSkip.getText().equals("NEXT")) {
                    btnSkip.setText("I don't know");
                    textMess.setVisibility(View.INVISIBLE);

                    for (Button b2 : lettersWord) {
                        ViewGroup layout = (ViewGroup) b2.getParent();
                        if (null != layout) //for safety only  as you are doing onClick
                            layout.removeView(b2);
                    }
                    lettersWord.clear();


                    for (Button b3 : lettersButton) {
                        ViewGroup layout = (ViewGroup) b3.getParent();
                        if (null != layout) //for safety only  as you are doing onClick
                            layout.removeView(b3);
                    }
                    lettersButton.clear();
                    letters.clear();
                    lettersW.clear();

                    //makeScreenL();
                    checkCounter++;
                    if (checkCounter < limit) {
                        Log.d(TAG, "ch0=" + checkCounter);

                        makeScreenL();

                    } else {
                        checkCounter = 0;
                        showSimpleDialog(limit);
                        //btn.setBackgroundColor(Color.WHITE);

                    }
                } else {
                    color = R.color.red;
                    setColorCounter(checkCounter, color);
                    Log.d(TAG, "SET COUNT id_word="+id_word);
                    setCount(id_word,false);
                    markWords[checkCounter]=false;
                    textMess.setVisibility(View.INVISIBLE);
                    // checkCounter++;
                    int jj = 0;
                    for (Button b : lettersWord) {
                        b.setText(lettersW.get(jj));
                        jj++;
                    }
                    btnSkip.setText("NEXT");
                }
            }
        }
        });

        btnSoundTr=findViewById(R.id.btn_sound_tr);

        btnSoundTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.startAnimation(animAlpha);
                playSpeech(wordTrain.getText().toString());
            }
        });
       // btnSoundTr.setEnabled(false);

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
    public void checkLastGroup() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                id_group= (int)AppDatabase.getInstance(getApplicationContext())
                        .countDao()
                        .lastGroup(id_exercise);

                Log.d(TAG, "LastGr==" + id_group);
//                if(id_group==null){
//                    id_group=0L;
//                }

            }

        }).start();
        //return lastGroup;
    }
    public void playSpeech(String txtSpeech) {
        textToSpeech.speak((String) txtSpeech, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void makeSpin(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                listGroups = AppDatabase.getInstance(getApplicationContext())
                        .groupDao()
                        .getGroupsForSpinner();
                listGroups.add(0,"Without groups");
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //String[] countries = { "Бразилия", "Аргентина", "Колумбия", "Чили", "Уругвай"};
                        adapter = new ArrayAdapter(TrainActivityL.this, R.layout.spinner_item_tr, listGroups);
                        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // Применяем адаптер к элементу spinner
                        spinner2.setAdapter(adapter);
                        if(listGroups!=null && listGroups.size()>0) {
                            spinner2.setText(listGroups.get(id_group), false);
                        }
                        spinner2.setTextColor(Color.rgb(255, 165, 0));
                        spinner2.setTextSize(22);

                        textSpinner2.setHint("Select Group");

                    }
                }, 0);


            }
        });
        thread.start();


        //startTrain();
    }

    public void startTrain(){
        checkCounter=0;
        if(markWords!=null) {
            Arrays.fill(markWords, null);
        }
        btnSkip.setText("I don't know");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "id_gr="+id_group);
//                if(id_group==null){
//                    id_group=0L;
//                }
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
                if(listWords!=null && listWords.size()>0 & listWords.size()<limit){

                    List<Dbwords> listWordsAdd = AppDatabase.getInstance(getApplicationContext())
                            .wordDao()
                            .getWordsTrainWithoutGroup2(id_exercise, limit-listWords.size(),filterWord, false);
                    listWords.addAll(listWordsAdd);

                }
                if(listWords!=null) {
                    Log.d(TAG, "size=" + listWords.size() + " limit=" + limit + " offset=" + offset);
                    for (Dbwords w : listWords) {
                        Log.d(TAG, w.getWord() + " trainDate=" + w.getTrainDate());
                    }
                    markWords = new Boolean[listWords.size()];
                }

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (listWords!=null && listWords.size() != 0) {
                            for(int i=0; i<listWords.size(); i++) {
                                setColorCounter(i, R.color.yellow);
                            }}
                        if(passedTechName.equals("collecttheword")) {
                            makeScreenL();
                        }else{
                            makeScreenLW();
                        }
                    }
                }, 0);
            }
        });
        thread.start();


    }
    public void makeScreenLW() {
        if (listWords!=null && listWords.size() != 0) {

            wordTrain.setVisibility(View.INVISIBLE);
            wordTranscript.setVisibility(View.INVISIBLE);

            btnSkip.setAlpha(1f);
            btnSkip.setEnabled(true);
            btnCheck.setAlpha(1f);
            btnCheck.setEnabled(true);

            textInputWord.setVisibility(View.VISIBLE);
            //   }
            String wordTrainText=listWords.get(checkCounter).getWord().trim();
            wordTrain.setText(wordTrainText);
            Log.d(TAG,"W="+listWords.get(checkCounter).getWord());
            id_word = String.valueOf(listWords.get(checkCounter).getId());
            wordTranscript.setText("["+listWords.get(checkCounter).getTranscript()+"]");

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

        }else{
            btnSkip.setAlpha(0f);
            btnSkip.setEnabled(false);
            btnCheck.setAlpha(0f);
            btnCheck.setEnabled(false);
            wordTrain.setVisibility(View.VISIBLE);
            wordTrain.setText("No words found, try another group");
            textInputWord.setVisibility(View.INVISIBLE);
        }
    }

    public void makeScreenL(){
        if (listWords!=null && listWords.size() != 0) {
            //listCheckWords=listWords;
            //Collections.copy(listCheckWords, listWords);

            //listCheckWords.addAll(listWords);
            listCheckWords = new ArrayList<>(listWords);
          //  if(passedTechName.equals("collecttheword")){
                wordTrain.setVisibility(View.INVISIBLE);
                wordTranscript.setVisibility(View.INVISIBLE);
         //   }
            btnSkip.setAlpha(1f);
            btnSkip.setEnabled(true);
            id_word = String.valueOf(listWords.get(checkCounter).getId());
            String wordTrainText=listWords.get(checkCounter).getWord().trim();
            wordTrain.setText(wordTrainText);
            Log.d(TAG,"W="+listWords.get(checkCounter).getWord()+" checkCounter="+checkCounter);
            wordTranscript.setText("["+listWords.get(checkCounter).getTranscript()+"]");

            letters=new ArrayList<>();


            for (int i = 0; i < wordTrainText.length(); i++) {
                letters.add(String.valueOf(wordTrainText.charAt(i)));
            }

            lettersW = new ArrayList<>(letters);
            int longLetters=letters.size();
            int rowX=0;
            double rowY=0;
            double inRow=7;
            if(longLetters<=inRow){
               rowX= longLetters;
               rowY=1;
            }else{
                rowX= (int)inRow;
                rowY=(int)Math. ceil(longLetters/inRow);
                //rowY=longLetters/inRow;
            }
            Log.d(TAG,"Length="+longLetters+" X="+rowX+" Y="+rowY);
            layoutW=findViewById(R.id.layout_word);
            layoutW.setOrientation(LinearLayout.VERTICAL);
            int lett=0;

            for (int i = 0; i < rowY; i++) {
                LinearLayout row = new LinearLayout(this);
                row.setLayoutParams(new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                // row.setMa
                for (int j = 0; j < rowX; j++) {
                    Button btnW = new Button(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            100,100
                    );
                    params.setMargins(0, 10, 8, 5);
                    btnW.setLayoutParams(params);
                    //btnW.setLayoutParams(new LinearLayout.LayoutParams
                           // (150, ViewGroup.LayoutParams.WRAP_CONTENT));
                           // (100, 100));


                    // LinearLayout.LayoutParams.MATCH_PARENT));
                    btnW.setTextColor(Color.BLACK);
                    btnW.setTextSize(20);
                    btnW.setTypeface(btnW.getTypeface(), Typeface.BOLD);
                    btnW.setBackgroundColor(Color.WHITE);

                    btnW.setPadding(0,0,0,10);
                    btnW.setBackground(getResources().getDrawable(R.drawable.letter_circle));

                    //btnTag.setText( letters.get(lett));

                    btnW.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG,btnW.getText().toString());
                            //Toast.makeText(MainActivity.this, R.string.welcome_message, Toast.LENGTH_LONG).show();
                            String letW=btnW.getText().toString();
                            if(!letW.equals("")) {
                                btnW.setText("");
                                Log.d(TAG, letW);
                                collectL(letW);
                                //btnTag.setEnabled(false);
                            }
                        }
                    });

                    //btnTag.setBackgroundColor(Color.WHITE);
                    //android.R.id.button1+lett
                    btnW.setId(lett);
                    btnW.setTag("w"+lett);
                    row.addView(btnW);
                    lettersWord.add(btnW);
                    // Log.d(TAG,"btn");
                    lett++;
                    if(lett==longLetters) break;
                }
                layoutW.addView(row);
            }

            //LinearLayout layoutL = new LinearLayout(this);




    layoutL = findViewById(R.id.layout_letter);
    layoutL.setOrientation(LinearLayout.VERTICAL);
    Collections.shuffle(letters);
    lett = 0;
    //layoutL.setMargins(20, 20, 30, 20);
    for (int i = 0; i < rowY; i++) {
        LinearLayout row = new LinearLayout(this);
        row.setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        // row.setMa
        for (int j = 0; j < rowX; j++) {
            Button btnTag = new Button(this);
            //btnTag.setLayoutParams(new LinearLayout.LayoutParams
                    //(150, ViewGroup.LayoutParams.WRAP_CONTENT));
            //        (100, 100));
            // LinearLayout.LayoutParams.MATCH_PARENT));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    100,100
            );
            params.setMargins(0, 5, 10, 5);

            btnTag.setLayoutParams(params);
            //btnTag.getPaint().setStrokeWidth(5);
            //btnTag.getPaint().strokeC
            btnTag.setTextColor(Color.BLACK);
            btnTag.setTextSize(20);
            btnTag.setTypeface(btnTag.getTypeface(), Typeface.BOLD);
            btnTag.setText(letters.get(lett));
            btnTag.setPadding(0,0,0,10);
            //btnTag.setBackground(getResources().getDrawable(R.drawable.letter_circle));
            btnTag.setBackgroundResource(R.drawable.letter_circle);
            btnTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String letW = btnTag.getText().toString();
                    if (!letW.equals("")) {
                        btnTag.setText("");
                        Log.d(TAG, letW);
                        collectW(letW);
                        btnTag.setEnabled(false);
                    }
                    //lettersWord.add(letW);
                    // Button b = (Button)findViewById(1);
                    //Toast.makeText(MainActivity.this, R.string.welcome_message, Toast.LENGTH_LONG).show();
                }
            });

            //btnTag.setBackgroundColor(Color.WHITE);

            //btnTag.setId(j + 1 + (i * 4));
            row.addView(btnTag);
            lettersButton.add(btnTag);
            lett++;
            if (lett == longLetters) break;
        }
        layoutL.addView(row);
          }
         }else{
            btnSkip.setAlpha(0f);
            btnSkip.setEnabled(false);
            wordTrain.setVisibility(View.VISIBLE);
            wordTrain.setText("No words found, try another group");
        }

    }
    public void collectW(String l){
        //lettersWord.add();
        String wordC="";

        for(Button b: lettersWord){
            wordC=wordC+b.getText();
          if(b.getText().equals("")){
              b.setText(l);
              wordC=wordC+l;
              Log.d(TAG,"WordC="+wordC);
              if(wordC.equals(wordTrain.getText())){
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
                  Log.d(TAG,"Win!!!");
                  color=R.color.green;
                  setColorCounter(checkCounter,color);
                  setCount(id_word,true);
                  markWords[checkCounter]=true;
                  textMess.setText("Word spelled correctly!");
                  textMess.setTextColor(getResources().getColor(R.color.green));
                  textMess.setVisibility(View.VISIBLE);
                  btnSkip.setText("NEXT");
//                  checkCounter++;
//                  /////////////////////////
//                  if(checkCounter<limit) {
//
//                      for(Button b3: lettersButton){
//                          ViewGroup layout = (ViewGroup) b3.getParent();
//                          if(null!=layout) //for safety only  as you are doing onClick
//                              layout.removeView(b3);
//                      }
//
//                      lettersButton.clear();
//                      new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                          @Override
//                          public void run() {
//                              for(Button b2: lettersWord){
//                                  ViewGroup layout = (ViewGroup) b2.getParent();
//                                  if(null!=layout) //for safety only  as you are doing onClick
//                                      layout.removeView(b2);
//                              }
//                              lettersWord.clear();
//                              makeScreenL();
//
//                          }
//                      }, 1000);
//                  }

                      ///////////////////////////
              }else{
                  if(wordC.length()==wordTrain.getText().length()) {
                      textMess.setVisibility(View.VISIBLE);
                      textMess.setText("You made a mistake, try again");
                      textMess.setTextColor(Color.RED);
                  }
              }
              break;
            }
        }
    }
    public void collectL(String l){
        textMess.setVisibility(View.INVISIBLE);
        String wordC="";
        for(Button b: lettersButton){
            //wordC=wordC+b.getText();
            if(b.getText().equals("")){
                b.setText(l);
                b.setEnabled(true);
               // wordC=wordC+l;
               // Log.d(TAG,"WordC="+wordC);
              //  if(wordC.equals(wordTrain.getText())){
               //     Log.d(TAG,"Win!!!");
              //  }
                break;
            }
        }
    }

    private void showSimpleDialog(int position){
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
        builder.setView(dialogView);
        //builder.setMessage("Do you want to delete this word from group?");
        //Setting message manually and performing action on button click
        //builder.setMessage("Do you want to continue this exercise?")
                builder.setCancelable(false)
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
                        Intent intent = new Intent(TrainActivityL.this, ExercisesActivity.class);
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
    public void setCount(String ind, boolean resTrain){
        Log.d(TAG,"word_id="+ind);
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentTime = Calendar.getInstance().getTime();
                AppDatabase.getInstance(getApplicationContext())
                        .countDao()
                        .insertOrUpdate(id_exercise, Integer.parseInt(ind), id_group, resTrain, currentTime);
                Log.d(TAG, "Update count=" + ind+" word="+wordTrain.getText()+" id_group="+id_group);
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

}