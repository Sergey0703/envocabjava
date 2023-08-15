package com.step.envocab;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "dbsample")
public class Dbsample {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id_sample;

    @NonNull
    private Integer id_word;


    private Integer priority;

    private String partOfSpeech;

    private String clause;

    private String translate;

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public Dbsample(@NonNull Integer id_sample, @NonNull Integer id_word, String partOfSpeech, String clause, Integer priority, String translate ) {
        this.id_sample = id_sample;
        this.id_word = id_word;
        this.priority = priority;
        this.partOfSpeech = partOfSpeech;
        this.clause = clause;
        this.translate = translate;

    }

    @NonNull
    public Integer getId_sample() {
        return id_sample;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public void setId_sample(@NonNull Integer id_sample) {
        this.id_sample = id_sample;
    }

    @NonNull
    public Integer getId_word() {
        return id_word;
    }

    public void setId_word(@NonNull Integer id_word) {
        this.id_word = id_word;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getClause() {
        return clause;
    }

    public void setClause(String clause) {
        this.clause = clause;
    }
}
