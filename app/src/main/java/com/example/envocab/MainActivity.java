package com.example.envocab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.util.Calendar;
import java.util.Date;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="MainActivity";

    int uid;
    Button btnWordOk;

    Button btnWordStudy;
    Button btnWordTranslate;
    Button btnNext;
    Button btnPrev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("log","My log!");
        btnWordOk=findViewById(R.id.btnWordOk);
        btnWordStudy=findViewById(R.id.btnWordStudy);
        btnWordTranslate=findViewById(R.id.btnWordTranslate);
        btnNext=findViewById(R.id.btnNext);
        btnPrev=findViewById(R.id.btnPrev);

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
            public void onClick(View view) {
                updateWord(true);
            }
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
                //updateWord();
            }
        });

        /*   AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "dbname").build();
        WordDao wordDao = db.wordDao();
        List<Word> words = wordDao.getAll();
        System.out.println(words);
       */
    }
    public void insWord() {
        System.out.println("Ins");
        Word word=new Word("NewTest!!!", "translate", "transcript");
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
                    Date currentTime = Calendar.getInstance().getTime();
                    word.setTrain1(up);
                    word.setTrainDate(currentTime);
                    AppDatabase.getInstance(getApplicationContext())
                            .wordDao()
                            .updateWord(word);
                }
            }
        }).start();
        takeWord();
    }
    public void allWords(){
        System.out.println("Ok");
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
        System.out.println("Ok");
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

    class InsertAsyncTask extends AsyncTask<Word, Void, Void>{

        @Override
        protected Void doInBackground(Word... words) {
            System.out.println("w0="+words[0]);
            AppDatabase.getInstance(getApplicationContext())
                    .wordDao()
                    .insertWord(words[0]);
            System.out.println("w="+words[0]);
            return null;
        }
    }
}