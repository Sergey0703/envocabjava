package com.step.envocab;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import java.util.Locale;

public class SoundActivity extends BaseActivity implements WordListInterface {

    private Handler handler = null;
    private Handler ihandler = null;
    private Handler mHandler = new Handler();
    private Runnable runnable;
    private Context context;
    private static final String TAG = "SoundActivity";
    boolean isLoading = false;
    private RecyclerView wordsList;
    private WordsAdapter wordsAdapter;
    private List<Dbwords> listWords, listWordsForAdd;
    LinearLayoutManager layoutManager;
    Button btnPlaySound;
    Button btnPrevDay;
    Button btnNextDay;


    TextToSpeech textToSpeech;
    TextToSpeech textToSpeechTr;
    boolean playSoundOn;
    Switch speechTranslate;
    Switch speechCategory;
    Switch allStudyWords;
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
        setContentView(R.layout.activity_sound);
        today = LocalDate.now();
        dateList = today;

        layoutManager = new LinearLayoutManager(this);
        wordsList = findViewById(R.id.rv_words);
        wordsList.setLayoutManager(layoutManager);
        btnPlaySound = findViewById(R.id.buttonPlaySound);
        speechTranslate = (Switch) findViewById(R.id.speechTranslate);
        speechCategory = (Switch) findViewById(R.id.speechCategory);
        btnPrevDay = findViewById(R.id.btnPrevDay);
        btnNextDay = findViewById(R.id.btnNextDay);
        allStudyWords = findViewById(R.id.allStudyWords);
        textCaution=findViewById(R.id.caution);

        animAlpha= AnimationUtils.loadAnimation(this, R.anim.alpha);

        allStudyWords.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                onStop();
//                if (allStudyWords.isChecked()) {
//                   // allStudyWords.setText("Words only by date");
//                    Log.d(TAG, "Words only by date");
//                } else {
//                  //  allStudyWords.setText("Only studying words");
//                    Log.d(TAG, "All words for study");
//                }
                dataToList("");
            }
        });
        speechCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //System.out.println("Switch!!!!!!");
                onStop();
                if (speechCategory.isChecked()) {
                    speechCategory.setText("On the date");
                    Log.d(TAG, "Words by date");
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
                dataToList("next");
            }
        });
        btnPlaySound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listWords.size()==0) return;
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
                    ViewCompat.setBackgroundTintList(btnPlaySound, ContextCompat.getColorStateList(getApplicationContext(), R.color.purple_500));
                }
            }
        });

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
        textToSpeechTr = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if (i != TextToSpeech.ERROR) {
                    // To Choose language of speech
                    Locale locRu = new Locale("ru");
                    textToSpeechTr.setLanguage(locRu);
                }
            }
        });
        dataToList("");
    }

    public void dataToList(String nav) {
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
                if (listWords.size() < 4) {
                    listWords.addAll(listWordsForAdd);
                }
            }
        });
        thread.start();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                 if (listWords.size() != 0) {
//                     LinearLayoutManager manager = new LinearLayoutManager(getParent());
//                     wordsList.setLayoutManager(manager);
//                     //listWords.add();
                    wordsList.setHasFixedSize(true);
                    wordsAdapter = new WordsAdapter(listWords, SoundActivity.this);
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
                    card.setCardElevation(100f);
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuSoundTraining = menu.findItem(R.id.soundTraining);
        if(menuSoundTraining != null){
            menuSoundTraining.setEnabled(false);
            menuSoundTraining.getIcon().setAlpha(130);
        }
        MenuItem menuSoundTraining2 = menu.findItem(R.id.soundTraining2);
        if(menuSoundTraining2 != null){
            menuSoundTraining2.setEnabled(false);
            menuSoundTraining2.getIcon().setAlpha(130);
        }
        MenuItem menuWordsTraining = menu.findItem(R.id.wordsTraining);
        if(menuWordsTraining != null){
            menuWordsTraining.setEnabled(true);
            menuWordsTraining.getIcon().setAlpha(255);
        }
        MenuItem menuWordsTraining2 = menu.findItem(R.id.wordsTraining2);
        if(menuWordsTraining2 != null){
            menuWordsTraining2.setEnabled(true);
            menuWordsTraining2.getIcon().setAlpha(255);
        }
        return super.onPrepareOptionsMenu(menu);
    }
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