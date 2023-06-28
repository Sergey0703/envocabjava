package com.step.envocab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class GroupActivity extends BaseActivity implements GroupRosterInterface{
    EditText groupFilter;
    private List<Dbgroups> listSearchGroups;
    private Dbgroups searchWord;
    LinearLayoutManager layoutManager;
    private RecyclerView searchGroupsRecycler;
    private GroupsRosterAdapter groupsRosterAdapter;
    TextView textCautionGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

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
        textCautionGroup=findViewById(R.id.caution_group);
        layoutManager = new LinearLayoutManager(this);
        searchGroupsRecycler = findViewById(R.id.group_recycler_filter);
        searchGroupsRecycler.setLayoutManager(layoutManager);


    }

    public void onResume() {
        super.onResume();
        dataToSearchList("");
    }

    public void dataToSearchList(String findStr) {
        String str = findStr + '%';
        //handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                listSearchGroups = AppDatabase.getInstance(getApplicationContext())
                        .groupDao()
                        .getGroupsFiltered(str);
                Log.d("DICT", "dataToSearchList=" + str);
                Log.d("DICT", "listSearchGroups=" + listSearchGroups.size());


            }
        });
        thread.start();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            //handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                //Log.d("DICT","listSearchWords2="+ listSearchWords);
                if (listSearchGroups != null && listSearchGroups.size() != 0) {

                    Log.d("DICT", "listSearchGroups=" + listSearchGroups.size());
                    textCautionGroup.setVisibility(View.GONE);
                    searchGroupsRecycler.setVisibility(View.VISIBLE);
                    //                    LinearLayoutManager manager = new LinearLayoutManager(getParent());
//                     wordsList.setLayoutManager(manager);
//                     //listWords.add();

                    searchGroupsRecycler.setHasFixedSize(true);
                    groupsRosterAdapter = new GroupsRosterAdapter(listSearchGroups, GroupActivity.this);
                    searchGroupsRecycler.setAdapter(groupsRosterAdapter);

                } else {
                    Log.d("Group3", "listSearchWords3=NULLLLL");
                    searchGroupsRecycler.setVisibility(View.GONE);
                    textCautionGroup.setVisibility(View.VISIBLE);
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

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void sendData(String id, String group, String description, Boolean native1) {

    }
}