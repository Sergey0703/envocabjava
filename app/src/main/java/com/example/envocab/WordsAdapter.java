package com.example.envocab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.WordViewHolder>{
    private static int viewHolderCount;
    private int numberItems;
    private List<Word> wordsList;


    public WordsAdapter(List<Word> wordsList){
          //numberItems=numbersOfItems;
          //viewHolderCount=0;
        this.wordsList=wordsList;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context= parent.getContext();
        int layoutIdForListItem=R.layout.word_list_item;

        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(layoutIdForListItem, parent, false);
        WordViewHolder viewHolder=new WordViewHolder(view);
        //viewHolder.viewHolderIndex.setText("ViewHolder index="+viewHolderCount);
        //viewHolderCount++;
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        //holder.bind(position);
        Word word =wordsList.get(position);
        holder.listItemNumberView.setText(word.getWord());
        holder.viewHolderIndex.setText(word.getTranslate());
    }

    @Override
    public int getItemCount() {
        return wordsList.size();

    }

    class WordViewHolder extends RecyclerView.ViewHolder{
        TextView listItemNumberView;
        TextView viewHolderIndex;


        public WordViewHolder(@NonNull View itemView) {
            super(itemView);

            listItemNumberView=itemView.findViewById(R.id.tv_number_item);
            viewHolderIndex=itemView.findViewById(R.id.tv_holder_number);

        }

        //void bind(int listIndex){
            //listItemNumberView.setText(String.valueOf(listIndex));

        //}
    }
}
