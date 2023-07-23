package com.step.envocab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class GroupActivity extends BaseActivity implements GroupRosterInterface{
    private EditText groupFilter;
    private Handler handler;
    private String TAG="Group";
    private Button btnNewGroup;
    private GroupDialog dialogGroup;
    private List<Dbgroups> listSearchGroups;
    private Dbgroups searchGroup;
    GridLayoutManager layoutManager;
    private RecyclerView searchGroupsRecycler;
    private GroupsRosterAdapter groupsRosterAdapter;
    private TextView textCautionGroup;
    private int layoutIdForListItem;
    private String theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        Boolean s1 = sh.getBoolean("themeApp", true);

        if(s1) {
            setContentView(R.layout.activity_group2);
            layoutIdForListItem=R.layout.group_roster_item2;
            theme="light";
        }else{
            setContentView(R.layout.activity_group);
            layoutIdForListItem=R.layout.group_roster_item;
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

        textCautionGroup=findViewById(R.id.caution_group);
      //  layoutManager = new LinearLayoutManager(this);
         layoutManager=new GridLayoutManager(this,2);
        searchGroupsRecycler = findViewById(R.id.group_recycler_filter);


        searchGroupsRecycler.setLayoutManager(layoutManager);

        groupFilter=findViewById(R.id.groupFilter);
        btnNewGroup = findViewById(R.id.btn_new_group);
        btnNewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int width = displaymetrics.widthPixels * 3 / 4;
                int height = displaymetrics.heightPixels * 2 / 4;

                Log.d(TAG,  "= DialogN=" + width);
                dialogGroup = new GroupDialog(GroupActivity.this,  GroupActivity.this);
                dialogGroup.showDialog(GroupActivity.this, width, height, theme,"New group","new",null
                        ,null );
            }
        });

    }

    public void onResume() {
        super.onResume();
        groupFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            // при изменении текста выполняем фильтрацию
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String searchString = s.toString();

                if (searchString.trim().length() == 0) {

//                    searchRecycler.setHasFixedSize(true);
//                    int size = listSearchWords.size();
//                    listSearchWords.clear();
//                    wordsRosterAdapter = new WordsRosterAdapter(listSearchWords, DictActivity.this);
//                    searchRecycler.setAdapter(wordsRosterAdapter);
                    searchGroupsRecycler.setVisibility(View.GONE);
                    return;
                } else {

                    //String searchString='%'+s.toString()+'%';
                    Log.d("Dict", searchString.toString() + " " + count);
                    dataToSearchListGroup(searchString);
                }
                //wordsSearchAdapter.getFilter().filter(s.toString());
            }
        });
        dataToSearchListGroup("");
    }

    public void dataToSearchListGroup(String findStr) {
        String str = findStr.trim() + '%';
        //handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                listSearchGroups = AppDatabase.getInstance(getApplicationContext())
                        .groupDao()
                        .getGroupsFiltered3(str);
                Log.d("DICT", "dataToSearchList=" + str);
                if(listSearchGroups!=null) {
                    Log.d("DICT", "listSearchGroups=" + listSearchGroups.size());
                }
//            for(int i=0; listSearchGroups.size()>i; i++){
//               Log.d(TAG,"i="+listSearchGroups.get(i));
//            }



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
                    groupsRosterAdapter = new GroupsRosterAdapter(listSearchGroups, GroupActivity.this,layoutIdForListItem,theme);
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

        }, 0);

            }
        });
        thread.start();
    }

    @Override
    public void onItemClick(int position) {
        int top = position;

        Log.d(TAG, "position=" + top);
        View v = layoutManager.findViewByPosition(top);
        //v.startAnimation(animAlpha);
        CardView card = (CardView) v.findViewById(R.id.cardWord);
        card.setCardElevation(100f);
        TextView textViewName
                = (TextView) v.findViewById(R.id.tv_number_item);
        TextView useGroup
                = (TextView) v.findViewById(R.id.use_group);
        String useGroupS = (String) useGroup.getText();

        String selectedName = (String) textViewName.getText();
        TextView textViewId
                = (TextView) v.findViewById(R.id.id_item);

        String id = (String) textViewId.getText();

        Log.d(TAG, top + "= onScrollStateChanged=" + selectedName + " id=" + id);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"id=" + id);
                Intent intent = new Intent(GroupActivity.this, GroupWordsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("data",id);
                extras.putString("passedName",selectedName);
                extras.putString("passedTrain",useGroupS);

                //startActivity(new Intent(GroupActivity.this,GroupWordsActivity.class).putExtra("data",id));
                intent.putExtras(extras);
                startActivity(intent);
            }
        },200);

//        handler = new Handler();
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                searchGroup = AppDatabase.getInstance(getApplicationContext())
//                        .groupDao()
//                        .findById(Integer.parseInt(id));
//                Log.d(TAG, "idToSearchList=" + id);
//                Log.d(TAG, "searchGroup=" + searchGroup.getGroup());
//                // ViewDialog alert = new ViewDialog();
//                // alert.showDialog(DictActivity.this, "Window");
//
//
//            }
//        });
//        thread.start();
//
//        if (handler == null) return;
//
//
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                DisplayMetrics displaymetrics = new DisplayMetrics();
//                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//                int width = displaymetrics.widthPixels * 3 / 4;
//                int height = displaymetrics.heightPixels * 2 / 4;
//
//                Log.d(TAG, +top + "= Dialog=" + width);
//                dialogGroup = new GroupDialog(GroupActivity.this,  GroupActivity.this);
//                dialogGroup.showDialog(GroupActivity.this, width, height,"Edit group", String.valueOf(searchGroup.getId()),
//                        searchGroup.getGroup(), searchGroup.getDescription());
//            }
//        },100);
    }

    @Override
    public void sendData(String id, String group, String description, Boolean native1) {
        groupFilter.setText(group);
        Log.d(TAG,"SendData!!!! id="+id+" group="+group);
        handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                if(id.equals("new")){

                    Long idup = AppDatabase.getInstance(getApplicationContext())
                            .groupDao()
                            .insGroup( group, description, native1);

                    Log.d("DialogGroupAdd", String.valueOf(idup));
                }else {
//                    int idup = AppDatabase.getInstance(getApplicationContext())
//                            .groupDao()
//                            .upGroup(Integer.parseInt(id), group, description, native1);

  //                  Log.d("DialogGroup", String.valueOf(idup));
                }
            }
        });
        thread.start();


        if (handler == null) return;
        handler.postDelayed(new Runnable() {
            public void run() {

                //dataToSearchListGroup(groupFilter.getText().toString());
                dataToSearchListGroup("");
            }
        }, 500);
    }
}