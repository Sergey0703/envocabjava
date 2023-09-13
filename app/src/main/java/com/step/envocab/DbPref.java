package com.step.envocab;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "dbpref")

@TypeConverters(Converters.class)
public class DbPref {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id_pref")
    @NonNull
    //type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1, defaultValue='undefined'
    private Integer id_pref;

    @TypeConverters({Converters.class})
    private Date reminderDate;

    private String pref_theme;

    private Integer language;

    private Boolean pref_bool;

    private String pref_string;

    private Integer pref_int;

    public DbPref(Date reminderDate, String pref_theme, Integer language, Boolean pref_bool, String pref_string, Integer pref_int) {
        this.reminderDate = reminderDate;
        this.pref_theme = pref_theme;
        this.language = language;
        this.pref_bool = pref_bool;
        this.pref_string = pref_string;
        this.pref_int = pref_int;
    }

    @NonNull
    public Integer getId_pref() {
        return id_pref;
    }
    public void setId_pref(@NonNull Integer id_pref) {
        this.id_pref = id_pref;
    }

    public Date getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

    public String getPref_theme() {
        return pref_theme;
    }

    public void setPref_theme(String pref_theme) {
        this.pref_theme = pref_theme;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public Boolean getPref_bool() {
        return pref_bool;
    }

    public void setPref_bool(Boolean pref_bool) {
        this.pref_bool = pref_bool;
    }

    public String getPref_string() {
        return pref_string;
    }

    public void setPref_string(String pref_string) {
        this.pref_string = pref_string;
    }

    public Integer getPref_int() {
        return pref_int;
    }

    public void setPref_int(Integer pref_int) {
        this.pref_int = pref_int;
    }
}
