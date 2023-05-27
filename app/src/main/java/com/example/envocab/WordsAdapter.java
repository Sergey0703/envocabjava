package com.example.envocab;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.WordViewHolder>{
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
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

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_list_item, parent, false);
            return new WordViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new WordViewHolder(view);

        }

    }
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvItem;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tv_number_item);
        }
    }

    private class LoadingviewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        public LoadingviewHolder(@NonNull View itemView) {
            super(itemView);
            //progressBar = itemView.findViewById(R.id.progressbar);
        }
    }

    public int getItemViewType(int position) {
        return wordsList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }
//    private void populateItemRows(ItemViewHolder viewHolder, int position) {
//        String item = wordsList.get(position);
//        viewHolder.tvItem.setText(item);
//    }
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
            //Log.d("Tag",String.valueOf(listItemNumberView.getText() ));
            //int ind=getAdapterPosition();

        }

        //void bind(int listIndex){
            //listItemNumberView.setText(String.valueOf(listIndex));

        //}
    }
}
