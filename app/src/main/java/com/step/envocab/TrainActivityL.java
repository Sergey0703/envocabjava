package com.step.envocab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class TrainActivityL extends BaseActivity {
    private TextToSpeech textToSpeech;
    private List<String> letters, lettersW;
    private int color;
    private LinearLayout layoutL, layoutW;
    private String theme = "";
    private String TAG = "Train";
    private String passedDestination = "", passedTechName = "", passedName = "";
    private TextView textNameTrain, wordTrain, wordTranscript;
    private List<Dbwords> listWords, listCheckWords;
    private int offset=0;
    private TextView textMess;
    private Button btnSkip;
    private ImageButton btnSoundTr;
    private ImageView countIm1, countIm2, countIm3, countIm4, countIm5, countIm6, countIm7, countIm8, countIm9, countIm10;

    private int checkCounter, limit = 10;

    private List<Button> lettersWord=new ArrayList<>();
    private List<Button> lettersButton=new ArrayList<>();
    Animation animAlpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_train_l);

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        Boolean s1 = sh.getBoolean("themeApp", true);

        if (s1) {
            setContentView(R.layout.activity_train_l);
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
            passedName = extras.getString("passedName");
            passedTechName = extras.getString("passedTechName");
            passedDestination = extras.getString("passedDestination");
//            passedId=intent.getStringExtra("data");
//            passedName=intent.getStringExtra("passedName");
            Log.d(TAG, "pass=" + passedTechName + " " + passedDestination);
        }

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

        btnSkip = findViewById(R.id.btn_word_skip);

        btnSkip.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(btnSkip.getText().equals("Next")||btnSkip.getText().equals("NEXT")) {
                btnSkip.setText("I don't know");

                for(Button b2: lettersWord){
                    ViewGroup layout = (ViewGroup) b2.getParent();
                    if(null!=layout) //for safety only  as you are doing onClick
                        layout.removeView(b2);
                }
                lettersWord.clear();


                for(Button b3: lettersButton){
                    ViewGroup layout = (ViewGroup) b3.getParent();
                    if(null!=layout) //for safety only  as you are doing onClick
                        layout.removeView(b3);
                }
                lettersButton.clear();
                letters.clear();
                lettersW.clear();

                //makeScreenL();
                checkCounter++;
                if(checkCounter<limit){
                    Log.d(TAG,"ch0="+checkCounter);

                    makeScreenL();

                }else{
                    checkCounter=0;
                    showSimpleDialog(limit);
                    //btn.setBackgroundColor(Color.WHITE);

                }
            }else{
                color = R.color.red;
                setColorCounter(checkCounter, color);
               // checkCounter++;
                int jj=0;
                for(Button b: lettersWord){
                    b.setText(lettersW.get(jj));
                    jj++;
                }
                btnSkip.setText("NEXT");
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
        btnSoundTr.setEnabled(false);

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
    public void startTrain(){
        checkCounter=0;

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
                makeScreenL();

            }
        }, 100);
    }

    public void makeScreenL(){
        if (listWords.size() != 0) {
            //listCheckWords=listWords;
            //Collections.copy(listCheckWords, listWords);

            //listCheckWords.addAll(listWords);
            listCheckWords = new ArrayList<>(listWords);
          //  if(passedTechName.equals("collecttheword")){
                wordTrain.setVisibility(View.INVISIBLE);
                wordTranscript.setVisibility(View.INVISIBLE);
         //   }
            String wordTrainText=listWords.get(checkCounter).getWord().trim();
            wordTrain.setText(wordTrainText);
            Log.d(TAG,"W="+listWords.get(checkCounter).getWord());
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
                    btnW.setLayoutParams(new LinearLayout.LayoutParams
                            (150, ViewGroup.LayoutParams.WRAP_CONTENT));

                    // LinearLayout.LayoutParams.MATCH_PARENT));
                    btnW.setTextColor(Color.BLACK);
                    btnW.setTextSize(20);
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


//            Button btnShow = new Button(this);
//            btnShow.setText(R.string.show_text);
//            btnShow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            layoutL.addView(btnShow);

            layoutL=findViewById(R.id.layout_letter);
            layoutL.setOrientation(LinearLayout.VERTICAL);
            Collections.shuffle(letters);
            lett=0;
            //layoutL.setMargins(20, 20, 30, 20);
            for (int i = 0; i < rowY; i++) {
                LinearLayout row = new LinearLayout(this);
                row.setLayoutParams(new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
               // row.setMa
                for (int j = 0; j < rowX; j++) {
                    Button btnTag = new Button(this);
                    btnTag.setLayoutParams(new LinearLayout.LayoutParams
                            (150, ViewGroup.LayoutParams.WRAP_CONTENT));

                    // LinearLayout.LayoutParams.MATCH_PARENT));
                    btnTag.setTextColor(Color.BLACK);
                    btnTag.setTextSize(20);
                    btnTag.setText( letters.get(lett));
                    btnTag.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String letW=btnTag.getText().toString();
                            if(!letW.equals("")) {
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
                    if(lett==longLetters) break;
                }
                layoutL.addView(row);
            }

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
//                  btnSoundTr.performClick();
//                  btnSoundTr.setPressed(true);
//                  btnSoundTr.invalidate();
//                  // delay completion till animation completes
//                  btnSoundTr.postDelayed(new Runnable() {  //delay button
//                      public void run() {
//                          btnSoundTr.setPressed(false);
//                          btnSoundTr.invalidate();
//                          //any other associated action
//                      }
//                  }, 100);
                  Log.d(TAG,"Win!!!");
                  color=R.color.green;
                  setColorCounter(checkCounter,color);
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