package com.step.envocab;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GroupWordsRosterAdapter extends RecyclerView.Adapter<GroupWordsRosterAdapter.GroupWordsViewHolder> {
    private final GroupWordsRosterInterface groupWordsRosterInterface;

    private boolean firstLoading = true;
    private List<GroupWithWords2> groupWordsList;
    private int layoutIdForListItem;

    Animation animAlpha;

    public GroupWordsRosterAdapter(List<GroupWithWords2> groupWordsList, GroupWordsRosterInterface groupWordsRosterInterface, int layoutIdForListItem) {
        this.groupWordsList = groupWordsList;
        this.groupWordsRosterInterface = groupWordsRosterInterface;
        this.layoutIdForListItem=layoutIdForListItem;
    }


    @NonNull
    @Override
    public GroupWordsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        //int layoutIdForListItem = R.layout.group_word_roster_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        //animAlpha= AnimationUtils.loadAnimation(parent.getContext(), R.anim.alpha);

        return new GroupWordsViewHolder(view, groupWordsRosterInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupWordsViewHolder holder, int position) {
        //holder.bind(position);
        GroupWithWords2 word = groupWordsList.get(position);
        holder.listItemNumberView.setText(String.valueOf(word.getWord()));
        holder.id_item.setText(String.valueOf(word.getId()));
//        if (word.getTrain1() != null && word.getTrain1() == true) {
//            holder.listItemNumberView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.green_circle, 0, 0, 0);
//        } else {
//            holder.listItemNumberView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_circle, 0, 0, 0);
//        }
        holder.viewHolderIndex.setText(word.getTranslate());
        if(word.getId_group()!=null){
            holder.btnDelItem.setVisibility(View.VISIBLE);
            holder.btnAddItem.setVisibility(View.INVISIBLE);
        }else{
            holder.btnDelItem.setVisibility(View.INVISIBLE);
            holder.btnAddItem.setVisibility(View.VISIBLE);
        }
//        if (word.getTranslate().trim().length() > 0) {
//            holder.viewHolderTranscription.setText("[" + word.getTranscript() + "]");
//        }
    }

    @Override
    public int getItemCount() {
        return groupWordsList.size();

    }

    class GroupWordsViewHolder extends RecyclerView.ViewHolder {
        TextView listItemNumberView;
        TextView viewHolderIndex;
        TextView viewHolderTranscription;
        TextView id_item;
        ImageButton btnDelItem, btnAddItem;

        public GroupWordsViewHolder(@NonNull View itemView, GroupWordsRosterInterface groupWordsRosterInterface) {
            super(itemView);

            listItemNumberView = itemView.findViewById(R.id.tv_number_item);
            viewHolderIndex = itemView.findViewById(R.id.tv_holder_number);
            id_item = itemView.findViewById(R.id.id_item);
            btnDelItem=itemView.findViewById(R.id.btnDelItem);
            btnAddItem=itemView.findViewById(R.id.btnAddItem);
            //String id=String.valueOf(id_item.getText());

            //viewHolderTranscription = itemView.findViewById(R.id.tv_transcription);

            itemView.findViewById(R.id.btnDelItem).setOnClickListener(new View.OnClickListener() {
            //itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (groupWordsRosterInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {

                            //v.startAnimation(animAlpha);
                            groupWordsRosterInterface.onItemClick(pos, "del");
                            //Log.d("TAG",id);
                        }
                    }
                }
            });
            itemView.findViewById(R.id.btnAddItem).setOnClickListener(new View.OnClickListener() {
                //itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (groupWordsRosterInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {

                            //v.startAnimation(animAlpha);
                            groupWordsRosterInterface.onItemClick(pos, "add");
                            //Log.d("TAG",id);
                        }
                    }
                }
            });

        }


    }
}
