package com.example.envocab;
import static com.example.envocab.Converters.dateToTimestamp;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="MainActivity";
    DatabaseHelper databaseHelper;

    Word word;
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
    TextView dashWordsTodayCount;
    TextView dashWordsTodayBadCount;

    Long trainDateLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        databaseHelper=new DatabaseHelper(this,"dictdb",1);
//        try{
//            databaseHelper.CheckDatabase();
//        }catch (Exception e){
//             e.printStackTrace();
//        }
//        try{
//            databaseHelper.OpenDatabase();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
        databaseHelper = new DatabaseHelper(getApplicationContext());
        // создаем базу данных
        databaseHelper.create_db();

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
        dashWordsTodayCount=findViewById(R.id.dashWordsTodayCount);
        dashWordsTodayBadCount=findViewById(R.id.dashWordsTodayBadCount);

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
            public void onClick(View v) {takeWord("Next");}
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //insWord();
                takeWord("Prev");
            }
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
                //allWords();
            }
        });

        takeWord("");
    }
    public void playSpeech(){
        textToSpeech.speak((String) dashWord.getText(), TextToSpeech.QUEUE_FLUSH,null);
    }
    public void insWord() {
        //System.out.println("Ins");
        Word word=new Word("NewTest2", "translate2", "transcript2");
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

                if (word != null) {
                    Log.d(TAG,"findBy="+uid+" word="+word.getWord());
                    currentTime = Calendar.getInstance().getTime();
                    Log.d(TAG, "currentTime="+currentTime);
                    word.setTrain1(up);
                    word.setTrainDate(currentTime);
                    AppDatabase.getInstance(getApplicationContext())
                            .wordDao()
                            .updateWord(word);
                Log.d(TAG, "Update word="+word.getWord());
                takeWord("");
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
                //Log.d(TAG, "run "+wordList.toString());
                for(Word w: wordList){
                    Log.d(TAG,w.toString());
                }

            }
        });
        thread.start();
    }
    public void takeWord(String nav){
        Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {
                word = null;
                Log.d(TAG,"trainDateLong="+trainDateLong);
                if(nav=="") {
                    word = AppDatabase.getInstance(getApplicationContext())
                            .wordDao()
                            .findLast();
                }else if(nav=="Next"){
                    word = AppDatabase.getInstance(getApplicationContext())
                            .wordDao()
                            .findNext(trainDateLong);
                    if(word == null){
                        word = AppDatabase.getInstance(getApplicationContext())
                                .wordDao()
                                .findLast();
                        Log.d(TAG,"nav=");
                    }

                }else if(nav=="Prev"){
                    word = AppDatabase.getInstance(getApplicationContext())
                            .wordDao()
                            .findPrev(trainDateLong);
                    if(word == null){
                        word = AppDatabase.getInstance(getApplicationContext())
                                .wordDao()
                                .findPrevAdd();
                        Log.d(TAG,"nav=");
                    }
                    Log.d(TAG,"nav="+nav);
                }

                if (word != null) {
                    Log.d(TAG, "Take Word="+word.getWord());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            uid = word.getId();
                            trainDateLong= Long.valueOf(0);
                            dashWord.setText(word.getWord());
                            if(word.getTrain1()!=null&&word.getTrain1()==true) {
                                dashWord.setCompoundDrawablesWithIntrinsicBounds(R.drawable.green_circle, 0, 0, 0);
                            }else{
                                dashWord.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_circle, 0, 0, 0);
                            }

                            dashTranscript.setText(word.getTranscript());
                            if(word.getTrainDate()!=null) {
                                DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                dateWithoutTime = sdf.format(word.getTrainDate());
                                Log.d(TAG,"dateWithoutTime="+dateWithoutTime);
                                dashTrainDate.setText(dateWithoutTime);
                                trainDateLong=dateToTimestamp(word.getTrainDate());

                            }
                            translate.setVisibility(View.GONE);
                            btnWordTranslate.setText("SHOW TRANSLATE");
                            translate.setText(word.getTranslate());

                            Log.d(TAG, "Take Word setText " + word.getWord());
                            Log.d(TAG, "Date " + dateToTimestamp(word.getTrainDate()));
                            //countWordsToday();
                        }
                    });
                    countWordsToday(0);
                    countWordsToday(1);
                }else{
                    Log.d(TAG,"Empty word");
                }
            }
        });
        thread.start();
    }

    public void countWordsToday(int typeWords){
        Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {
                LocalDate today = LocalDate.now() ;
                LocalDateTime startOfDate = today.atStartOfDay();
                LocalDateTime endOfDate = LocalTime.MAX.atDate(today);
                //java.sql.Timestamp.valueOf(startOfDay);
                //java.sql.Date.valueOf(startOfDay);
                //Long st=Converters.dateToTimestamp(startOfDay);
                ZonedDateTime zdtStart = ZonedDateTime.of(startOfDate, ZoneId.systemDefault());
                ZonedDateTime zdtEnd = ZonedDateTime.of(endOfDate, ZoneId.systemDefault());
                Long startOfDay=zdtStart.toInstant().toEpochMilli();
                Long endOfDay=zdtEnd.toInstant().toEpochMilli();

//                Date dateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy HH:mm:ss")
//                LocalDate today2 = LocalDate.now();   // your current date time
//                LocalDateTime start2= today2.atStartOfDay(); // date time at start of the date
                //Long start2.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(); // start time to timestamp
                Log.d(TAG, "start date ="+startOfDay);
                Log.d(TAG, "End date2 ="+endOfDay);

               // Log.d("Date:", "start date parsed "+startOfDay.format(dateFormatter)}")

                //word.getTrainDate()
                int count=AppDatabase.getInstance(getApplicationContext())
                           .wordDao()
                           .countToday(startOfDay,endOfDay, typeWords);

                Log.d(TAG, "countToday="+count);
                //if (count != 0) {
                   // dashWordsTodayCount.setText(count);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if(typeWords==0){
                                dashWordsTodayBadCount.setText(String.valueOf(count));
                            }else {
                                dashWordsTodayCount.setText(String.valueOf(count));
                            }
                        }
                    });
//                }else{
//                    Log.d(TAG,"Empty Count");
//                }
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