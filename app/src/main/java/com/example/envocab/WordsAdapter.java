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
    private final WordListInterface wordListInterface;
    //private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private static int viewHolderCount;
    private int numberItems;
    private boolean firstLoading=true;
    private List<Word> wordsList;
    private int height;


    public WordsAdapter(List<Word> wordsList, WordListInterface wordListInterface){
          //numberItems=numbersOfItems;
          //viewHolderCount=0;
        this.wordsList=wordsList;
        this.wordListInterface=wordListInterface;
    }



    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.word_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        //WordViewHolder viewHolder = new WordViewHolder(view);
        //int height ;
        //height = parent.getHeight()/2;

//        }else{
//            height = parent.getHeight();
//        }
        //int width = parent.getMeasuredWidth();
        //int h = parent.getMeasuredHeight();
        //Log.d("VH=","Parent="+parent+" height="+height+" h="+h+" width="+width );
        //  view.setLayoutParams(new RecyclerView.LayoutParams(width, height));

        return new WordViewHolder(view, wordListInterface);
    }

//        if (viewType == VIEW_TYPE_ITEM) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_list_item, parent, false);
//            return new WordViewHolder(view);
//        } else {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
//            return new WordViewHolder(view);
//
//        }

//    }
//    public class ItemViewHolder extends RecyclerView.ViewHolder {
//        TextView tvItem;
//        public ItemViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvItem = itemView.findViewById(R.id.tv_number_item);
//        }
//    }
//
//    private class LoadingviewHolder extends RecyclerView.ViewHolder {
//        ProgressBar progressBar;
//        public LoadingviewHolder(@NonNull View itemView) {
//            super(itemView);
//            //progressBar = itemView.findViewById(R.id.progressbar);
//        }
//    }

//    public int getItemViewType(int position) {
//        return wordsList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
//    }
//    private void populateItemRows(ItemViewHolder viewHolder, int position) {
//        String item = wordsList.get(position);
//        viewHolder.tvItem.setText(item);
//    }
    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        //holder.bind(position);
        Word word =wordsList.get(position);
        holder.listItemNumberView.setText(word.getWord());
        if(word.getTrain1()!=null&&word.getTrain1()==true) {
            holder.listItemNumberView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.green_circle, 0, 0, 0);
        }else{
            holder.listItemNumberView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_circle, 0, 0, 0);
        }
        holder.viewHolderIndex.setText(word.getTranslate());
        if(word.getTranslate().trim().length()>0) {
            holder.viewHolderTranscription.setText("["+word.getTranscript()+"]");
        }
    }

    @Override
    public int getItemCount() {
        return wordsList.size();

    }

    class WordViewHolder extends RecyclerView.ViewHolder{
        TextView listItemNumberView;
        TextView viewHolderIndex;
        TextView viewHolderTranscription;


        public WordViewHolder(@NonNull View itemView, WordListInterface wordListInterface) {
            super(itemView);

            listItemNumberView=itemView.findViewById(R.id.tv_number_item);
            viewHolderIndex=itemView.findViewById(R.id.tv_holder_number);
            viewHolderTranscription=itemView.findViewById(R.id.tv_transcription);

            itemView.findViewById(R.id.btnSoundItem).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                      if(wordListInterface!=null){
                          int pos=getAdapterPosition();
                          if(pos!=RecyclerView.NO_POSITION){
                                    wordListInterface.onItemClick(pos);
                          }
                      }
                }
            });

        }

        //void bind(int listIndex){
            //listItemNumberView.setText(String.valueOf(listIndex));

        //}
    }
}
