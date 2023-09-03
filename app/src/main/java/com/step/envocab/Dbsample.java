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

    @NonNull
    private Integer id_lang;

    private String translate;
    private Integer priority;

    private String partOfSpeech;

    private String clause;

    private String clause_trans;

    @NonNull
    public Integer getId_lang() {
        return id_lang;
    }

    public void setId_lang(@NonNull Integer id_lang) {
        this.id_lang = id_lang;
    }

    public String getClause_trans() {
        return clause_trans;
    }

    public void setClause_trans(String clause_trans) {
        this.clause_trans = clause_trans;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public Dbsample(@NonNull Integer id_sample, @NonNull Integer id_word, @NonNull Integer id_lang, String translate, Integer priority, String partOfSpeech, String clause, String clause_trans) {
        this.id_sample = id_sample;
        this.id_word = id_word;
        this.id_lang = id_lang;
        this.translate = translate;
        this.priority = priority;
        this.partOfSpeech = partOfSpeech;
        this.clause = clause;
        this.clause_trans = clause_trans;
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
