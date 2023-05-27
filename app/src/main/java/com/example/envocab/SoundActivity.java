package com.example.envocab;

import static android.app.PendingIntent.getActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class SoundActivity extends BaseActivity {
    //    private static final int MENU3 = 1;
    private static final String TAG = "SoundActivity";
    boolean isLoading = false;
    private RecyclerView wordsList;
    private WordsAdapter wordsAdapter;
    private List<Word> listWords;
    LinearLayoutManager layoutManager;
    ImageButton btnPlaySound;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        wordsList = findViewById(R.id.rv_words);
        layoutManager = new LinearLayoutManager(this);
        wordsList.setLayoutManager(layoutManager);
        btnPlaySound = findViewById(R.id.btnPlaySound);

        btnPlaySound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("testLogs", "Speech");
                playAutoSound3();

            }
        });

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
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                LocalDate today = LocalDate.now();
                LocalDateTime startOfDate = today.atStartOfDay();
                LocalDateTime endOfDate = LocalTime.MAX.atDate(today);

                ZonedDateTime zdtStart = ZonedDateTime.of(startOfDate, ZoneId.systemDefault());
                ZonedDateTime zdtEnd = ZonedDateTime.of(endOfDate, ZoneId.systemDefault());
                Long startOfDay = zdtStart.toInstant().toEpochMilli();
                Long endOfDay = zdtEnd.toInstant().toEpochMilli();

                listWords = AppDatabase.getInstance(getApplicationContext())
                        .wordDao()
                        .wordsForList(startOfDay, endOfDay, 1);
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
                    initScrollListener();
                }
            }
        });
    }
    private void initScrollListener() {
        wordsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                          @Override
                                          public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                              super.onScrollStateChanged(recyclerView, newState);
                                             // System.out.println("onScrollStateChanged");
                                              LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                                              View v = layoutManager.findViewByPosition(linearLayoutManager.findFirstCompletelyVisibleItemPosition());
                                              TextView textViewName
                                                      = (TextView) v.findViewById(R.id.tv_number_item);
                                              String selectedName = (String) textViewName.getText();
                                              System.out.println("onScrollStateChanged="+selectedName);
                                              //R.id.tv_number_item).toString());
                                          }
                                          @Override
                                          public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                              super.onScrolled(recyclerView, dx, dy);
                                              //System.out.println("onScrolled1");
                                              LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                                              if (!isLoading) {
                                                  System.out.println("onScrolled---222 "+linearLayoutManager.findFirstCompletelyVisibleItemPosition()+" "+linearLayoutManager.findLastCompletelyVisibleItemPosition());
                                                  if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == listWords.size() - 1) {
                                                      // bottom of list!
                                                      System.out.println("onScrolled222");
                                                       loadMore();
                                                      isLoading = true;
                                                  }
                                              }
                                          }
                                      }
        );
    }
    private void loadMore() {
      //  listWords.add(null);
      //  wordsAdapter.notifyItemInserted(listWords.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              //  listWords.remove(listWords.size() - 1);
                int scrollPosition = listWords.size();
                System.out.println("loadMore");
              // wordsAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                System.out.println("loadMore1111");
                // Next load more option is to be shown after every 10 items.
                int nextLimit = currentSize + 10;

                while (currentSize - 1 < nextLimit) {
                    //listWords.add("Item " + currentSize);
                    currentSize++;
                }

                wordsAdapter.notifyDataSetChanged();
                isLoading = false;
                System.out.println("Load more!");
            }

        }, 2000);
    }

    public void playAutoSound3(){

    }

    public void playAutoSound(){
        final Handler handler = new Handler();
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
            if(layoutManager.findLastVisibleItemPosition()<(wordsAdapter.getItemCount()-1)){
                layoutManager.smoothScrollToPosition(wordsList,new RecyclerView.State(),layoutManager.findLastCompletelyVisibleItemPosition()+1);
            //}else if(layoutManager.findLastVisibleItemPosition()==(wordsAdapter.getItemCount()-1)){
            }else {
                layoutManager.smoothScrollToPosition(wordsList,new RecyclerView.State(),0);
            }
            //layoutManager.getFocusedChild().
            //Log.d("Tag","Pos="+layoutManager.findLastVisibleItemPosition());
                System.out.println("TagLogsOne");
            }
            //int ind=getAdapterPosition();
        },0,3000);
    }
    public void playAutoSound2(){

        final int speedScroll = 1200;
        final Handler handler = new Handler();
        System.out.println("Before00000");
        View  loc=wordsList.getChildAt(8);
        System.out.println("Before1111"+loc);
       // RecyclerView.ViewHolder holder = wordsList.getChildViewHolder(loc);
        System.out.println("Before");
       // TextView txtName = holder.itemView.findViewById(R.id.tv_number_item);
        System.out.println("After");
      //  System.out.println("TTT="+txtName.getText().toString());
        final Runnable runnable = new Runnable() {
            int count = 0;
            boolean flag = true;


            @Override
            public void run() {
                if(layoutManager.findLastVisibleItemPosition()<(wordsAdapter.getItemCount()-1)){
                    layoutManager.smoothScrollToPosition(wordsList,new RecyclerView.State(),layoutManager.findLastCompletelyVisibleItemPosition()+1);
                    //}else if(layoutManager.findLastVisibleItemPosition()==(wordsAdapter.getItemCount()-1)){
                }else {
                    layoutManager.smoothScrollToPosition(wordsList,new RecyclerView.State(),0);
                }
                //Log.d("testLogs",String.valueOf(wordsAdapter.));
                //String title = ((TextView) wordsList.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.tv_number_item)).getText().toString();
                //wordsList.findViewHolderForAdapterPosition(0).itemView.findViewById()
                //Log.d("testLogs",title);
                handler.postDelayed(this,speedScroll);
//                if(count < wordsAdapter.getItemCount()){
//                    if(count==wordsAdapter.getItemCount()-1){
//                        flag = false;
//                    }else if(count == 0){
//                        flag = true;
//                    }
//                    if(flag) count++;
//                    else count--;
//
//                    wordsList.smoothScrollToPosition(count);
//                    Log.d("testLogs", String.valueOf(count));
//                    handler.postDelayed(this,speedScroll);
//                }
            }
        };

        handler.postDelayed(runnable,speedScroll);
    }



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