package com.step.envocab;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GroupsRosterAdapter extends RecyclerView.Adapter<GroupsRosterAdapter.GroupViewHolder> {
    private final GroupRosterInterface groupRosterInterface;

    private boolean firstLoading = true;
    private List<Dbgroups> groupsList;
    private int layoutIdForListItem;
    Animation animAlpha;

    public GroupsRosterAdapter(List<Dbgroups> groupsList, GroupRosterInterface groupRosterInterface, int layoutIdForListItem) {
        this.groupsList = groupsList;
        this.groupRosterInterface = groupRosterInterface;
        this.layoutIdForListItem=layoutIdForListItem;
    }


    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
       // int layoutIdForListItem = R.layout.group_roster_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        //animAlpha= AnimationUtils.loadAnimation(parent.getContext(), R.anim.alpha);

        return new GroupViewHolder(view, groupRosterInterface);
    }
    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        //holder.bind(position);
        Dbgroups group = groupsList.get(position);
        holder.listItemNumberView.setText(group.getGroup());
        holder.id_item.setText(String.valueOf(group.getId_group()));
        holder.use_group.setText(String.valueOf(group.getUse_group()));

        if(group.getUse_group()!=null && group.getUse_group()>0) {
            holder.use_group_text.setText("This group is training");
            holder.use_group_text.setTextColor(Color.parseColor("#3A5BAE"));
        }else{
            holder.use_group_text.setText("This group is not currently training");
            holder.use_group_text.setTextColor(Color.parseColor("#DE817B"));
        }
        Log.d("RRR=",group.getGroup()+" " +String.valueOf(group.getId_group()));
//        if (word.getTrain1() != null && word.getTrain1() == true) {
//            holder.listItemNumberView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.green_circle, 0, 0, 0);
//        } else {
//            holder.listItemNumberView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_circle, 0, 0, 0);
//        }
        holder.viewHolderIndex.setText("Words: "+group.getDescription());
//        if (word.getTranslate().trim().length() > 0) {
//            holder.viewHolderTranscription.setText("[" + word.getTranscript() + "]");
//        }
    }

    @Override
    public int getItemCount() {
        return groupsList.size();

    }

    class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView listItemNumberView;
        TextView viewHolderIndex;
        TextView viewHolderTranscription;
        TextView id_item;
        TextView use_group;
        TextView use_group_text;

        public GroupViewHolder(@NonNull View itemView, GroupRosterInterface groupRosterInterface) {
            super(itemView);

            listItemNumberView = itemView.findViewById(R.id.tv_number_item);
            viewHolderIndex = itemView.findViewById(R.id.tv_holder_number);
            id_item = itemView.findViewById(R.id.id_item);
            use_group=itemView.findViewById(R.id.use_group);
            use_group_text=itemView.findViewById(R.id.use_group_text);
            //String id=String.valueOf(id_item.getText());

            //viewHolderTranscription = itemView.findViewById(R.id.tv_transcription);

//            itemView.findViewById(R.id.btnSoundItem).setOnClickListener(new View.OnClickListener() {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (groupRosterInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {

                            //v.startAnimation(animAlpha);
                           groupRosterInterface.onItemClick(pos);
                            //Log.d("TAG",id);
                        }
                    }
                }
            });

        }

    }
}
