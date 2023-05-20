package com.example.envocab;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {
    ImageView menuIcon;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.popup_menu, menu);
    return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
        case R.id.soundTraining:
            Intent intent=new Intent(BaseActivity.this, SoundActivity.class);
            startActivity(intent);
            return true;

        case R.id.aboutProgram:
            AboutDialog aboutDialog = new AboutDialog();
            aboutDialog.show(getSupportFragmentManager(),"example dialog");
            return true;

        //case R.id.submenu2:
        //Toast.makeText(this, "Clicked: Menu No. 2 - SubMenu No .2", Toast.LENGTH_SHORT).show();
          //  return true;

        default:
            return super.onOptionsItemSelected(item);
    }

}

}