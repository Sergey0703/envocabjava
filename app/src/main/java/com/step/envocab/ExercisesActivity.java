package com.step.envocab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class ExercisesActivity extends BaseActivity implements ExerciseRosterInterface{
    private String theme="";
    private Handler handler;
    private String TAG="Exercises";
    private List<Dbexercises> listSearchExercises;
    GridLayoutManager layoutManager;
    private RecyclerView searchExercisesRecycler;
    private ExercisesRosterAdapter exercisesRosterAdapter;
    private TextView textCautionExercises;
    private int layoutIdForListItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        Boolean s1 = sh.getBoolean("themeApp", true);

        if(s1) {
            setContentView(R.layout.activity_exercises);
            //layoutIdForListItem=R.layout.word_dict_roster_item2;
            theme="light";
        }else{
            setContentView(R.layout.activity_exercises);
           // layoutIdForListItem=R.layout.word_roster_item;
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

        textCautionExercises=findViewById(R.id.caution_exercise);
        //  layoutManager = new LinearLayoutManager(this);
        layoutManager=new GridLayoutManager(this,2);
        searchExercisesRecycler = findViewById(R.id.exercise_recycler_filter);


        searchExercisesRecycler.setLayoutManager(layoutManager);
        dataToSearchListExercises();

    }

    public void dataToSearchListExercises() {

        //handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                listSearchExercises = AppDatabase.getInstance(getApplicationContext())
                        .exerciseDao()
                        .findExercises();

                Log.d(TAG, "listSearchExercises=" + listSearchExercises.size());
//            for(int i=0; listSearchGroups.size()>i; i++){
//               Log.d(TAG,"i="+listSearchGroups.get(i));
//            }

            }
        });
        thread.start();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            //handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                //Log.d("DICT","listSearchWords2="+ listSearchWords);
                if (listSearchExercises != null && listSearchExercises.size() != 0) {

                    Log.d("DICT", "listSearchGroups=" + listSearchExercises.size());
                    textCautionExercises.setVisibility(View.GONE);
                    searchExercisesRecycler.setVisibility(View.VISIBLE);


                    searchExercisesRecycler.setHasFixedSize(true);
                    exercisesRosterAdapter = new ExercisesRosterAdapter(listSearchExercises, ExercisesActivity.this,layoutIdForListItem,theme);
                    searchExercisesRecycler.setAdapter(exercisesRosterAdapter);

                } else {
                    Log.d("Group3", "listSearchExercises3=NULLLLL");
                    searchExercisesRecycler.setVisibility(View.GONE);
                    textCautionExercises.setVisibility(View.VISIBLE);

                }

            }

        }, 100);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void sendData(String id_ex, String name) {

    }
}