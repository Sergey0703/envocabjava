package com.example.envocab;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "dbwords")
public class Word {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String word;
    private String translate;

    //private Date trainDate;
    private Boolean train1;
    private String transcript;

    public Word(){

    }

    /*
    public void setTrainDate(Date trainDate) {
        this.trainDate = trainDate;
    }
    */
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


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
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

