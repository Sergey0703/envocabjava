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
        }

    getMenuInflater().inflate(R.menu.popup_menu, menu);
    return true;
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        this.activity=activity;
//    }

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

        case R.id.aboutProgram:
//            AboutDialog aboutDialog = new AboutDialog();
//            aboutDialog.show(getSupportFragmentManager(),"example dialog");
            ViewDialog alert = new ViewDialog();
            alert.showDialog(BaseActivity.this, "Error de conexión al servidor");
            return true;

        //case R.id.submenu2:
        //Toast.makeText(this, "Clicked: Menu No. 2 - SubMenu No .2", Toast.LENGTH_SHORT).show();
          //  return true;

        default:
            return super.onOptionsItemSelected(item);
    }

}

}