package com.step.envocab;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SoundActivity extends BaseActivity implements WordListInterface {
    private Long trainDateLong;
    private Integer filterWord=null;

    private TextInputLayout textSpinnerS;
    private AutoCompleteTextView spinnerS;
    private List<String> listGroups;
    private ArrayAdapter<String> adapter;
    private Date currentTime;
    private Date lastTrain;
    private int limit = 10;
    private int offset = 0;
    private Handler handler = null;
    private Handler ihandler = null;
    private Handler mHandler = new Handler();
    private Runnable runnable;
    private Context context;
    private static final String TAG = "SoundActivity";
    //boolean isLoading = false;
    private RecyclerView wordsList;
    private WordsAdapter wordsAdapter;
    private List<Dbwords> listWords, listWordsForAdd;
    private LinearLayoutManager layoutManager;
    private Button btnPlaySound;
    private Button btnPrevDay;
    private Button btnNextDay;
    private int id_group;
    private int id_exercise=5;

    TextToSpeech textToSpeech;
    TextToSpeech textToSpeechTr;
    boolean playSoundOn;
    SwitchCompat speechTranslate;
    SwitchCompat speechCategory;
    SwitchCompat allStudyWords;

    String selectedTranslate;
    TextView textCaution;
    int speedScroll = 4000;
    LocalDate today, dateList;
    LocalDateTime startOfDate;
    LocalDateTime endOfDate;
    Long startOfDay;
    Long endOfDay;
    boolean loading = true;
    Animation animAlpha;
    private int layoutIdForListItem;
    private String theme="";

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        if (handler != null) {
            if (runnable != null) {
                handler.removeCallbacks(runnable);
            }
            handler = null;
            playSoundOn = false;
            btnPlaySound.setBackgroundResource(R.drawable.play_circle);
            ViewCompat.setBackgroundTintList(btnPlaySound, ContextCompat.getColorStateList(getApplicationContext(), R.color.purple_500));

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        Boolean s1 = sh.getBoolean("themeApp", true);

        if(s1) {
            setContentView(R.layout.activity_sound2);
            layoutIdForListItem=R.layout.word_list_item2;
            theme="light";
        }else{
            setContentView(R.layout.activity_sound);
            layoutIdForListItem=R.layout.word_list_item;
            theme="dark";
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.action_toolbar);
        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        today = LocalDate.now();
        dateList = today;

        layoutManager = new LinearLayoutManager(this);
        wordsList = findViewById(R.id.rv_words);
        wordsList.setLayoutManager(layoutManager);
        btnPlaySound = findViewById(R.id.buttonPlaySound);
        speechTranslate =  findViewById(R.id.speechTranslate);
        speechCategory =  findViewById(R.id.speechCategory);
        btnPrevDay = findViewById(R.id.btnPrevDay);
        btnNextDay = findViewById(R.id.btnNextDay);
        allStudyWords = findViewById(R.id.allStudyWords);
        textCaution=findViewById(R.id.caution);



        animAlpha= AnimationUtils.loadAnimation(this, R.anim.alpha);

        spinnerS = findViewById(R.id.spinner_s);
        textSpinnerS=findViewById(R.id.text_spinner_s);

        checkLastGroup();

        spinnerS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Object item = parent.getItemAtPosition(position);
//                if (item instanceof StudentInfo){
//                    StudentInfo student=(StudentInfo) item;
//                    doSomethingWith(student);
//                }
                String item2 = (String)parent.getItemAtPosition(position);
//                try {
//                id_group = (int) parent.getItemIdAtPosition(position);
//                }catch(NullPointerException e){
//                    id_group=0;
//                }
                Long id_group0 = parent.getItemIdAtPosition(position);
                id_group=id_group0==null?0:id_group0.intValue();

                if(id_group==0){
                    allStudyWords.setVisibility(View.INVISIBLE);
                }
                //int id_gr=id_group.intValue();
                Log.d(TAG, "item2="+item2+" id_item="+String.valueOf(id_group));
                allStudyWords.setChecked(false);
                filterWord=null;
                makeSpin();
                dataToList("");

            }
        });


        allStudyWords.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                onStop();
                if (allStudyWords.isChecked()) {
                 //   speechCategory.setVisibility(View.INVISIBLE);
                    filterWord=1;
                    Log.d(TAG, "Words only by date");
                } else {
                 //   speechCategory.setVisibility(View.VISIBLE);
                    Log.d(TAG, "All words for study");
                    filterWord=null;
                }
                dataToList("");
            }
        });
        speechCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //System.out.println("Switch!!!!!!");
                onStop();
                if (speechCategory.isChecked()) {
                    speechCategory.setText("All words on the date");
                    Log.d(TAG, "All words on the date");
                } else {
                    speechCategory.setText("Marked words");
                    Log.d(TAG, "Marked words by date");
                }
                dataToList("");
            }
        });
        speechTranslate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onStop();
                if (speechTranslate.isChecked()) {
                    speechTranslate.setText("Speak translation ON");
                    Log.d(TAG, "Speak translation ON");
                } else {
                    speechTranslate.setText("Speak translation OFF");
                    Log.d(TAG, "Speak translation OFF");
                }

                dataToList("");

            }
        });

        btnPrevDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                dataToList("prev");
            }
        });

        btnNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                btnNextDay.setEnabled(false);
                dataToList("next");
//                if(id_group==0){
//                    dataToList("next");
//                }else {
//                    setCount();
//                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            dataToList("next");
//
//                        }
//                    }, 500);
//
//                }
                btnNextDay.setEnabled(true);
            }
        });
        btnPlaySound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listWords!=null && listWords.size()==0) return;
                Log.d("testLogs", "Speech");
                if (!playSoundOn) {
                    view.startAnimation(animAlpha);
                    playSoundOn = true;
                    btnPlaySound.setBackgroundResource(R.drawable.pause_circle);
                    //btnPlaySound.setBackgroundTint();
                    ViewCompat.setBackgroundTintList(btnPlaySound, ContextCompat.getColorStateList(getApplicationContext(), R.color.red));
                    playAutoSound3();
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else {
                    if (handler != null) {
                        handler.removeCallbacks(runnable);
                        handler = null;
                    }
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    playSoundOn = false;
                    btnPlaySound.setBackgroundResource(R.drawable.play_circle);
                    if(theme.equals("light")) {
                        ViewCompat.setBackgroundTintList(btnPlaySound, ContextCompat.getColorStateList(getApplicationContext(), R.color.purple_500));
                    }else {
                        ViewCompat.setBackgroundTintList(btnPlaySound, ContextCompat.getColorStateList(getApplicationContext(), R.color.green));
                    }
                }
            }
        });

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if (i != TextToSpeech.ERROR) {
                    // To Choose language of speech
                    Locale loc = new Locale("en","GB");
                    //textToSpeech.setLanguage(Locale.UK);
                    textToSpeech.setLanguage(loc);
                }
            }
        });
        textToSpeechTr = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if (i != TextToSpeech.ERROR) {
                    // To Choose language of speech
                    Locale loc = new Locale("uk","UA");
                    textToSpeechTr.setLanguage(loc);
                }
            }
        });
        makeSpin();
       dataToList("");
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
                adapter = new ArrayAdapter(SoundActivity.this, R.layout.spinner_item_tr, listGroups);
                //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Применяем адаптер к элементу spinner
                spinnerS.setAdapter(adapter);
                //spinnerS.setText(spinnerS.getAdapter().getItem(id_group).toString(), false);
                if(listGroups!=null && listGroups.size()>0) {
                    spinnerS.setText(listGroups.get(id_group), false);
                }
                spinnerS.setTextColor(Color.rgb(255, 165, 0));
                spinnerS.setTextSize(22);

                textSpinnerS.setHint("Select Group");

            }
        }, 0);

            }
        });
        thread.start();
        //dataToList("");
    }

//    public void setCount(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for(Dbwords word: listWords) {
//                    int ind=word.getId();
//                    Date currentTimeUp = Calendar.getInstance().getTime();
//                    AppDatabase.getInstance(getApplicationContext())
//                            .countDao()
//                            .insertOrUpdate(id_exercise, ind, id_group, true, currentTimeUp);
//                  //  lastTrain=currentTimeUp;
//                    Log.d(TAG, "Update count=" + ind + " word=" + word.getWord());
//                }
//            }
//
//        }).start();
//       // dataToList("");
//    }




    public void dataToList(String nav) {
        if(id_group==-5) {
            Log.d(TAG, "today=" + String.valueOf(today) + " dateList=" + String.valueOf(dateList));
            if (nav == "prev") {
                dateList = dateList.minusDays(1);
            } else if (nav == "next") {
                if (!dateList.isEqual(today)) dateList = dateList.plusDays(1);
            }
            Log.d(TAG, "dateList=" + String.valueOf(dateList));
            startOfDate = dateList.atStartOfDay();
            endOfDate = LocalTime.MAX.atDate(dateList);

            ZonedDateTime zdtStart = ZonedDateTime.of(startOfDate, ZoneId.systemDefault());
            ZonedDateTime zdtEnd = ZonedDateTime.of(endOfDate, ZoneId.systemDefault());
            startOfDay = zdtStart.toInstant().toEpochMilli();
            endOfDay = zdtEnd.toInstant().toEpochMilli();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (allStudyWords.isChecked()) {
                        Log.d(TAG, "All BAD!!!!");
                        listWords = AppDatabase.getInstance(getApplicationContext())
                                .wordDao()
                                .wordsForListAll(0);
                        //System.out.println("Size=" + listWords.size());
                    } else if (speechCategory.isChecked()) {
                        Log.d(TAG, "All word!!!!");
                        listWords = AppDatabase.getInstance(getApplicationContext())
                                .wordDao()
                                .wordsForListAll(startOfDay, endOfDay);

                    } else {
                        Log.d(TAG, "Only BAD!!!!");
                        listWords = AppDatabase.getInstance(getApplicationContext())
                                .wordDao()
                                .wordsForList(startOfDay, endOfDay, 0);
                        //.wordsForListAllTest();

                    }
                    listWordsForAdd = listWords;
                    if (listWords!=null && listWords.size() < 4) {
                        listWords.addAll(listWordsForAdd);
                    }
                }
            });
            thread.start();
        }else {
            if (nav == "prev") {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
//                    listWords = AppDatabase.getInstance(getApplicationContext())
//                            .wordDao()
//                            .getWordsSound(id_group, limit, offset);

                        currentTime = Calendar.getInstance().getTime();
                        Log.d(TAG,"curr="+currentTime +" lastTrain="+lastTrain);
                        if(lastTrain!=null ){
                        //  currentTime=lastTrain;
                        }
                        //trainDateLong = Converters.dateToTimestamp(word.getTrainDate());
                        trainDateLong = Converters.dateToTimestamp(currentTime);
                        Log.d(TAG,"curr="+currentTime+" offset="+offset );
                        listWords = AppDatabase.getInstance(getApplicationContext())
                                .wordDao()
                                .getWordsTrainPrev(id_exercise,  id_group,trainDateLong ,limit,offset, null, false);

                        for(Dbwords ww:listWords){
                            Log.d(TAG, "GET prev="+ww.getWord()+" "+ ww.getTrainDate()+" offset="+offset);
                        }

                        if(listWords!=null && listWords.size()>0) {
                             lastTrain = listWords.get(listWords.size() - 1).getTrainDate();
                            //lastTrain = listWords.get(0).getTrainDate();
                            // Log.d(TAG,"lastTrain0="+lastTrain);
                        }

                      //  Collections.reverse(listWords);
                        Log.d(TAG, "Sound " + id_group + " size=" + listWords.size() + " id_exercise=" + id_exercise);
                        listWordsForAdd = listWords;
                        if (listWords!=null && listWords.size() < 4) {
                            listWords.addAll(listWordsForAdd);
                        }
                        offset=offset+limit;
                    }
                });
                thread.start();

            } else {



            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (nav == "next") {
                        Date currentTimeUp = Calendar.getInstance().getTime();
                        for (Dbwords word : listWords) {
                            int ind = word.getId();

                            AppDatabase.getInstance(getApplicationContext())
                                    .countDao()
                                    .insertOrUpdate(id_exercise, ind, id_group, true, currentTimeUp);

                            Log.d(TAG, "Update count=" + ind + " word=" + word.getWord());
                        }
                        lastTrain=Calendar.getInstance().getTime();
                        offset=0;
                    }

                    Log.d(TAG, "After next");
//                    listWords = AppDatabase.getInstance(getApplicationContext())
//                            .wordDao()
//                            .getWordsSound(id_group, limit, offset);
                    if(id_group==0) {
                        listWords = AppDatabase.getInstance(getApplicationContext())
                                .wordDao()
                                .getWordsTrainWithoutGroup2(id_exercise, limit, filterWord, false);
                    }else {

                        listWords = AppDatabase.getInstance(getApplicationContext())
                                .wordDao()
                                .getWordsTrain2(id_exercise, id_group, limit, filterWord, false);
                    }
                    Log.d(TAG, "Sound " + id_group + " size=" +  " id_exercise=" + id_exercise+" filterWord="+filterWord);


                    if(listWords!=null) {
                        for(Dbwords ww:listWords){
                            Log.d(TAG, "GET word="+ww.getWord()+" "+ ww.getTrainDate());
                        }
                    }
                    listWordsForAdd = listWords;

                    if (listWords!=null && listWords.size() < 4) {
                        listWords.addAll(listWordsForAdd);
                    }
                }
            });
            thread.start();
        }
        }

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                 if (listWords!=null && listWords.size() != 0) {
//                     LinearLayoutManager manager = new LinearLayoutManager(getParent());
//                     wordsList.setLayoutManager(manager);
//                     //listWords.add();
                    wordsList.setHasFixedSize(true);
                    wordsAdapter = new WordsAdapter(listWords, SoundActivity.this,layoutIdForListItem );
                    wordsList.setAdapter(wordsAdapter);
                    if(wordsList.getVisibility()==View.GONE){
                        wordsList.setVisibility(View.VISIBLE);
                    }
                     textCaution.setVisibility(View.GONE);
                    initScrollListener();
                     }else{
                     //wordsList.setAdapter(null);
                     wordsList.setVisibility(View.GONE);
                     textCaution.setVisibility(View.VISIBLE);
                 }
            }
        }, 500);
    }

    public void playSpeech(String txtSpeech) {
        textToSpeech.speak((String) txtSpeech, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void playSpeechTr(String txtSpeech) {
        textToSpeechTr.speak((String) txtSpeech, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void initScrollListener() {
        Log.d(TAG, "INIT");
       wordsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.d(TAG, "INIT2 " + dy + " " + dx);
                if (dy >= 0) { //check for scroll down
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                    Log.d(TAG, "visibleItemCount=" + visibleItemCount + " totalItemCount=" + totalItemCount + " pastVisiblesItems=" + pastVisiblesItems);
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
    }

///////////////////////////////////////////////////////////////

    public void playAutoSound3() {
        if (handler != null) return;

        handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            // runnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                int top = layoutManager.findFirstCompletelyVisibleItemPosition();
                if(top<0) return;
                View v = layoutManager.findViewByPosition(top);
//                int pos = wordsAdapter.getAdapterPosition();
//                if (pos != RecyclerView.NO_POSITION) {


                    CardView card = (CardView) v.findViewById(R.id.cardWord);
                    // System.out.println("Elev="+card.getCardElevation());
                    card.setCardElevation(150f);
                ImageButton btnSoundItem
                        = (ImageButton) v.findViewById(R.id.btnSoundItem);
                    btnSoundItem.startAnimation(animAlpha);
                //}else{
                  //  return;
                //}
                TextView textViewName
                        = (TextView) v.findViewById(R.id.tv_number_item);
                //textViewName.setAllCaps(true);
                String selectedName = (String) textViewName.getText();
                Log.d(TAG, top + "= onScrollStateChanged=" + selectedName);

                //handler.postDelayed(new Runnable() {
                if (handler == null) return;
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Log.d(TAG, +top + "= Speech=" + selectedName);

                        playSpeech(selectedName);
                    }
                }, 1);

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
                    speedScroll = 4000;
                } else {
                    speedScroll = 2000;
                }

                if (handler == null) return;
                handler.postDelayed(new Runnable() {
                    public void run() {
                        layoutManager.scrollToPositionWithOffset(top + 1, 0);
                        Log.d(TAG, "NewTop " + String.valueOf(top + 1));
                        card.setCardElevation(17.5f);

                    }
                }, speedScroll - 1000); //3000 & 1000
                if (handler == null) return;
                handler.postDelayed(this, speedScroll); //4000
                Log.d(TAG, "Act=" + getClass().getName().trim());
            }
        });
        thread.start();
        Log.d(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

    }
    ////////////////////////////////////////////////////////////////
    public void startRepeating(View view) {
         mToastRunnable.run();
    }

    public void StopRepeating(View view) {
        mHandler.removeCallbacks(mToastRunnable);
    }

    private Runnable mToastRunnable = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(SoundActivity.this, "This is delayed toast", Toast.LENGTH_SHORT).show();
            mHandler.postDelayed(this, 5000);
        }
    };
/////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onItemClick(int position) {
        if (handler != null) return;
        //animAlpha= AnimationUtils.loadAnimation(this, R.anim.alpha);
        int top = position;
        Log.d(TAG, "position0=" + top);
        handler = new Handler();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Log.d(TAG, "position=" + top);
                View v = layoutManager.findViewByPosition(top);
                //v.startAnimation(animAlpha);
                CardView card = (CardView) v.findViewById(R.id.cardWord);
                card.setCardElevation(100f);
                TextView textViewName
                        = (TextView) v.findViewById(R.id.tv_number_item);

                String selectedName = (String) textViewName.getText();
                Log.d(TAG, top + "= onScrollStateChanged=" + selectedName);

                //handler.postDelayed(new Runnable() {
                if (handler == null) return;
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Log.d(TAG, +top + "= Speech=" + selectedName);

                        playSpeech(selectedName);
                    }
                }, 1);

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
                }
                if (handler == null) return;
                handler.postDelayed(new Runnable() {
                    public void run() {

                        card.setCardElevation(17.5f);
                        if (runnable != null) {
                            handler.removeCallbacks(runnable);
                        }
                        handler = null;
                    }
                }, 2000);
            }

        });
        thread.start();
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//        MenuItem menuSoundTraining = menu.findItem(R.id.soundTraining);
//        if(menuSoundTraining != null){
//            menuSoundTraining.setEnabled(false);
//            menuSoundTraining.getIcon().setAlpha(130);
//        }
//        MenuItem menuSoundTraining2 = menu.findItem(R.id.soundTraining2);
//        if(menuSoundTraining2 != null){
//            menuSoundTraining2.setEnabled(false);
//            menuSoundTraining2.getIcon().setAlpha(130);
//        }
//
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