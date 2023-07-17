package com.step.envocab;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity(tableName = "dbcounts")
public class Dbcounts {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id_count;

    @NonNull
    private Integer id_exercice;

    @NonNull
    private Integer id_group;

    @NonNull
    private Integer id_word;


    private Boolean train;

    @TypeConverters({Converters.class})
    public Date trainDate;

    public Dbcounts(@NonNull Integer id_count, @NonNull Integer id_exercice, @NonNull Integer id_group, @NonNull Integer id_word, Boolean train, Date trainDate) {
        this.id_count = id_count;
        this.id_exercice = id_exercice;
        this.id_group = id_group;
        this.id_word = id_word;
        this.train = train;
        this.trainDate = trainDate;
    }

    @NonNull
    public Integer getId_count() {
        return id_count;
    }

    public void setId_count(@NonNull Integer id_count) {
        this.id_count = id_count;
    }

    @NonNull
    public Integer getId_exercice() {
        return id_exercice;
    }

    public void setId_exercice(@NonNull Integer id_exercice) {
        this.id_exercice = id_exercice;
    }

    @NonNull
    public Integer getId_group() {
        return id_group;
    }

    public void setId_group(@NonNull Integer id_group) {
        this.id_group = id_group;
    }

    @NonNull
    public Integer getId_word() {
        return id_word;
    }

    public void setId_word(@NonNull Integer id_word) {
        this.id_word = id_word;
    }

    public Boolean getTrain() {
        return train;
    }

    public void setTrain(Boolean train) {
        this.train = train;
    }

    public Date getTrainDate() {
        return trainDate;
    }

    public void setTrainDate(Date trainDate) {
        this.trainDate = trainDate;
    }
}
