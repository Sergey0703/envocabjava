package com.step.envocab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.List;

public class DictActivity extends BaseActivity implements WordListInterface{
    EditText wordFilter;
    private List<Dbwords> listSearchWords;
    LinearLayoutManager layoutManager;
    private RecyclerView searchRecycler;
    private WordsAdapter wordsSearchAdapter;

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
                    searchRecycler.setHasFixedSize(true);
                    int size = listSearchWords.size();
                    listSearchWords.clear();
                    wordsSearchAdapter = new WordsAdapter(listSearchWords, DictActivity.this);
                    searchRecycler.setAdapter(wordsSearchAdapter);
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
                    //                    LinearLayoutManager manager = new LinearLayoutManager(getParent());
//                     wordsList.setLayoutManager(manager);
//                     //listWords.add();

                    searchRecycler.setHasFixedSize(true);
                    wordsSearchAdapter = new WordsAdapter(listSearchWords, DictActivity.this);
                    searchRecycler.setAdapter(wordsSearchAdapter);

                }else{
                    Log.d("DICT","listSearchWords2=NULLLLL");
                    searchRecycler.setHasFixedSize(true);
                    wordsSearchAdapter = new WordsAdapter(null, DictActivity.this);
                    searchRecycler.setAdapter(wordsSearchAdapter);
                }
            }
        }, 100);
    }

    public void onItemClick(int position) {

    }
}