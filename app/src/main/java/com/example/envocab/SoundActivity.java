package com.example.envocab;

import static android.app.PendingIntent.getActivity;

//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SoundActivity extends BaseActivity  {
    //    private static final int MENU3 = 1;
    private RecyclerView wordsList;
    private WordsAdapter wordsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        wordsList=findViewById(R.id.rv_words);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        wordsList.setLayoutManager(layoutManager);

        wordsList.setHasFixedSize(true);
        wordsAdapter=new WordsAdapter(100);
        wordsList.setAdapter(wordsAdapter);

    }
    //    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        MenuItem menu3 = menu.findItem(MENU3);
//        if(menu3 == null){
//            menu3 = menu.add(Menu.NONE, MENU3, 3, "Menu No. 3");
//        }
//        return true;
//    }
//@Override
//public boolean onOptionsItemSelected(MenuItem item) {
//
//    super.onOptionsItemSelected(item);
//    switch (item.getItemId()) {
//
//        case MENU3:
//            Toast.makeText(this, "Clicked: Menu No. 3", Toast.LENGTH_SHORT).show();
//            return true;
//
//        default:
//            return super.onOptionsItemSelected(item);
//    }
//
//}
}