package com.step.envocab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.WordViewHolder> {
    private final WordListInterface wordListInterface;

    private boolean firstLoading = true;
    private List<Dbwords> wordsList;
    Animation animAlpha;
    int layoutIdForListItem;

    public WordsAdapter(List<Dbwords> wordsList, WordListInterface wordListInterface, int layoutIdForListItem) {
        this.wordsList = wordsList;
        this.wordListInterface = wordListInterface;
        this.layoutIdForListItem=layoutIdForListItem;
    }


    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        //layoutIdForListItem = R.layout.word_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        animAlpha= AnimationUtils.loadAnimation(parent.getContext(), R.anim.alpha);

        return new WordViewHolder(view, wordListInterface);
    }
    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        //holder.bind(position);
        Dbwords word = wordsList.get(position);
        holder.listItemNumberView.setText(word.getWord());
        holder.id_word_item.setText(String.valueOf(word.getId()));
        holder.id_group_item.setText(String.valueOf(word.getDescription()));

        if (word.getTrain1() != null && word.getTrain1() == true) {
            holder.simpleSwitch.setChecked(true);
            holder.listItemNumberView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.green_circle, 0, 0, 0);
        } else {
            holder.simpleSwitch.setChecked(false);
            holder.listItemNumberView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_circle, 0, 0, 0);
        }
        holder.viewHolderIndex.setText(word.getTranslate());
        if (word.getTranslate().trim().length() > 0) {
            holder.viewHolderTranscription.setText("[" + word.getTranscript() + "]");
        }

    }

    @Override
    public int getItemCount() {
        if(wordsList==null) return 0;
        return wordsList.size();

    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        TextView listItemNumberView;
        TextView viewHolderIndex;
        TextView viewHolderTranscription;
        SwitchCompat simpleSwitch;
        TextView id_word_item;
        TextView id_group_item;
        Boolean isTouched=false;

        public WordViewHolder(@NonNull View itemView, WordListInterface wordListInterface) {
            super(itemView);

            listItemNumberView = itemView.findViewById(R.id.tv_number_item);
            viewHolderIndex = itemView.findViewById(R.id.tv_holder_number);
            viewHolderTranscription = itemView.findViewById(R.id.tv_transcription);
            simpleSwitch = (SwitchCompat)itemView.findViewById(R.id.simpleSwitch);

            id_word_item=itemView.findViewById(R.id.id_word_item);
            id_group_item=itemView.findViewById(R.id.id_group_item);
//            simpleSwitch.setTextOn("On");
//            simpleSwitch.setTextOff("Off");

            itemView.findViewById(R.id.btnSoundItem).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (wordListInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {

                            v.startAnimation(animAlpha);
                            wordListInterface.onItemClick(pos,"sound",String.valueOf(id_word_item.getText()),false, String.valueOf(id_group_item.getText()));
                        }
                    }
                }
            });
            simpleSwitch.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    isTouched = true;
                    return false;
                }
            });
            simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isTouched) {
                        isTouched = false;

                        if (wordListInterface != null) {
                            int pos = getAdapterPosition();
                            if (pos != RecyclerView.NO_POSITION) {

                                wordListInterface.onItemClick(pos, "switch", String.valueOf(id_word_item.getText()), isChecked, String.valueOf(id_group_item.getText()));
                            }
                        }

                    }
                }

            });

        }

    }
}
