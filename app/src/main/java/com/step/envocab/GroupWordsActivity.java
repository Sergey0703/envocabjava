package com.step.envocab;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class GroupWordsActivity extends BaseActivity implements GroupWordsRosterInterface{
    private Handler handler = null;
    private String TAG="GroupWords";
    private List<GroupWithWords2> listSearchGroupWords;
    private Button addButton;
    private LinearLayoutManager layoutManager;

    private RecyclerView groupWordsRecycler;
    private GroupWordsRosterAdapter groupWordsRosterAdapter;

    private TextView textCautionGroupWords, nameGroup;
    private EditText groupWordFilter;
    private int layoutIdForListItem;
    private String theme;


    String passedId="", passedName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        Boolean s1 = sh.getBoolean("themeApp", true);

        if(s1) {
            setContentView(R.layout.activity_group_words2);
            layoutIdForListItem=R.layout.group_word_roster_item2;
            theme="light";
        }else{
            setContentView(R.layout.activity_group_words);
            layoutIdForListItem=R.layout.group_word_roster_item;
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
        nameGroup.setText("Group: "+passedName);
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
//        Log.d(TAG,"ins!!!");
//        Dbgroupsandwords groupWithWords = new Dbgroupsandwords();
//        groupWithWords.setId_group(2);
//        groupWithWords.setId(1);
//        //GroupsAndWordsDao.insertGroupWithWord(groupWithWords);
//        //Dbwords word = new Dbwords("NewTest2", "translate2", "transcript2");
//        InsertAsyncTaskG insertAsyncTaskG = new InsertAsyncTaskG();
//        insertAsyncTaskG.execute(groupWithWords);
    }


    public void dataToSearchListGroupWords(String findStr) {
        String str = findStr + '%';
//        //handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(findStr.equals("")) {
                    listSearchGroupWords = AppDatabase.getInstance(getApplicationContext())
                            .groupsAndWordsDao()
                            .getGroupWithWords5(Integer.parseInt(passedId));
                }else{
                    Log.d(TAG,"str="+str);
                    listSearchGroupWords = AppDatabase.getInstance(getApplicationContext())
                            .groupsAndWordsDao()
                            .getGroupWithWords6(Integer.parseInt(passedId),str);

                }

//                  List<GroupWithWords2> ll= AppDatabase.getInstance(getApplicationContext())
//                  .groupsAndWordsDao()
//                      //  .getGroupWithWords6(Integer.parseInt(passedId));
//                        .getGroupWithWords6();

//                Log.d(TAG, "dataToSearchList=" + passedId);
//////                Log.d(TAG, "listSearchWordsInGroup=" + listSearchGroupWords.size());
//                Log.d(TAG, "listSearchWordsInGroup3=" + ll.size());
//                for(GroupWithWords2 ww: ll){
//                    Log.d(TAG,String.valueOf(ww.getId_group())+" word="+ww.getWord()+" "+ww.getTranslate());
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

                    //List<Dbwords> db= listSearchGroupWords.get(0).getListDbWords();
                   // List<GroupWithWords2> db= listSearchGroupWords.get(0).getListDbWords();
                    Log.d(TAG, "listSearchWords3=" + listSearchGroupWords.size());
                    textCautionGroupWords.setVisibility(View.GONE);
                    groupWordsRecycler.setVisibility(View.VISIBLE);


                    groupWordsRecycler.setHasFixedSize(true);
                    groupWordsRosterAdapter = new GroupWordsRosterAdapter(listSearchGroupWords, GroupWordsActivity.this, layoutIdForListItem);
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
    public void onItemClick(int position, String funct) {
        int top = position;
        Log.d(TAG, "position=" + top+" funct="+funct);
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
                if(funct.equals("add")) {
                    Dbgroupsandwords dbgroupsandwords=new Dbgroupsandwords();
                    dbgroupsandwords.setId(Integer.parseInt(id));
                    dbgroupsandwords.setId_group(Integer.parseInt(passedId));
                    Long idns = AppDatabase.getInstance(getApplicationContext())
                            .groupsAndWordsDao()
                            .insertGroupWithWord(dbgroupsandwords);
                }else{
                    int idns = AppDatabase.getInstance(getApplicationContext())
                            .groupsAndWordsDao()
                            .del(Integer.parseInt(id), Integer.parseInt(passedId));
                }

                if (handler == null) return;
                handler.postDelayed(new Runnable() {
                    public void run() {
                        dataToSearchListGroupWords(groupWordFilter.getText().toString());
                    }
                }, 200);
//                searchWord = AppDatabase.getInstance(getApplicationContext())
//                        .wordDao()
//                        .findById(Integer.parseInt(id));
//                Log.d("DICT", "idToSearchList=" + id);
//                Log.d("DICT", "searchWord=" + searchWord.getWord());
//                // ViewDialog alert = new ViewDialog();
//                // alert.showDialog(DictActivity.this, "Window");


            }
        });
        thread.start();

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
