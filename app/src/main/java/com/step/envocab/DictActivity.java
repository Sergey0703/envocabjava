package com.step.envocab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

public class DictActivity extends BaseActivity implements WordRosterInterface {
    private Runnable runnable;
    private String theme="light";

    private WordDialog dialog;
    private Context context;
    EditText wordFilter;
    private List<Dbwords> listSearchWords;
    private Dbwords searchWord;
    LinearLayoutManager layoutManager;
    private RecyclerView searchRecycler;
    private WordsRosterAdapter wordsRosterAdapter;
    TextView textCautionDict;
    private int layoutIdForListItem;

    private String TAG = "DictActivity";
    private Handler handler = null;

    private Button btnSaveD, btnNew;

    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        if (handler != null) {
            if (runnable != null) {
                handler.removeCallbacks(runnable);
            }
            handler = null;


        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        Boolean s1 = sh.getBoolean("themeApp", true);

        if(s1) {
            setContentView(R.layout.activity_dict2);
            layoutIdForListItem=R.layout.word_dict_roster_item2;
            theme="light";
        }else{
            setContentView(R.layout.activity_dict);
            layoutIdForListItem=R.layout.word_roster_item;
            theme="dark";
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.action_toolbar);
        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStop();
                finish();
            }
        });

        wordFilter = findViewById(R.id.wordFilter);

        layoutManager = new LinearLayoutManager(this);
        searchRecycler = findViewById(R.id.recyclerFilter);
        searchRecycler.setLayoutManager(layoutManager);

        textCautionDict=findViewById(R.id.caution_dict);

        btnNew = (Button) findViewById(R.id.btn_new);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int width = displaymetrics.widthPixels * 3 / 4;
                int height = displaymetrics.heightPixels * 3 / 4;

                Log.d(TAG,  "= DialogN=" + width);
                dialog = new WordDialog(DictActivity.this,  DictActivity.this);
                dialog.showDialog(DictActivity.this, width, height, theme, "New word","new",null
                        ,null ,null
                        ,null);
            }
        });

        //dataToSearchList();

    }

    public void onResume() {
        super.onResume();
        wordFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            // при изменении текста выполняем фильтрацию
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String searchString = s.toString();

                if (searchString.trim().length() == 0) {

                    dataToSearchList(searchString);
                   // searchRecycler.setVisibility(View.GONE)
                    return;
                } else {

                    //String searchString='%'+s.toString()+'%';
                    Log.d("Dict", searchString.toString() + " " + count);
                    dataToSearchList(searchString);
                }
                //wordsSearchAdapter.getFilter().filter(s.toString());
            }
        });
        dataToSearchList("");
    }


    public void dataToSearchList(String findStr) {
        String str = findStr + '%';
        //handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                listSearchWords = AppDatabase.getInstance(getApplicationContext())
                        .wordDao()
                        .getItemsFiltered(str);
                Log.d("DICT", "dataToSearchList=" + str);
                Log.d("DICT", "listSearchWords=" + listSearchWords.size());


            }
        });
        thread.start();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
        //handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                //Log.d("DICT","listSearchWords2="+ listSearchWords);
                if (listSearchWords != null && listSearchWords.size() != 0) {

                    Log.d("DICT", "listSearchWords3=" + listSearchWords.size());
                    textCautionDict.setVisibility(View.GONE);
                    searchRecycler.setVisibility(View.VISIBLE);
                    //                    LinearLayoutManager manager = new LinearLayoutManager(getParent());
//                     wordsList.setLayoutManager(manager);
//                     //listWords.add();

                    searchRecycler.setHasFixedSize(true);
                    wordsRosterAdapter = new WordsRosterAdapter(listSearchWords, DictActivity.this,layoutIdForListItem);
                    searchRecycler.setAdapter(wordsRosterAdapter);

                } else {
                    Log.d("DICT3", "listSearchWords3=NULLLLL");
                    searchRecycler.setVisibility(View.GONE);
                    textCautionDict.setVisibility(View.VISIBLE);
//                    int size = listSearchWords.size();
//                    listSearchWords.clear();
//                    wordsRosterAdapter = new WordsRosterAdapter(listSearchWords, DictActivity.this);
//                    searchRecycler.setAdapter(wordsRosterAdapter);
//                    searchRecycler.setHasFixedSize(true);
//                    wordsSearchAdapter = new WordsAdapter(null, DictActivity.this);
//                    searchRecycler.setAdapter(wordsSearchAdapter);
                }

            }

        }, 100);
    }

    /////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onItemClick(int position) {
        int top = position;
//        if (handler != null) return;
//        //animAlpha= AnimationUtils.loadAnimation(this, R.anim.alpha);
//
//        Log.d(TAG, "position0=" + top);
//        handler = new Handler();
//        Thread thread = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
        Log.d(TAG, "position=" + top);
        View v = layoutManager.findViewByPosition(top);
        //v.startAnimation(animAlpha);
        CardView card = (CardView) v.findViewById(R.id.cardWord);
//        card.setCardElevation(100f);
        TextView textViewName
                = (TextView) v.findViewById(R.id.tv_number_item);

        String selectedName = (String) textViewName.getText();
        TextView textViewId
                = (TextView) v.findViewById(R.id.id_item);

        String id = (String) textViewId.getText();
        Log.d(TAG, top + "= onScrollStateChanged=" + selectedName + " id=" + id);

        handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                searchWord = AppDatabase.getInstance(getApplicationContext())
                        .wordDao()
                        .findById(Integer.parseInt(id));
                Log.d("DICT", "idToSearchList=" + id);
                Log.d("DICT", "searchWord=" + searchWord.getWord());
                // ViewDialog alert = new ViewDialog();
                // alert.showDialog(DictActivity.this, "Window");


            }
        });
        thread.start();

        if (handler == null) return;


        handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int width = displaymetrics.widthPixels * 3 / 4;
                int height = displaymetrics.heightPixels * 3 / 4;

                Log.d(TAG, +top + "= Dialog=" + width);
                dialog = new WordDialog(DictActivity.this,  DictActivity.this);
                dialog.showDialog(DictActivity.this, width, height,theme,"Edit word", String.valueOf(searchWord.getId()),
                        searchWord.getWord(), searchWord.getTranslate(),
                        searchWord.getTranscript(),searchWord.getTrain1());
                }
        },100);
    }

    @Override
    public void sendData(String id, String word, String translate, String transcript, Boolean train1) {
        Log.d(TAG,"SendData!!!! id="+id+" translate="+translate);
         wordFilter.setText(word);
                handler = new Handler();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"word="+train1);
                        if(id.equals("new")){
                           // wordFilter.setText(word);
                            Long idup = AppDatabase.getInstance(getApplicationContext())
                                    .wordDao()
                                    .insWord( word, translate, transcript, null);

                            Log.d("DialogAdd", String.valueOf(idup));
                        }else {
                            int idup = AppDatabase.getInstance(getApplicationContext())
                                    .wordDao()
                                    .upWord(Integer.parseInt(id), word, translate, transcript, null);

                            Log.d("Dialog", String.valueOf(idup));
                        }
                    }
                });
                thread.start();
        //dataToSearchList(wordFilter.getText().toString());

                if (handler == null) return;
                handler.postDelayed(new Runnable() {
                    public void run() {
                        dataToSearchList(wordFilter.getText().toString());
                    }
                }, 200);
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//        MenuItem menuDict = menu.findItem(R.id.dict);
//        if(menuDict != null){
//            menuDict.setEnabled(false);
//            menuDict.getIcon().setAlpha(130);
//        }
//        MenuItem menuDict2 = menu.findItem(R.id.dict2);
//        if(menuDict2 != null){
//            menuDict2.setEnabled(false);
//            menuDict2.getIcon().setAlpha(130);
//        }
//
//        return true;
//    }
}
