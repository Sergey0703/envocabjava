package com.step.envocab;


import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.List;

public class GroupWithWords2 {
    private Integer id;
    private Integer id_group;
    private String word;
    private String translate;
    private String transcript;

    @TypeConverters({Converters.class})
    public Date trainDate;
    private Boolean train1;

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    public Date getTrainDate() {
        return trainDate;
    }

    public void setTrainDate(Date trainDate) {
        this.trainDate = trainDate;
    }

    public Boolean getTrain1() {
        return train1;
    }

    public void setTrain1(Boolean train1) {
        this.train1 = train1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_group() {
        return id_group;
    }

    public void setId_group(Integer id_group) {
        this.id_group = id_group;
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
