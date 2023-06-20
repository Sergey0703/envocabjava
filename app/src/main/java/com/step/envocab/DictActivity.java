package com.step.envocab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class DictActivity extends BaseActivity implements WordListInterface{
    EditText wordFilter;
    private List<Dbwords> listSearchWords;
    private Dbwords searchWord;
    LinearLayoutManager layoutManager;
    private RecyclerView searchRecycler;
    private WordsRosterAdapter wordsRosterAdapter;

    private String TAG= "DictActivity";
    private Handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dict);

        Toolbar toolbar = (Toolbar) findViewById(R.id.action_toolbar);
        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        wordFilter=findViewById(R.id.wordFilter);

        layoutManager = new LinearLayoutManager(this);
        searchRecycler = findViewById(R.id.recyclerFilter);
        searchRecycler.setLayoutManager(layoutManager);

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

                String searchString=s.toString();

                if(searchString.trim().length()==0) {

//                    searchRecycler.setHasFixedSize(true);
//                    int size = listSearchWords.size();
//                    listSearchWords.clear();
//                    wordsRosterAdapter = new WordsRosterAdapter(listSearchWords, DictActivity.this);
//                    searchRecycler.setAdapter(wordsRosterAdapter);
                    searchRecycler.setVisibility(View.GONE);
                    return;
                }else {
                    searchString = searchString + '%';
                    //String searchString='%'+s.toString()+'%';
                    Log.d("Dict", searchString.toString() + " " + count);
                    dataToSearchList(searchString);
                }
                //wordsSearchAdapter.getFilter().filter(s.toString());
            }
        });
    }



    public void  dataToSearchList(String findStr){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                listSearchWords = AppDatabase.getInstance(getApplicationContext())
                        .wordDao()
                        .getItemsFiltered(findStr);
                Log.d("DICT","dataToSearchList="+ findStr);
                Log.d("DICT","listSearchWords="+ listSearchWords.size());


            }
        });
        thread.start();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                //Log.d("DICT","listSearchWords2="+ listSearchWords);
                if (listSearchWords!=null && listSearchWords.size() != 0) {

                    Log.d("DICT","listSearchWords3="+ listSearchWords.size());
                    searchRecycler.setVisibility(View.VISIBLE);
                    //                    LinearLayoutManager manager = new LinearLayoutManager(getParent());
//                     wordsList.setLayoutManager(manager);
//                     //listWords.add();

                    searchRecycler.setHasFixedSize(true);
                    wordsRosterAdapter = new WordsRosterAdapter(listSearchWords, DictActivity.this);
                    searchRecycler.setAdapter(wordsRosterAdapter);

                }else{
                    Log.d("DICT3","listSearchWords3=NULLLLL");
                    searchRecycler.setVisibility(View.GONE);
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
                card.setCardElevation(100f);
                TextView textViewName
                        = (TextView) v.findViewById(R.id.tv_number_item);

                String selectedName = (String) textViewName.getText();
                TextView textViewId
                        = (TextView) v.findViewById(R.id.id_item);

                String id = (String) textViewId.getText();
                Log.d(TAG, top + "= onScrollStateChanged=" + selectedName+" id="+id);

           handler = new Handler();
           Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                searchWord = AppDatabase.getInstance(getApplicationContext())
                        .wordDao()
                        .findById(Integer.parseInt(id));
                Log.d("DICT","idToSearchList="+ id);
                Log.d("DICT","searchWord="+ searchWord.getWord());
               // ViewDialog alert = new ViewDialog();
               // alert.showDialog(DictActivity.this, "Window");


            }
        });
        thread.start();
                if (handler == null) return;
                handler.postDelayed(new Runnable() {
                    public void run() {
                        DisplayMetrics displaymetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                        int width = displaymetrics.widthPixels*3/4;
                        int height = displaymetrics.heightPixels*3/4;

                        Log.d(TAG, +top + "= Dialog="+width );
                        WordDialog alert = new WordDialog();
                        alert.showDialog(DictActivity.this, width, height, searchWord.getWord(),searchWord.getTranslate(),searchWord.getTranscript());
                        //playSpeech(selectedName);
                    }
                }, 100);
//
//
//            }
//
//        });
//        thread.start();
    }

}