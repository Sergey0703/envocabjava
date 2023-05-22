package com.example.envocab;

import static android.app.PendingIntent.getActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class SoundActivity extends BaseActivity {
    //    private static final int MENU3 = 1;
    private static final String TAG = "SoundActivity";
    private RecyclerView wordsList;
    private WordsAdapter wordsAdapter;
    private List<Word> listWords;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        wordsList = findViewById(R.id.rv_words);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        wordsList.setLayoutManager(layoutManager);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                LocalDate today = LocalDate.now() ;
                LocalDateTime startOfDate = today.atStartOfDay();
                LocalDateTime endOfDate = LocalTime.MAX.atDate(today);

                ZonedDateTime zdtStart = ZonedDateTime.of(startOfDate, ZoneId.systemDefault());
                ZonedDateTime zdtEnd = ZonedDateTime.of(endOfDate, ZoneId.systemDefault());
                Long startOfDay=zdtStart.toInstant().toEpochMilli();
                Long endOfDay=zdtEnd.toInstant().toEpochMilli();

                 listWords = AppDatabase.getInstance(getApplicationContext())
                         .wordDao()
                         .wordsForList(startOfDay,endOfDay, 1);
                 //for(Word w: listWords){
                    // Log.d(TAG,w.toString());
                //}

            }
        });
        thread.start();

            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    if (listWords.size() != 0) {
                    wordsList.setHasFixedSize(true);
                    wordsAdapter = new WordsAdapter(listWords);
                    wordsList.setAdapter(wordsAdapter);
                }
            }
            });

        //    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        MenuItem menu3 = menu.findItem(MENU3);
//        if(menu3 == null){
//            menu3 = menu.add(Menu.NONE, MENU3, 3, "Menu No. 3");
//        }
//        return true;
//    }
//@Override
//public boolean onOptionsItemSelected(MenuItem item) {
//
//    super.onOptionsItemSelected(item);
//    switch (item.getItemId()) {
//
//        case MENU3:
//            Toast.makeText(this, "Clicked: Menu No. 3", Toast.LENGTH_SHORT).show();
//            return true;
//
//        default:
//            return super.onOptionsItemSelected(item);
//    }
//
//}
    }
}