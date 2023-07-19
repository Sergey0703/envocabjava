package com.step.envocab;

import android.content.Context;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBar;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

import java.util.List;
import java.util.Locale;
//my first Test version
//public class MainActivity extends AppCompatActivity {
public class MainActivity extends BaseActivity {
    private int count, countB;
    private int id_exercise = 6;
    private SwitchCompat onlyMarkedWords;
    private ArrayAdapter<String> adapter;
    private TextInputLayout textSpinner2;
    private AutoCompleteTextView spinner2;
    private List<String> listGroups;
    private Integer filterWord=null;
    private int id_group=0;
    private static final String TAG = "MainActivity";
    //final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
    //DataBaseHelper databaseHelper;
    DatabaseHelper db;
    Dbwords word;
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
    TextView dashWordsInVocabCount;
    TextView dashWordsTodayCount;
    TextView dashWordsTodayBadCount;

    private Long trainDateLong;
    Switch switchSound;
    //   ImageView menuIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Log.d(TAG,themeApp);
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        Boolean s1 = sh.getBoolean("themeApp", true);
//        String fileNameString = sharedPreferencesBinding.fileNameEditView.getText().toString();
//        SharedPreferences sharedPreferences;
//        if(fileNameString.isEmpty()) {
//            sharedPreferences = getPreferences(MODE_PRIVATE);
//        }
//        else {
//            sharedPreferences = getSharedPreferences(fileNameString, MODE_PRIVATE);
//        }
        if(s1) {
            setContentView(R.layout.activity_main2);
        }else{
            setContentView(R.layout.activity_main);
            }

        Toolbar toolbar = (Toolbar) findViewById(R.id.action_toolbar);
        setSupportActionBar(toolbar);


        switchSound = (Switch) findViewById(R.id.switchSound);

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
        //databaseHelper = new DataBaseHelper(getApplicationContext());
        // создаем базу данных
        //databaseHelper.createDataBase();

//        DatabaseHelper databaseHelper = new DatabaseHelper(this);
//        databaseHelper = new DatabaseHelper(this);
//
//        try {
//            databaseHelper.createDataBase();
//        } catch (IOException ioe) {
//            throw new Error("Unable to create database");
//        }

//        db = new DatabaseHelper(this);
//        db = new DatabaseHelper(this);
//        try {
//            db.createDataBase();
//            db.openDataBase();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }

//        try {
//            databaseHelper.openDataBase();
//        }catch(SQLException sqle){
//            throw sqle;
//        }

        //Log.d("log","My log!");
        btnWordOk = findViewById(R.id.btnWordOk);
        btnWordStudy = findViewById(R.id.btnWordStudy);
        btnWordTranslate = findViewById(R.id.btnWordTranslate);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        btnSound = findViewById(R.id.btnSound);
        dashWord = findViewById(R.id.dashWord);
        dashTranscript = findViewById(R.id.dashTranscript);
        dashTrainDate = findViewById(R.id.dashTrainDate);
        translate = findViewById(R.id.dashTranslate);
        translate.setVisibility(View.INVISIBLE);
        dashWordsInVocabCount = findViewById(R.id.dashWordsInVocabCount);
        dashWordsTodayCount = findViewById(R.id.dashWordsTodayCount);
        dashWordsTodayBadCount = findViewById(R.id.dashWordsTodayBadCount);
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);

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

        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("testLogs", "Speech");
                view.startAnimation(animAlpha);
                playSpeech();

            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                takeWord("Next");
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                takeWord("Prev");
                //insWord();
            }
        });

        btnWordOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                updateWord(true);
            }
        });

        btnWordStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                updateWord(false);
            }
        });
        btnWordTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                translate.setVisibility(translate.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                btnWordTranslate.setText(translate.getVisibility() == View.VISIBLE ? "HIDE TRANSLATE" : "SHOW TRANSLATE");

            }
        });

        onlyMarkedWords = findViewById(R.id.only_marked_words);

        spinner2 = findViewById(R.id.spinner_tr2);
        textSpinner2=findViewById(R.id.text_spinner2);

        checkLastGroup();

        spinner2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Object item = parent.getItemAtPosition(position);
//                if (item instanceof StudentInfo){
//                    StudentInfo student=(StudentInfo) item;
//                    doSomethingWith(student);
//                }
                String item2 = (String)parent.getItemAtPosition(position);
                id_group = (int)parent.getItemIdAtPosition(position);
                onlyMarkedWords.setChecked(false);
                filterWord=null;
//                if(id_group==null){
//                    id_group=0L;
//                }
                Log.d(TAG, "item2="+item2+" id_item="+String.valueOf(id_group));
                takeWord("");
            }
        });
        onlyMarkedWords.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //onStop();
                if (onlyMarkedWords.isChecked()) {
                    filterWord=1;
                    Log.d(TAG, "Only words for study");
                } else {
                    filterWord=null;
                    Log.d(TAG, "All words ");
                }
                takeWord("");
            }
        });

        makeSpin();
       // takeWord("");
    }
    public void makeSpin(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                listGroups = AppDatabase.getInstance(getApplicationContext())
                        .groupDao()
                        .getGroupsForSpinner();
                listGroups.add(0,"Without groups");
            }
        });
        thread.start();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                //String[] countries = { "Бразилия", "Аргентина", "Колумбия", "Чили", "Уругвай"};
                adapter = new ArrayAdapter(MainActivity.this, R.layout.spinner_item_tr, listGroups);
                //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Применяем адаптер к элементу spinner
                spinner2.setAdapter(adapter);
//                if(id_group==null){
//                    id_group=0L;
//                }
                //int id_gr=id_group.intValue();
                //spinner2.setSelection(2);
                spinner2.setText(spinner2.getAdapter().getItem(id_group).toString(), false);
                textSpinner2.setHint("Select Group");

            }
        }, 10);
        takeWord("");
    }

    public void checkLastGroup() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                id_group= AppDatabase.getInstance(getApplicationContext())
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

    //    public void showMenu(View v){
//        PopupMenu popupMenu=new PopupMenu(MainActivity.this,v);
//        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                if(item.getItemId()==R.id.aboutProgram) {
//                    //Log.d(TAG,"menu1");
//                    AboutDialog aboutDialog = new AboutDialog();
//                    aboutDialog.show(getSupportFragmentManager(),"example dialog");
//
//                }else if(item.getItemId()==R.id.soundTraining) {
//
//                    Intent intent=new Intent(MainActivity.this, SoundActivity.class);
//                    startActivity(intent);
//                    //setContentView(R.layout.activity_sound);
//                    //Log.d(TAG,"menu1");
//                    //AboutDialog aboutDialog = new AboutDialog();
//                    //aboutDialog.show(getSupportFragmentManager(),"example dialog");
//
//                }
//                return true;
//            }
//        });
//        popupMenu.show();
//    }
    public void playSpeech() {
        //final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        //btnSound.startAnimation(animAlpha);
        textToSpeech.speak((String) dashWord.getText(), TextToSpeech.QUEUE_FLUSH, null);
    }

    //   public void insWord() {
        //System.out.println("Ins");
//        Dbwords word = new Dbwords("NewTest2", "translate2", "transcript2");
//        InsertAsyncTask insertAsyncTask = new InsertAsyncTask();
//        insertAsyncTask.execute(word);
 //   }

    public void updateWord(Boolean up) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                 word = AppDatabase.getInstance(getApplicationContext())
                        .wordDao()
                        .findById(uid);

                if (word != null) {
                    currentTime = Calendar.getInstance().getTime();
                    Log.d(TAG, "currentTime=" + currentTime);
                    if (id_group == 0){
                        Log.d(TAG, "findBy=" + uid + " word=" + word.getWord());

                    word.setTrain1(up);
                    word.setTrainDate(currentTime);
                    AppDatabase.getInstance(getApplicationContext())
                            .wordDao()
                            .updateWord(word);
                        Log.d(TAG, "Update word=" + word.getWord());
                }else {
                        AppDatabase.getInstance(getApplicationContext())
                                .countDao()
                                .insertOrUpdate(id_exercise, word.getId(), id_group, up, currentTime);
                        Log.d(TAG, "Update Group word=" + word.getWord());
                }

                takeWord("");
                }
            }

        }).start();

    }

//    public void allWords() {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                List<Dbwords> wordList = AppDatabase.getInstance(getApplicationContext())
//                        .wordDao()
//                        .getAll();
//                //Log.d(TAG, "run "+wordList.toString());
//                for (Dbwords w : wordList) {
//                    Log.d(TAG, w.toString());
//                }
//
//            }
//        });
//        thread.start();
//    }

    public void takeWord(String nav) {
        count=0;
        countB=0;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
             //   word = null;
//                List<Dbgroups> listGroup = AppDatabase.getInstance(getApplicationContext())
//                        .groupDao()
//                        .findUseGroup();

                Log.d(TAG, "trainDateLong=" + trainDateLong);
                if (nav == "") {
                    //if(listGroup.size()>0) {
                    if(id_group>0) {
//                        word = AppDatabase.getInstance(getApplicationContext())
//                                .groupsAndWordsDao()
//                                .getWords7();
                        List<Dbwords> listWords = AppDatabase.getInstance(getApplicationContext())
                                .wordDao()
                                .getWordsTrain2(id_exercise,  id_group, 1, null, false);
                        if(listWords.size()>0){
                            word=listWords.get(0);
                        }
                        Log.d(TAG, "Sound " + id_group + " size=" + listWords.size() + " id_exercise=" + id_exercise);
                    }else{
                        word = AppDatabase.getInstance(getApplicationContext())
                                .wordDao()
                                .findLast();

                    }
                } else if (nav == "Next") {
                    if(id_group==0) {
                        word = AppDatabase.getInstance(getApplicationContext())
                                .wordDao()
                                .findPrev(trainDateLong);
                        if (word == null) {
                            word = AppDatabase.getInstance(getApplicationContext())
                                    .wordDao()
                                    .findPrevAdd();
                            Log.d(TAG, "nav=");
                        }
                    }else {
                        Date currentTimeW=word.getTrainDate();
                        Log.d(TAG,"currentTimeW="+currentTimeW );
                        if(currentTimeW==null){
                            currentTimeW=Calendar.getInstance().getTime();
                        }
                        long trainDateLong = Converters.dateToTimestamp(currentTimeW);
                        Log.d(TAG,"curr="+currentTimeW+" filterWord="+filterWord );
                        List <Dbwords> listWords = AppDatabase.getInstance(getApplicationContext())
                                .wordDao()
                                .getWordsTrainNext(id_exercise,  id_group,trainDateLong ,1,0, filterWord, false);
                        if(listWords.size()>0){
                            word=listWords.get(0);
                        }

                    }
                } else if (nav == "Prev") {
                    if(id_group==0) {
                        word = AppDatabase.getInstance(getApplicationContext())
                                .wordDao()
                                .findPrev(trainDateLong);
                        if (word == null) {
                            word = AppDatabase.getInstance(getApplicationContext())
                                    .wordDao()
                                    .findPrevAdd();
                            Log.d(TAG, "nav=");
                        }
                    }else{
                        //Date currentTime = Calendar.getInstance().getTime();
                        Date currentTimeW=word.getTrainDate();
                        Log.d(TAG,"currentTimeW="+currentTimeW );
                        if(currentTimeW==null){
                            currentTimeW=Calendar.getInstance().getTime();
                        }
                        long trainDateLong = Converters.dateToTimestamp(currentTimeW);
                        Log.d(TAG,"curr="+currentTimeW+" offset=" );
                        List <Dbwords> listWords = AppDatabase.getInstance(getApplicationContext())
                                .wordDao()
                                .getWordsTrainPrev(id_exercise,  id_group,trainDateLong ,1,0,filterWord, false);
                        if(listWords.size()>0){
                            word=listWords.get(0);
                        }

                    }
                    Log.d(TAG, "nav=" + nav);
                }

                if (word != null) {
                    Log.d(TAG, "Take Word=" + word.getWord());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            uid = word.getId();
                            trainDateLong = Long.valueOf(0);
                            dashWord.setText(word.getWord());
                            if (word.getTrain1() != null && word.getTrain1() == true) {
                                dashWord.setCompoundDrawablesWithIntrinsicBounds(R.drawable.green_circle, 0, 0, 0);
                            } else {
                                dashWord.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_circle, 0, 0, 0);
                            }

                            dashTranscript.setText("[" + word.getTranscript() + "]");
                            if (word.getTrainDate() != null) {
                                DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                dateWithoutTime = sdf.format(word.getTrainDate());
                                Log.d(TAG, "dateWithoutTime=" + dateWithoutTime);
                                dashTrainDate.setText(dateWithoutTime);
                                trainDateLong = Converters.dateToTimestamp(word.getTrainDate());

                            } else {
                                dashTrainDate.setText("");

                            }
                            translate.setVisibility(View.GONE);
                            btnWordTranslate.setText("SHOW TRANSLATE");
                            translate.setText(word.getTranslate());

                            if (switchSound.isChecked()) {
                                //btnSound.startAnimation(animAlpha);
                                playSpeech();
                            }

                            Log.d(TAG, "Take Word setText " + word.getWord());
                           // Log.d(TAG, "Date " + Converters.dateToTimestamp(word.getTrainDate()));
                            //countWordsToday();
                        }
                    });

                } else {
                    Log.d(TAG, "Empty word");
                }
                countWordsInVocab();

                countWordsToday(0);
                countWordsToday(1);
                countWordsTodayGroup(0);
                countWordsTodayGroup(1);

            }
        });
        thread.start();
    }

    public void countWordsToday(int typeWords) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                LocalDate today = LocalDate.now();
                LocalDateTime startOfDate = today.atStartOfDay();
                LocalDateTime endOfDate = LocalTime.MAX.atDate(today);
                //java.sql.Timestamp.valueOf(startOfDay);
                //java.sql.Date.valueOf(startOfDay);
                //Long st=Converters.dateToTimestamp(startOfDay);
                ZonedDateTime zdtStart = ZonedDateTime.of(startOfDate, ZoneId.systemDefault());
                ZonedDateTime zdtEnd = ZonedDateTime.of(endOfDate, ZoneId.systemDefault());
                Long startOfDay = zdtStart.toInstant().toEpochMilli();
                Long endOfDay = zdtEnd.toInstant().toEpochMilli();

//                Date dateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy HH:mm:ss")
//                LocalDate today2 = LocalDate.now();   // your current date time
//                LocalDateTime start2= today2.atStartOfDay(); // date time at start of the date
                //Long start2.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(); // start time to timestamp
                Log.d(TAG, "start date =" + startOfDay);
                Log.d(TAG, "End date2 =" + endOfDay);

                // Log.d("Date:", "start date parsed "+startOfDay.format(dateFormatter)}")

                //word.getTrainDate()

                     int count1 = AppDatabase.getInstance(getApplicationContext())
                            .wordDao()
                            .countToday(startOfDay, endOfDay, typeWords);
                     if(typeWords==0){
                         countB=countB+count1;
                     }else{
                         count=count+count1;
                     }
                    Log.d(TAG, "countToday=" + count);



                //if (count != 0) {
                // dashWordsTodayCount.setText(count);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (typeWords == 0) {
                            dashWordsTodayBadCount.setText(String.valueOf(countB));
                        } else {
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

    public void countWordsTodayGroup(int typeWords) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                LocalDate today = LocalDate.now();
                LocalDateTime startOfDate = today.atStartOfDay();
                LocalDateTime endOfDate = LocalTime.MAX.atDate(today);
                //java.sql.Timestamp.valueOf(startOfDay);
                //java.sql.Date.valueOf(startOfDay);
                //Long st=Converters.dateToTimestamp(startOfDay);
                ZonedDateTime zdtStart = ZonedDateTime.of(startOfDate, ZoneId.systemDefault());
                ZonedDateTime zdtEnd = ZonedDateTime.of(endOfDate, ZoneId.systemDefault());
                Long startOfDay = zdtStart.toInstant().toEpochMilli();
                Long endOfDay = zdtEnd.toInstant().toEpochMilli();

//                Date dateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy HH:mm:ss")
//                LocalDate today2 = LocalDate.now();   // your current date time
//                LocalDateTime start2= today2.atStartOfDay(); // date time at start of the date
                //Long start2.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(); // start time to timestamp
                Log.d(TAG, "start date =" + startOfDay);
                Log.d(TAG, "End date2 =" + endOfDay);

                // Log.d("Date:", "start date parsed "+startOfDay.format(dateFormatter)}")

                //word.getTrainDate()

                    int count1 = AppDatabase.getInstance(getApplicationContext())
                            .wordDao()
                            .countTodayGroup(startOfDay, endOfDay, typeWords, id_exercise);
                    Log.d(TAG, "countTodayGr=" + count);
                if(typeWords==0){
                    countB=countB+count1;
                }else{
                    count=count+count1;
                }


                //if (count != 0) {
                // dashWordsTodayCount.setText(count);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (typeWords == 0) {
                            dashWordsTodayBadCount.setText(String.valueOf(countB));
                        } else {
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


    public void countWordsInVocab() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                int count = AppDatabase.getInstance(getApplicationContext())
                        .wordDao()
                        .countAll();

                Log.d(TAG, "countAll=" + count);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                         dashWordsInVocabCount.setText(String.valueOf(count));
                   }
                });
            }
        });
        thread.start();
    }

//    class InsertAsyncTask extends AsyncTask<Dbwords, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Dbwords... words) {
//            AppDatabase.getInstance(getApplicationContext())
//                    .wordDao()
//                    .insertWord(words[0]);
//            return null;
//        }
//    }

    //@Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//        MenuItem menuWordsTraining = menu.findItem(R.id.wordsTraining);
//        if(menuWordsTraining != null){
//            menuWordsTraining.setEnabled(false);
//            menuWordsTraining.getIcon().setAlpha(130);
//            //Log.d("MENU","Menu!!!!!!!!!!!!!!!!!!!!!!!2000000000000");
//        }
//        MenuItem menuWordsTraining2 = menu.findItem(R.id.wordsTraining2);
//        if(menuWordsTraining2 != null){
//            menuWordsTraining2.setEnabled(false);
//            menuWordsTraining2.getIcon().setAlpha(130);
//        }
//
//        return true;
//    }
}