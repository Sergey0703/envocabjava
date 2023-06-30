package com.step.envocab;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.UserDictionary;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class GroupWordsActivity extends BaseActivity implements GroupWordsRosterInterface{
    private String TAG="GroupWords";
    private List<GroupWithWords> listSearchGroupWords;
    private Button addButton;
    private LinearLayoutManager layoutManager;

    private RecyclerView groupWordsRecycler;
    private GroupWordsRosterAdapter groupWordsRosterAdapter;

    private TextView textCautionGroupWords, nameGroup;
    private EditText groupWordFilter;


    String passedId="", passedName="";

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
            Bundle extras = intent.getExtras();
            passedId=extras.getString("data");
            passedName=extras.getString("passedName");
//            passedId=intent.getStringExtra("data");
//            passedName=intent.getStringExtra("passedName");
            Log.d(TAG,passedId );
            }

        layoutManager = new LinearLayoutManager(this);
        groupWordsRecycler=findViewById(R.id.group_words_recycler);
        groupWordsRecycler.setLayoutManager(layoutManager);

        textCautionGroupWords=findViewById(R.id.caution_group_words);


        nameGroup=findViewById(R.id.name_group);
        nameGroup.setText(passedName);
        addButton=findViewById(R.id.btn_add_group_word);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.d(TAG,  "= DialogN=");
                insGroupWord();
            }
        });

        groupWordFilter=findViewById(R.id.group_word_filter);
        groupWordFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            // при изменении текста выполняем фильтрацию
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String searchString = s.toString();

                if (searchString.trim().length() == 0) {


                    dataToSearchListGroupWords("");
                    return;
                } else {

                    //String searchString='%'+s.toString()+'%';
                    Log.d("Dict", searchString.toString() + " " + count);
                    dataToSearchListGroupWords(searchString);
                }
                //wordsSearchAdapter.getFilter().filter(s.toString());
            }
        });

        dataToSearchListGroupWords("");

    }
    public void insGroupWord() {
        Log.d(TAG,"ins!!!");
        Dbgroupsandwords groupWithWords = new Dbgroupsandwords();
        groupWithWords.setId_group(1);
        groupWithWords.setId(3);
        //GroupsAndWordsDao.insertGroupWithWord(groupWithWords);
        //Dbwords word = new Dbwords("NewTest2", "translate2", "transcript2");
        InsertAsyncTaskG insertAsyncTaskG = new InsertAsyncTaskG();
        insertAsyncTaskG.execute(groupWithWords);
    }


    public void dataToSearchListGroupWords(String findStr) {
//        String str = findStr + '%';
//        //handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(findStr.equals("")) {
                    listSearchGroupWords = AppDatabase.getInstance(getApplicationContext())
                            .groupsAndWordsDao()
                            .getGroupWithWords2(Integer.parseInt(passedId));
                }
//                  List<Dbwords> ll= AppDatabase.getInstance(getApplicationContext())
//                  .groupsAndWordsDao()
//                        .getGroupWithWords3(Integer.parseInt(findStr));

//                Log.d(TAG, "dataToSearchList=" + findStr);
////                Log.d(TAG, "listSearchWordsInGroup=" + listSearchGroupWords.size());
//                Log.d(TAG, "listSearchWordsInGroup3=" + ll.size());
//                for(Dbwords ww: ll){
//                    Log.d(TAG,String.valueOf(ww.getWord()));
//                }
//                for(GroupWithWords it: listSearchGroupWords ){
//                    Log.d(TAG,"Size="+String.valueOf(it.getListDbWords().size())+" gr="+it.getDbgroups().getGroup());
//                    for(Dbwords w: it.getListDbWords()){
//                        Log.d(TAG,"w="+w.getWord());
//                    }
//                }


            }
        });
        thread.start();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            //handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                //Log.d("DICT","listSearchWords2="+ listSearchWords);
                if (listSearchGroupWords != null && listSearchGroupWords.size() != 0) {

                    List<Dbwords> db= listSearchGroupWords.get(0).getListDbWords();
                    Log.d(TAG, "listSearchWords3=" + listSearchGroupWords.size());
                    textCautionGroupWords.setVisibility(View.GONE);
                    groupWordsRecycler.setVisibility(View.VISIBLE);


                    groupWordsRecycler.setHasFixedSize(true);
                    groupWordsRosterAdapter = new GroupWordsRosterAdapter(db, GroupWordsActivity.this);
                    groupWordsRecycler.setAdapter(groupWordsRosterAdapter);

                } else {
                    Log.d(TAG, "listSearchWords3=NULLLLL");
                    groupWordsRecycler.setVisibility(View.GONE);
                    textCautionGroupWords.setVisibility(View.VISIBLE);

                }

            }

        }, 100);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void sendData(String id, String word, String translate, String transcript, Boolean train1) {

    }

    /////////////////////////////////////////////////////////////////////////////////////
    class InsertAsyncTaskG extends AsyncTask<Dbgroupsandwords, Void, Void> {

        @Override
        protected Void doInBackground(Dbgroupsandwords... groupsandwords) {
            AppDatabase.getInstance(getApplicationContext())
                    .groupsAndWordsDao()
                    .insertGroupWithWord(groupsandwords[0]);
            return null;
        }
    }

}
