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

public class ExercisesRosterAdapter extends RecyclerView.Adapter<ExercisesRosterAdapter.ExerciseViewHolder> {
    private final ExerciseRosterInterface exerciseRosterInterface;

    //private boolean firstLoading = true;
    private List<Dbexercises> exercisesList;
    private int layoutIdForListItem;
    Animation animAlpha;
    private String theme="";

    public ExercisesRosterAdapter(List<Dbexercises> exercisesList, ExerciseRosterInterface exerciseRosterInterface, int layoutIdForListItem, String theme) {
        this.exercisesList = exercisesList;
        this.exerciseRosterInterface = exerciseRosterInterface;
        this.layoutIdForListItem=layoutIdForListItem;
        this.theme=theme;
    }


    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
       // int layoutIdForListItem = R.layout.group_roster_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        //animAlpha= AnimationUtils.loadAnimation(parent.getContext(), R.anim.alpha);

        return new ExerciseViewHolder(view, exerciseRosterInterface);
    }
    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        //holder.bind(position);
        Dbexercises exercise = exercisesList.get(position);
       // holder.listItemNumberView.setText(exercise.getName());
        holder.listItemNumberView.setText(exercise.getName().substring(0, 1).toUpperCase() + exercise.getName().substring(1));
        holder.id_item.setText(String.valueOf(exercise.getId_ex()));
        holder.tech_name.setText(String.valueOf(exercise.getTech_name()));
        holder.destination.setText(String.valueOf(exercise.getDestination()));



    }

    @Override
    public int getItemCount() {
        return exercisesList.size();

    }

    class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView listItemNumberView;
        TextView viewHolderIndex;
        TextView id_item;
        TextView tech_name;
        TextView destination;


        public ExerciseViewHolder(@NonNull View itemView, ExerciseRosterInterface exerciseRosterInterface) {
            super(itemView);

            listItemNumberView = itemView.findViewById(R.id.tv_number_item);
            viewHolderIndex = itemView.findViewById(R.id.tv_holder_number);
            id_item = itemView.findViewById(R.id.id_item);
            tech_name = itemView.findViewById(R.id.tv_holder_tech_name);
            destination = itemView.findViewById(R.id.text_destination);



//            itemView.findViewById(R.id.btnSoundItem).setOnClickListener(new View.OnClickListener() {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (exerciseRosterInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            try {
                                exerciseRosterInterface.onItemClick(pos);
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            //v.startAnimation(animAlpha);
                        //    exerciseRosterInterface.onItemClick(pos);
                            //Log.d("TAG",id);
                        }
                    }
                }
            });

        }

    }
}
