package com.example.envocab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btnWordOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("log","My log!");
        btnWordOk=findViewById(R.id.btnWordOk);
        btnWordOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeWord();
            }
        });


        /*   AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "dbname").build();

        WordDao wordDao = db.wordDao();
        List<Word> words = wordDao.getAll();
        System.out.println(words);
       */
    }
    public void takeWord(){
        System.out.println("Ok");

    }
}