package com.example.envocab;
import android.speech.tts.TextToSpeech;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="MainActivity";
    String dateWithoutTime;
    Date currentTime;
    int uid;
    ImageButton btnSound;
    Button btnWordOk;
    Button btnWordStudy;
    Button btnWordTranslate;
    Button btnNext;
    Button btnPrev;

    TextView dashWord;
    TextView dashTranscript;
    TextView dashTrainDate;
    TextView translate;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Log.d("log","My log!");
        btnWordOk=findViewById(R.id.btnWordOk);
        btnWordStudy=findViewById(R.id.btnWordStudy);
        btnWordTranslate=findViewById(R.id.btnWordTranslate);
        btnNext=findViewById(R.id.btnNext);
        btnPrev=findViewById(R.id.btnPrev);
        btnSound=findViewById(R.id.btnSound);
        dashWord=findViewById(R.id.dashWord);
        dashTranscript=findViewById(R.id.dashTranscript);
        dashTrainDate=findViewById(R.id.dashTrainDate);
        translate=findViewById(R.id.dashTranslate);
        translate.setVisibility(View.INVISIBLE);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if(i!=TextToSpeech.ERROR){
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("testLogs","Speech");
                playSpeech();

            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {allWords();}
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {insWord();}
        });

        btnWordOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { updateWord(true);}
        });

        btnWordStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateWord(false);
            }
        });
        btnWordTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translate.setVisibility(translate.getVisibility()==View.VISIBLE ? View.GONE : View.VISIBLE);
                btnWordTranslate.setText(translate.getVisibility()==View.VISIBLE ? "HIDE TRANSLATE" : "SHOW TRANSLATE");
            }
        });

        takeWord();
    }
    public void playSpeech(){
        textToSpeech.speak((String) dashWord.getText(), TextToSpeech.QUEUE_FLUSH,null);
    }
    public void insWord() {
        //System.out.println("Ins");
        Word word=new Word("NewTest3", "translate3", "transcript3");
        InsertAsyncTask insertAsyncTask=new InsertAsyncTask();
        insertAsyncTask.execute(word);
    }

    public void updateWord(Boolean up){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Word word = AppDatabase.getInstance(getApplicationContext())
                        .wordDao()
                        .findById(uid);
                Log.d(TAG,"findBy="+uid+" word="+word.getWord());
                if (word != null) {
                    currentTime = Calendar.getInstance().getTime();
                    word.setTrain1(up);
                    word.setTrainDate(currentTime);
                    AppDatabase.getInstance(getApplicationContext())
                            .wordDao()
                            .updateWord(word);
                Log.d(TAG, "Update word="+word.getWord());
                takeWord();
                }
            }

        }).start();

    }
    public void allWords(){
         Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {
                List<Word> wordList=AppDatabase.getInstance(getApplicationContext())
                        .wordDao()
                        .getAll();
                Log.d(TAG, "run "+wordList.toString());

            }
        });
        thread.start();
    }
    public void takeWord(){
        Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {
                Word word=AppDatabase.getInstance(getApplicationContext())
                        .wordDao()
                        .findLast();
                Log.d(TAG, "Take Word="+word.getWord());
                if (word != null) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            uid = word.getId();
                            dashWord.setText(word.getWord());
                            if(word.getTrain1()==true) {
                                dashWord.setCompoundDrawablesWithIntrinsicBounds(R.drawable.green_circle, 0, 0, 0);
                            }else{
                                dashWord.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_circle, 0, 0, 0);
                            }

                            dashTranscript.setText(word.getTranscript());
                            if(word.getTrainDate()!=null) {
                                DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                dateWithoutTime = sdf.format(word.getTrainDate());
                                dashTrainDate.setText(dateWithoutTime);
                            }
                            translate.setVisibility(View.GONE);
                            btnWordTranslate.setText("SHOW TRANSLATE");
                            translate.setText(word.getTranslate());
                            Log.d(TAG, "Take Word setText " + word.getWord());
                        }
                    });
                }else{
                    Log.d(TAG,"Empty word");
                }
            }
        });
        thread.start();
    }

    class InsertAsyncTask extends AsyncTask<Word, Void, Void>{

        @Override
        protected Void doInBackground(Word... words) {
             AppDatabase.getInstance(getApplicationContext())
                    .wordDao()
                    .insertWord(words[0]);
             return null;
        }
    }
}