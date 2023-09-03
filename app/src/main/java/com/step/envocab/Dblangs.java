package com.step.envocab;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "dblangs")

@TypeConverters(Converters.class)
public class Dblangs {

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Boolean getPrior() {
        return prior;
    }

    public void setPrior(Boolean prior) {
        this.prior = prior;
    }

    public Integer getUse_count() {
        return use_count;
    }

    public void setUse_count(Integer use_count) {
        this.use_count = use_count;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    private Integer id;
    private String lang;
    private String flag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFlag() {
        return flag;
    }

    public Dblangs(Integer id, String lang, String flag, Boolean prior, Integer use_count) {
        this.id = id;
        this.lang = lang;
        this.flag = flag;
        this.prior = prior;
        this.use_count = use_count;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    private Boolean prior;

    private Integer use_count;


}

