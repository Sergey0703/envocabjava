package com.step.envocab;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "dbwords")

@TypeConverters(Converters.class)
public class Dbwords {
    //defaultValue="undefined"
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    @NonNull
    //type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1, defaultValue='undefined'
    private Integer id;
    private String word;
    private String translate;
    @TypeConverters({Converters.class})
    public Date trainDate;
    private Boolean train1;
    private String transcript;
    private String description;

    public Dbwords(String word, String translate, String transcript, String description){
        this.word=word;
        this.translate=translate;
        this.transcript=transcript;
        this.description=description;
        this.train1=false;
    }


    public void setTrainDate(Date trainDate) {
        this.trainDate = trainDate;
    }
    public Date getTrainDate() {
        return trainDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getTrain1() {
        return train1;
    }

    public void setTrain1(Boolean train1) {
        this.train1 = train1;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", translate='" + translate + '\'' +
                ", train1=" + train1 +
                ", transcript='" + transcript + '\'' +
                ", tranDate='" + trainDate + '\'' +
                '}';
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }
}

