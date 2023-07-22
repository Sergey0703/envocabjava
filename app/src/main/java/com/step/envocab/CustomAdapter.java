package com.step.envocab;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;

public class CustomAdapter extends BaseAdapter {
    Context context;
    String countryList[];
    String countryList2[];
    Boolean  markWord[];
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, String[] countryList, String[] countryList2, Boolean[] markWord) {
        this.context = context;
        this.countryList = countryList;
        this.countryList2 = countryList2;
        this.markWord = markWord;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return countryList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.list_view, null);
        ImageView mark = (ImageView)           view.findViewById(R.id.imView);
        TextView country = (TextView)           view.findViewById(R.id.textView);
        TextView country2 = (TextView)           view.findViewById(R.id.textView2);
       // ImageView icon = (ImageView) view.findViewById(R.id.icon);
        if(markWord[i]==true) {
            mark.setBackgroundColor(Color.GREEN);
        }else {
            mark.setBackgroundColor(Color.RED);
        }
        country.setText(countryList[i]);
        country2.setText(countryList2[i]);

        //icon.setImageResource(flags[i]);
        return view;
    }
}