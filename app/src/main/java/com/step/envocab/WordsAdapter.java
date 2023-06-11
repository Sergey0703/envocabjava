package com.step.envocab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.step.envocab.R;

import java.util.List;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.WordViewHolder> {
    private final WordListInterface wordListInterface;

    private boolean firstLoading = true;
    private List<Word> wordsList;
    Animation animAlpha;

    public WordsAdapter(List<Word> wordsList, WordListInterface wordListInterface) {
        this.wordsList = wordsList;
        this.wordListInterface = wordListInterface;
    }


    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.word_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        animAlpha= AnimationUtils.loadAnimation(parent.getContext(), R.anim.alpha);

        return new WordViewHolder(view, wordListInterface);
    }
    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        //holder.bind(position);
        Word word = wordsList.get(position);
        holder.listItemNumberView.setText(word.getWord());
        if (word.getTrain1() != null && word.getTrain1() == true) {
            holder.listItemNumberView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.green_circle, 0, 0, 0);
        } else {
            holder.listItemNumberView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_circle, 0, 0, 0);
        }
        holder.viewHolderIndex.setText(word.getTranslate());
        if (word.getTranslate().trim().length() > 0) {
            holder.viewHolderTranscription.setText("[" + word.getTranscript() + "]");
        }
    }

    @Override
    public int getItemCount() {
        return wordsList.size();

    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        TextView listItemNumberView;
        TextView viewHolderIndex;
        TextView viewHolderTranscription;


        public WordViewHolder(@NonNull View itemView, WordListInterface wordListInterface) {
            super(itemView);

            listItemNumberView = itemView.findViewById(R.id.tv_number_item);
            viewHolderIndex = itemView.findViewById(R.id.tv_holder_number);
            viewHolderTranscription = itemView.findViewById(R.id.tv_transcription);

            itemView.findViewById(R.id.btnSoundItem).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (wordListInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {

                            v.startAnimation(animAlpha);
                            wordListInterface.onItemClick(pos);
                        }
                    }
                }
            });

        }

    }
}
