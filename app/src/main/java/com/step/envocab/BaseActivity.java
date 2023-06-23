package com.step.envocab;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import android.widget.Toast;

import com.step.envocab.R;

public class BaseActivity extends AppCompatActivity {
    Activity activity;
    //Context context;
    ImageView menuIcon;

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.action_toolbar);
//        // using toolbar as ActionBar
//        setSupportActionBar(toolbar);
////        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
////        getSupportActionBar().setCustomView(R.layout.toolbar);
//    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //if (menu == MenuBuilder) (menu as MenuBuilder).setOptionalIconsVisible(true)
        if (menu instanceof MenuBuilder) {
            ((MenuBuilder) menu).setOptionalIconsVisible(true);
            Log.d("MENU","Menu!!!!!!!!!!!!!!!!!!!!!!!1");
        }

    getMenuInflater().inflate(R.menu.popup_menu, menu);
    //return true;
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuWordsTraining = menu.findItem(R.id.wordsTraining);
        if(menuWordsTraining != null){
            menuWordsTraining.setEnabled(true);
            menuWordsTraining.getIcon().setAlpha(255);
            //Log.d("MENU","Menu!!!!!!!!!!!!!!!!!!!!!!!1000");
        }
        MenuItem menuWordsTraining2 = menu.findItem(R.id.wordsTraining2);
        if(menuWordsTraining2 != null){
            menuWordsTraining2.setEnabled(true);
            menuWordsTraining2.getIcon().setAlpha(255);
        }
        MenuItem menuSoundTraining = menu.findItem(R.id.soundTraining);
        if(menuSoundTraining != null){
            menuSoundTraining.setEnabled(true);
            menuSoundTraining.getIcon().setAlpha(255);
        }
        MenuItem menuSoundTraining2 = menu.findItem(R.id.soundTraining2);
        if(menuSoundTraining2 != null){
            menuSoundTraining2.setEnabled(true);
            menuSoundTraining2.getIcon().setAlpha(255);
        }
        super.onPrepareOptionsMenu(menu);
        MenuItem menuDict = menu.findItem(R.id.dict);
        if(menuDict != null){
            menuDict.setEnabled(true);
            menuDict.getIcon().setAlpha(255);
        }
        MenuItem menuDict2 = menu.findItem(R.id.dict2);
        if(menuDict2 != null){
            menuDict2.setEnabled(true);
            menuDict2.getIcon().setAlpha(255);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
        case R.id.wordsTraining:
            Intent intent1=new Intent(BaseActivity.this, MainActivity.class);
            startActivity(intent1);
            return true;
        case R.id.wordsTraining2:
            Intent intent2=new Intent(BaseActivity.this, MainActivity.class);
            startActivity(intent2);
            return true;
        case R.id.soundTraining:
            Intent intent3=new Intent(BaseActivity.this, SoundActivity.class);
            startActivity(intent3);
            return true;
        case R.id.soundTraining2:
            Intent intent4=new Intent(BaseActivity.this, SoundActivity.class);
            startActivity(intent4);
            return true;
        case R.id.dict:
            Intent intent5=new Intent(BaseActivity.this, DictActivity.class);
            startActivity(intent5);
            return true;
        case R.id.dict2:
            Intent intent6=new Intent(BaseActivity.this, DictActivity.class);
            startActivity(intent6);
            return true;
        case R.id.aboutProgram:
//            AboutDialog aboutDialog = new AboutDialog();
//            aboutDialog.show(getSupportFragmentManager(),"example dialog");
            ViewDialog alert = new ViewDialog();
            alert.showDialog(BaseActivity.this, "Window");
            return true;


        default:
            return super.onOptionsItemSelected(item);
    }

}

}