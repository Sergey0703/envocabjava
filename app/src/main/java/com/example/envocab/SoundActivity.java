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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
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
    Handler handler=null;
    Runnable runnable;
    Handler handlerMain=null;
    Runnable runnableMain;
    private static final String TAG = "SoundActivity";
    boolean isLoading = false;
    private RecyclerView wordsList;
    private WordsAdapter wordsAdapter;
    private List<Word> listWords;
    LinearLayoutManager layoutManager;
    Button btnPlaySound;

    TextToSpeech textToSpeech;
    TextToSpeech textToSpeechTr;
    boolean playSoundOn;
    Switch speechTranslate;
    Switch speechCategory;
    String selectedTranslate;

    int speedScroll = 4000;
    LocalDate today;
    LocalDateTime startOfDate ;
    LocalDateTime endOfDate;
    Long startOfDay;
    Long endOfDay;

    @Override
    protected void onStop() {
        super.onStop();
        if(handler!=null) {
            handler.removeCallbacks(runnable);
            handler = null;
            playSoundOn = false;
            btnPlaySound.setBackgroundResource(R.drawable.play_circle);
            ViewCompat.setBackgroundTintList(btnPlaySound, ContextCompat.getColorStateList(getApplicationContext(), R.color.purple_500));

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        wordsList = findViewById(R.id.rv_words);
        layoutManager = new LinearLayoutManager(this);
        wordsList.setLayoutManager(layoutManager);
        btnPlaySound = findViewById(R.id.buttonPlaySound);
        speechTranslate = (Switch) findViewById(R.id.speechTranslate);
        speechCategory = (Switch) findViewById(R.id.speechCategory);
        //btnPlaySound2 = findViewById(R.id.btnPlaySound2);

        today = LocalDate.now();
        startOfDate = today.atStartOfDay();
        endOfDate = LocalTime.MAX.atDate(today);

        ZonedDateTime zdtStart = ZonedDateTime.of(startOfDate, ZoneId.systemDefault());
        ZonedDateTime zdtEnd = ZonedDateTime.of(endOfDate, ZoneId.systemDefault());
        startOfDay = zdtStart.toInstant().toEpochMilli();
        endOfDay = zdtEnd.toInstant().toEpochMilli();
        speechCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                System.out.println("Switch!!!!!!");
                onStop();
                if(speechCategory.isChecked()) {
                    dataToList(true);
                }else{
                    dataToList(false);
                }
            }
        });
        speechTranslate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onStop();
                if(speechCategory.isChecked()) {
                    dataToList(true);
                }else{
                    dataToList(false);
                }
            }
        });
        btnPlaySound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("testLogs", "Speech");
                if(!playSoundOn) {
                    playSoundOn = true;
                    btnPlaySound.setBackgroundResource(R.drawable.pause_circle);
                    //btnPlaySound.setBackgroundTint();
                    ViewCompat.setBackgroundTintList(btnPlaySound, ContextCompat.getColorStateList(getApplicationContext(), R.color.red));
                    playAutoSound2();
                }else{
                    if(handler!=null) {
                        handler.removeCallbacks(runnable);
                        handler = null;
                    }
                    playSoundOn = false;
                    btnPlaySound.setBackgroundResource(R.drawable.play_circle);
                    ViewCompat.setBackgroundTintList(btnPlaySound, ContextCompat.getColorStateList(getApplicationContext(), R.color.purple_500));
                }
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
        textToSpeechTr = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if(i!=TextToSpeech.ERROR){
                    // To Choose language of speech
                    Locale locRu = new Locale("ru");
                    textToSpeechTr.setLanguage(locRu);
                }
            }
        });
        dataToList(false);
    }
    public void dataToList(boolean typeCategory){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                if(typeCategory) {
                    System.out.println("Only Bad!!!!");
                    listWords = AppDatabase.getInstance(getApplicationContext())
                            .wordDao()
                            .wordsForList(startOfDay, endOfDay, 0);
                    System.out.println("Size="+listWords.size());
                }else{
                    System.out.println("All word!!!!");
                    listWords = AppDatabase.getInstance(getApplicationContext())
                            .wordDao()
                            .wordsForListAll(startOfDay, endOfDay);
                            //.wordsForListAllTest();
                    System.out.println("Size2="+listWords.size());
                }
            }
        });
        thread.start();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
        //new Handler(thread.postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("Run!!!");
                if (listWords.size() != 0) {
                    System.out.println("NewList!!");
                    wordsList.setHasFixedSize(true);
                    wordsAdapter = new WordsAdapter(listWords);
                    wordsList.setAdapter(wordsAdapter);
                    // initScrollListener();
                }
            }
        },500);
    }
//    public void dataToList2(boolean typeCategory){
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                if(typeCategory) {
//                    System.out.println("Only Bad!!!!");
//                    listWords = AppDatabase.getInstance(getApplicationContext())
//                            .wordDao()
//                            .wordsForList(startOfDay, endOfDay, 0);
//                    System.out.println("SizeB="+listWords.size());
//                }else{
//                    System.out.println("All word!!!!");
//                    listWords = AppDatabase.getInstance(getApplicationContext())
//                            .wordDao()
//                            //.wordsForListAll(startOfDay, endOfDay);
//                            .wordsForListAllTest();
//                    System.out.println("Size2="+listWords.size());
//                }
//            }
//        });
//        thread.start();
//
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//
//            @Override
//            public void run() {
//                System.out.println("Run!!!");
//                if (listWords.size() != 0) {
//                    System.out.println("NewList!!");
//                    wordsList.setHasFixedSize(true);
//                    wordsAdapter = new WordsAdapter(listWords);
//                    wordsList.setAdapter(wordsAdapter);
//                    // initScrollListener();
//                }
//            }
//        });
//    }
    public void playSpeech(String txtSpeech){
        textToSpeech.speak((String) txtSpeech, TextToSpeech.QUEUE_FLUSH,null);
    }
    public void playSpeechTr(String txtSpeech){
        textToSpeechTr.speak((String) txtSpeech, TextToSpeech.QUEUE_FLUSH,null);
    }
    private void initScrollListener() {
    wordsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                          @Override
                                          public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                              super.onScrollStateChanged(recyclerView, newState);
                                             // System.out.println("onScrollStateChanged");
                                              LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                                              int top=linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                                              View v = layoutManager.findViewByPosition(top);
                                              TextView textViewName
                                                      = (TextView) v.findViewById(R.id.tv_number_item);
                                              String selectedName = (String) textViewName.getText();
                                              System.out.println(top+"= onScrollStateChanged="+selectedName);
                                              //R.id.tv_number_item).toString());
                                          }
                                          @Override
                                          public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                              super.onScrolled(recyclerView, dx, dy);
                                              //System.out.println("onScrolled1");
                                              LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                                              if (!isLoading) {
                                                  //System.out.println("onScrolled---222 "+linearLayoutManager.findFirstCompletelyVisibleItemPosition()+" "+linearLayoutManager.findLastCompletelyVisibleItemPosition());
                                                  if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == listWords.size() - 1) {
                                                      // bottom of list!
                                                     // System.out.println("onScrolled222");
                                                     //  loadMore();
                                                      isLoading = true;
                                                  }
                                              }
                                          }
                                      }
        );
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
        if(handler!=null) return;

        handler = new Handler();

        runnable = new Runnable() {
            int count = 0;
            boolean flag = true;

            @Override
            public void run() {

                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
               // LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //int top=linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                int top=layoutManager.findFirstCompletelyVisibleItemPosition();
                View v = layoutManager.findViewByPosition(top);
                TextView textViewName
                        = (TextView) v.findViewById(R.id.tv_number_item);
                String selectedName = (String) textViewName.getText();

                System.out.println("!!!!!!!!!!"+top+"= onScrollStateChanged="+selectedName);
                //Log.d("testLogs",String.valueOf(wordsAdapter.));
                //String title = ((TextView) wordsList.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.tv_number_item)).getText().toString();
                //wordsList.findViewHolderForAdapterPosition(0).itemView.findViewById()
                //Log.d("testLogs",title);

                playSpeech(selectedName);
             //   System.out.println(top+"= onScrollStateChanged="+selectedName);
                if (speechTranslate.isChecked()) {
                    TextView textViewTranslate
                            = (TextView) v.findViewById(R.id.tv_holder_number);
                    selectedTranslate= (String) textViewTranslate.getText();
                    selectedTranslate=selectedTranslate.trim();
                    if(selectedTranslate.length()>32) {
                        int endOfWord=selectedTranslate.indexOf(" ",22);

                        selectedTranslate = selectedTranslate.substring(0, endOfWord);
                    }
                   // Handler handler2 = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // действие будет выполнено через 2с
                            playSpeechTr(selectedTranslate);
                            //       System.out.println(top+"= Transl="+selectedTranscr);


                        }
                    }, 1000);
                }else{
                    speedScroll=2000;
                }


                handler.postDelayed(new Runnable() {
                    public void run() {

                        if(layoutManager.findLastVisibleItemPosition()<(wordsAdapter.getItemCount()-1)){
                            layoutManager.smoothScrollToPosition(wordsList,new RecyclerView.State(),layoutManager.findLastCompletelyVisibleItemPosition()+1);
                            //}else if(layoutManager.findLastVisibleItemPosition()==(wordsAdapter.getItemCount()-1)){
                        }else {
                            layoutManager.smoothScrollToPosition(wordsList,new RecyclerView.State(),0);
                        }

                    }
                }, 2000);

                handler.postDelayed(this,3000);
                System.out.println("=======================================================");


            }
        };
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        handler.postDelayed(runnable,1000);
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