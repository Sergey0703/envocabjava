package com.step.envocab;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.sql.Date;

@Entity(tableName = "dbscore")

@TypeConverters(Converters.class)
public class Dbscore {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    private Integer id;
    @TypeConverters({Converters.class})
    private Date day;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
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

    private String descript;

    public Dbscore(Integer id, Date day, String descript, Boolean prior, Integer use_count) {
        this.id = id;
        this.day = day;
        this.descript = descript;
        this.prior = prior;
        this.use_count = use_count;
    }

    private Boolean prior;

    private Integer use_count;


}

