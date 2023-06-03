package com.example.envocab;

import static android.app.PendingIntent.getActivity;

import static java.lang.System.exit;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

public class SoundActivity extends BaseActivity implements WordListInterface{
    //    private static final int MENU3 = 1;
    private Handler handler=null;
    private Handler ihandler=null;
    private Handler mHandler=new Handler();
    private Runnable runnable;
    private Context context;
//    Handler handlerMain=null;
//    Runnable runnableMain;
    private static final String TAG = "SoundActivity";
    boolean isLoading = false;
    private RecyclerView wordsList;
    private WordsAdapter wordsAdapter;
    private List<Word> listWords, listWordsForAdd;
    LinearLayoutManager layoutManager;
    Button btnPlaySound;
    Button btnPrevDay;
    Button btnNextDay;


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

    boolean loading = true;

    @Override
    protected void onPause() {
        super.onPause();
        if(handler!=null) {
            handler.removeCallbacks(runnable);
            handler = null;
            playSoundOn = false;
            btnPlaySound.setBackgroundResource(R.drawable.play_circle);
            ViewCompat.setBackgroundTintList(btnPlaySound, ContextCompat.getColorStateList(getApplicationContext(), R.color.purple_500));
            //exit(0);
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
        btnPrevDay = findViewById(R.id.btnPrevDay);
        btnNextDay = findViewById(R.id.btnNextDay);


        speechCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //System.out.println("Switch!!!!!!");
                onStop();
                //if(speechCategory.isChecked()) {
                //    dataToList(true);
                //}else{
                    dataToList("");
                //}
            }
        });
        speechTranslate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onStop();
                //if(speechCategory.isChecked()) {
                    dataToList("");
                //}else{
                //    dataToList(false);
               // }
            }
        });

        btnPrevDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataToList("prev");
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
                    playAutoSound3();
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
        dataToList("");
    }
    public void dataToList(String nav){
        today = LocalDate.now().minusDays(1);
        startOfDate = today.atStartOfDay();
        endOfDate = LocalTime.MAX.atDate(today);

        ZonedDateTime zdtStart = ZonedDateTime.of(startOfDate, ZoneId.systemDefault());
        ZonedDateTime zdtEnd = ZonedDateTime.of(endOfDate, ZoneId.systemDefault());
        startOfDay = zdtStart.toInstant().toEpochMilli();
        endOfDay = zdtEnd.toInstant().toEpochMilli();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(speechCategory.isChecked()) {
                //if(typeCategory) {
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
                listWordsForAdd=listWords;
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
                    wordsAdapter = new WordsAdapter(listWords, SoundActivity.this  );
                    wordsList.setAdapter(wordsAdapter);
                    initScrollListener();
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
        Log.d(TAG,"INIT");
        //int pastVisiblesItems, visibleItemCount, totalItemCount;
        wordsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            Log.d(TAG,"INIT2 "+dy+" "+dx);
            if (dy >= 0) { //check for scroll down
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                Log.d(TAG, "visibleItemCount="+visibleItemCount+" totalItemCount="+totalItemCount+" pastVisiblesItems="+pastVisiblesItems);
                if (loading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = false;
                        Log.d(TAG, "Last Item Wow !");
                        // Do pagination.. i.e. fetch new data
                        listWords.addAll(listWordsForAdd);
                        loading = true;
                    }
                }
            }
        }
    });

//    wordsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                                          @Override
//                                          public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                                              super.onScrollStateChanged(recyclerView, newState);
//                                             // System.out.println("onScrollStateChanged");
//                                              LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                                              int top=linearLayoutManager.findFirstCompletelyVisibleItemPosition();
//                                              View v = layoutManager.findViewByPosition(top);
//                                              TextView textViewName
//                                                      = (TextView) v.findViewById(R.id.tv_number_item);
//                                              String selectedName = (String) textViewName.getText();
//                                              System.out.println(top+"= onScrollStateChanged="+selectedName);
//                                              //R.id.tv_number_item).toString());
//                                          }
//                                          @Override
//                                          public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                                              super.onScrolled(recyclerView, dx, dy);
//                                              //System.out.println("onScrolled1");
//                                              LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//
//                                              if (!isLoading) {
//                                                  //System.out.println("onScrolled---222 "+linearLayoutManager.findFirstCompletelyVisibleItemPosition()+" "+linearLayoutManager.findLastCompletelyVisibleItemPosition());
//                                                  if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == listWords.size() - 1) {
//                                                      // bottom of list!
//                                                     // System.out.println("onScrolled222");
//                                                     //  loadMore();
//                                                      isLoading = true;
//                                                  }
//                                              }
//                                          }
//                                      }
//        );
    }

//    public void playAutoSound(){
//        final Handler handler = new Handler();
//        Timer timer=new Timer();
//        timer.schedule(new TimerTask() {
//
//            @Override
//            public void run() {
//            if(layoutManager.findLastVisibleItemPosition()<(wordsAdapter.getItemCount()-1)){
//                layoutManager.smoothScrollToPosition(wordsList,new RecyclerView.State(),layoutManager.findLastCompletelyVisibleItemPosition()+1);
//            //}else if(layoutManager.findLastVisibleItemPosition()==(wordsAdapter.getItemCount()-1)){
//            }else {
//                layoutManager.smoothScrollToPosition(wordsList,new RecyclerView.State(),0);
//            }
//            //layoutManager.getFocusedChild().
//            //Log.d("Tag","Pos="+layoutManager.findLastVisibleItemPosition());
//                System.out.println("TagLogsOne");
//            }
//            //int ind=getAdapterPosition();
//        },0,3000);
//    }
///////////////////////////////////////////////////////////////
public void playAutoSound4(){
    if(handler!=null) return;

    handler = new Handler();
    //int top=0;
    //int newTop=0;
    Thread thread = new Thread(new Runnable() {
        // runnable = new Runnable() {

        @Override
        public void run() {
            int top=layoutManager.findFirstCompletelyVisibleItemPosition();
            onItemClick2(top);

//                speedScroll=4000;
//            }else{
//                speedScroll=2000;
//            }

            if(handler==null) return;
            handler.postDelayed(new Runnable() {
                public void run() {
                    //int top=layoutManager.findFirstCompletelyVisibleItemPosition();
                    //newTop=top+1;
                    //layoutManager.smoothScrollToPosition(wordsList,new RecyclerView.State(),newTop);
                    layoutManager.scrollToPositionWithOffset(top+1, 0);
                    //layoutManager.
                    //RecyclerView.SmoothScroller.setTargetPosition(snapTarget);
                    //layoutManager.startSmoothScroll(newTop);
                    Log.d(TAG,"NewTop "+String.valueOf(top+1));
//                        if(layoutManager.findLastVisibleItemPosition()<(wordsAdapter.getItemCount()-1)){
//                            layoutManager.smoothScrollToPosition(wordsList,new RecyclerView.State(),layoutManager.findLastCompletelyVisibleItemPosition()+1);
//                            //}else if(layoutManager.findLastVisibleItemPosition()==(wordsAdapter.getItemCount()-1)){
//                            Log.d(TAG,"PositionL="+layoutManager.findLastVisibleItemPosition());
//                        }else {
//                            layoutManager.smoothScrollToPosition(wordsList,new RecyclerView.State(),0);
//                            Log.d(TAG,"Position="+0);
//                        }
                    //textViewName.setAllCaps(false);
                    //card.setCardElevation(17.5f);

                }
            }, speedScroll-1000); //3000 & 1000
            if(handler==null) return;
            handler.postDelayed(this,speedScroll); //4000

            //System.out.println("=======================================================");
            //Log.d(TAG,"after layout =======================================================");

        }
    });
    thread.start();
    //System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    Log.d(TAG,"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    //handler.postDelayed(runnable,1000);
}
    ///////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////
    public void playAutoSound3(){
        if(handler!=null) return;

        handler = new Handler();
        //int top=0;
        //int newTop=0;
        Thread thread = new Thread(new Runnable() {
       // runnable = new Runnable() {

            @Override
            public void run() {

                //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                Log.d(TAG,"run!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" );
                int top=layoutManager.findFirstCompletelyVisibleItemPosition();

                View v = layoutManager.findViewByPosition(top);
                CardView card=(CardView) v.findViewById(R.id.cardWord);
               // System.out.println("Elev="+card.getCardElevation());
                card.setCardElevation(100f);
                //v.setBackgroundColor(Color.CYAN);
                //card.setCardBackgroundColor(Color.BLUE);

                TextView textViewName
                        = (TextView) v.findViewById(R.id.tv_number_item);
                //textViewName.setAllCaps(true);
                String selectedName = (String) textViewName.getText();

                //System.out.println("!!!!!!!!!!"+top+"= onScrollStateChanged="+selectedName);
                Log.d(TAG,top+"= onScrollStateChanged="+selectedName);

                //handler.postDelayed(new Runnable() {
                if(handler==null) return;
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Log.d(TAG,+top+"= Speech="+selectedName);

                        playSpeech(selectedName);
                    }
                }, 1);

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
                    if(handler==null) return;
                    handler.postDelayed(new Runnable() {
                        public void run() {

                            playSpeechTr(selectedTranslate);
                        }
                    }, 1500);
                    speedScroll=4000;
                }else{
                    speedScroll=2000;
                }

                if(handler==null) return;
                handler.postDelayed(new Runnable() {
                    public void run() {
                        //newTop=top+1;
                        //layoutManager.smoothScrollToPosition(wordsList,new RecyclerView.State(),newTop);
                        layoutManager.scrollToPositionWithOffset(top+1, 0);
                        //layoutManager.
                        //RecyclerView.SmoothScroller.setTargetPosition(snapTarget);
                        //layoutManager.startSmoothScroll(newTop);
                        Log.d(TAG,"NewTop "+String.valueOf(top+1));
//                        if(layoutManager.findLastVisibleItemPosition()<(wordsAdapter.getItemCount()-1)){
//                            layoutManager.smoothScrollToPosition(wordsList,new RecyclerView.State(),layoutManager.findLastCompletelyVisibleItemPosition()+1);
//                            //}else if(layoutManager.findLastVisibleItemPosition()==(wordsAdapter.getItemCount()-1)){
//                            Log.d(TAG,"PositionL="+layoutManager.findLastVisibleItemPosition());
//                        }else {
//                            layoutManager.smoothScrollToPosition(wordsList,new RecyclerView.State(),0);
//                            Log.d(TAG,"Position="+0);
//                        }
                        //textViewName.setAllCaps(false);
                        card.setCardElevation(17.5f);

                    }
                }, speedScroll-1000); //3000 & 1000
                if(handler==null) return;
                handler.postDelayed(this,speedScroll); //4000

                //System.out.println("=======================================================");
                //Log.d(TAG,"after layout =======================================================");

            }
        });
        thread.start();
        //System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        Log.d(TAG,"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        //handler.postDelayed(runnable,1000);
    }
    ///////////////////////////////////////////////////////////////
//    wordsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            if (dy > 0) { //check for scroll down
//                visibleItemCount = mLayoutManager.getChildCount();
//                totalItemCount = mLayoutManager.getItemCount();
//                pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
//
//                if (loading) {
//                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
//                        loading = false;
//                        Log.v("...", "Last Item Wow !");
//                        // Do pagination.. i.e. fetch new data
//
//                        loading = true;
//                    }
//                }
//            }
//        }
//    });

    ////////////////////////////////////////////////////////////////
    public void startRepeating(View view){
        //mHandler.postDelayed(mToastRunnable,5000);
        mToastRunnable.run();
    }

    public void StopRepeating(View view){
            mHandler.removeCallbacks(mToastRunnable);
    }
    private Runnable mToastRunnable=new Runnable() {
        @Override
        public void run() {
            Toast.makeText(SoundActivity.this,"This is delayed toast", Toast.LENGTH_SHORT).show();
            mHandler.postDelayed(this,5000);
        }
    };
    /////////////////////////////////////////////////////////////////////////

    public void onItemClick2(int position) {
        int top = position;
        Log.d(TAG, "position0=" + top);
        ihandler = new Handler();
        //int top=0;
        //int newTop=0;
//        Thread thread = new Thread(new Runnable() {
//            // runnable = new Runnable() {
//
//            @Override
//            public void run() {


                Log.d(TAG, "position=" + top);
                View v = layoutManager.findViewByPosition(top);
                CardView card = (CardView) v.findViewById(R.id.cardWord);
                // System.out.println("Elev="+card.getCardElevation());
                card.setCardElevation(100f);
                TextView textViewName
                        = (TextView) v.findViewById(R.id.tv_number_item);
                //textViewName.setAllCaps(true);
                String selectedName = (String) textViewName.getText();

                //System.out.println("!!!!!!!!!!"+top+"= onScrollStateChanged="+selectedName);
                Log.d(TAG, top + "= onScrollStateChanged=" + selectedName);

                //handler.postDelayed(new Runnable() {
                if (ihandler == null) return;
                ihandler.postDelayed(new Runnable() {
                    public void run() {
                        Log.d(TAG, +top + "= Speech=" + selectedName);

                        playSpeech(selectedName);
                    }
                }, 1);

                //   System.out.println(top+"= onScrollStateChanged="+selectedName);
                if (speechTranslate.isChecked()) {
                    TextView textViewTranslate
                            = (TextView) v.findViewById(R.id.tv_holder_number);
                    selectedTranslate = (String) textViewTranslate.getText();
                    selectedTranslate = selectedTranslate.trim();
                    if (selectedTranslate.length() > 32) {
                        int endOfWord = selectedTranslate.indexOf(" ", 22);

                        selectedTranslate = selectedTranslate.substring(0, endOfWord);
                    }
                    // Handler handler2 = new Handler();
                    if (ihandler == null) return;
                    ihandler.postDelayed(new Runnable() {
                        public void run() {

                            playSpeechTr(selectedTranslate);

                        }
                    }, 1500);
                    if (ihandler == null) return;
                    ihandler.postDelayed(new Runnable() {
                        public void run() {

                            card.setCardElevation(17.5f);

                            ihandler.removeCallbacks(runnable);
                            ihandler = null;
                        }
                    }, 3000);


                }
           // }
//        });
//        thread.start();


    }
    ///////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onItemClick(int position) {
        int top = position;
        Log.d(TAG, "position0=" + top);
        handler = new Handler();
        //int top=0;
        //int newTop=0;
        Thread thread = new Thread(new Runnable() {
            // runnable = new Runnable() {

            @Override
            public void run() {


                Log.d(TAG, "position=" + top);
                View v = layoutManager.findViewByPosition(top);
                CardView card = (CardView) v.findViewById(R.id.cardWord);
                // System.out.println("Elev="+card.getCardElevation());
                card.setCardElevation(100f);
                TextView textViewName
                        = (TextView) v.findViewById(R.id.tv_number_item);
                //textViewName.setAllCaps(true);
                String selectedName = (String) textViewName.getText();

                //System.out.println("!!!!!!!!!!"+top+"= onScrollStateChanged="+selectedName);
                Log.d(TAG, top + "= onScrollStateChanged=" + selectedName);

                //handler.postDelayed(new Runnable() {
                if (handler == null) return;
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Log.d(TAG, +top + "= Speech=" + selectedName);

                        playSpeech(selectedName);
                    }
                }, 1);

                //   System.out.println(top+"= onScrollStateChanged="+selectedName);
                if (speechTranslate.isChecked()) {
                    TextView textViewTranslate
                            = (TextView) v.findViewById(R.id.tv_holder_number);
                    selectedTranslate = (String) textViewTranslate.getText();
                    selectedTranslate = selectedTranslate.trim();
                    if (selectedTranslate.length() > 32) {
                        int endOfWord = selectedTranslate.indexOf(" ", 22);

                        selectedTranslate = selectedTranslate.substring(0, endOfWord);
                    }
                    // Handler handler2 = new Handler();
                    if (handler == null) return;
                    handler.postDelayed(new Runnable() {
                        public void run() {

                            playSpeechTr(selectedTranslate);

                        }
                    }, 1500);
                    if (handler == null) return;
                    handler.postDelayed(new Runnable() {
                        public void run() {

                           card.setCardElevation(17.5f);
                           if(runnable!=null) {
                               handler.removeCallbacks(runnable);
                           }
                            handler = null;
                        }
                    }, 3000);


                }
            }
        });
        thread.start();


    }
    ///////////////////////////////////////////////////////////////
//    public void playAutoSound2(){
//        if(handler!=null) return;
//
//        handler = new Handler();
//
//        runnable = new Runnable() {
//           // int count = 0;
//          //  boolean flag = true;
//
//            @Override
//            public void run() {
//
//                //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                Log.d(TAG,"run!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" );
//               // LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                //int top=linearLayoutManager.findFirstCompletelyVisibleItemPosition();
//                int top=layoutManager.findFirstCompletelyVisibleItemPosition();
//
//                View v = layoutManager.findViewByPosition(top);
//                CardView card=(CardView) v.findViewById(R.id.cardWord);
//                System.out.println("Elev="+card.getCardElevation());
//                card.setCardElevation(100f);
//                //v.setBackgroundColor(Color.CYAN);
//                //card.setCardBackgroundColor(Color.BLUE);
//
//                TextView textViewName
//                        = (TextView) v.findViewById(R.id.tv_number_item);
//                //textViewName.setAllCaps(true);
//                String selectedName = (String) textViewName.getText();
//
//                //System.out.println("!!!!!!!!!!"+top+"= onScrollStateChanged="+selectedName);
//                Log.d(TAG,"!!!!!!!!!!"+top+"= onScrollStateChanged="+selectedName);
//                //Log.d("testLogs",String.valueOf(wordsAdapter.));
//                //String title = ((TextView) wordsList.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.tv_number_item)).getText().toString();
//                //wordsList.findViewHolderForAdapterPosition(0).itemView.findViewById()
//                //Log.d("testLogs",title);
//
//                playSpeech(selectedName);
//             //   System.out.println(top+"= onScrollStateChanged="+selectedName);
//                if (speechTranslate.isChecked()) {
//                    TextView textViewTranslate
//                            = (TextView) v.findViewById(R.id.tv_holder_number);
//                    selectedTranslate= (String) textViewTranslate.getText();
//                    selectedTranslate=selectedTranslate.trim();
//                    if(selectedTranslate.length()>32) {
//                        int endOfWord=selectedTranslate.indexOf(" ",22);
//
//                        selectedTranslate = selectedTranslate.substring(0, endOfWord);
//                    }
//                   // Handler handler2 = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        public void run() {
//
//                            playSpeechTr(selectedTranslate);
//                                                  }
//                    }, 2000);
//                }else{
//                    speedScroll=2000;
//                }
//
//
//                handler.postDelayed(new Runnable() {
//                    public void run() {
//
//                        if(layoutManager.findLastVisibleItemPosition()<(wordsAdapter.getItemCount()-1)){
//                            layoutManager.smoothScrollToPosition(wordsList,new RecyclerView.State(),layoutManager.findLastCompletelyVisibleItemPosition()+1);
//                            //}else if(layoutManager.findLastVisibleItemPosition()==(wordsAdapter.getItemCount()-1)){
//                        }else {
//                            layoutManager.smoothScrollToPosition(wordsList,new RecyclerView.State(),0);
//                        }
//                        //textViewName.setAllCaps(false);
//                        card.setCardElevation(17.5f);
//                    }
//                }, 2000);
//
//
//                handler.postDelayed(this,4000);
//                //System.out.println("=======================================================");
//                Log.d(TAG,"after layout =======================================================");
//
//            }
//        };
//        //System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//        Log.d(TAG,"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//        handler.postDelayed(runnable,1000);
//    }
/////////////////////////////////////////////////////////////////


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