package com.step.envocab;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import java.util.List;

public class GroupWordsActivity extends BaseActivity{
    private String TAG="GroupWords";
    private List listSearchGroupWords;
    String passedId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_words);

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

        Intent intent =getIntent();
        if(intent.getExtras()!=null){
            passedId=intent.getStringExtra("data");
            Log.d(TAG,passedId );
            }

        dataToSearchListGroupWords(passedId);

    }

    public void dataToSearchListGroupWords(String findStr) {
//        String str = findStr + '%';
//        //handler = new Handler();
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                listSearchGroupWords = AppDatabase.getInstance(getApplicationContext())
//                        .groupWordDao()
//                        .getItemsFiltered(str);
//                Log.d("DICT", "dataToSearchList=" + str);
//                Log.d("DICT", "listSearchWords=" + listSearchGroupWords.size());
//
//
//            }
//        });
//        thread.start();
//
//        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//            //handler.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//
//                //Log.d("DICT","listSearchWords2="+ listSearchWords);
//                if (listSearchGroupWords != null && listSearchGroupWords.size() != 0) {
//
//                    Log.d("DICT", "listSearchWords3=" + listSearchGroupWords.size());
//                    textCautionDict.setVisibility(View.GONE);
//                    searchRecycler.setVisibility(View.VISIBLE);
//
//
//                    searchRecycler.setHasFixedSize(true);
//                    groupWordsRosterAdapter = new GroupsWordsRosterAdapter(listSearchGroupWords, GroupWordsActivity.this);
//                    searchRecycler.setAdapter(groupWordsRosterAdapter);
//
//                } else {
//                    Log.d("DICT3", "listSearchWords3=NULLLLL");
//                    searchRecycler.setVisibility(View.GONE);
//                    textCautionDict.setVisibility(View.VISIBLE);
//
//                }
//
//            }
//
//        }, 100);
    }

    /////////////////////////////////////////////////////////////////////////////////////

}