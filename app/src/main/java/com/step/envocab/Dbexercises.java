package com.step.envocab;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "dbexercises")

@TypeConverters(Converters.class)
public class Dbexercises {

    public Dbexercises(String name, String tech_name) {

        this.name = name;
        this.tech_name=tech_name;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id_ex")
    @NonNull

    private Integer id_ex;
    private String name;

    public String getTech_name() {
        return tech_name;
    }

    public void setTech_name(String tech_name) {
        this.tech_name = tech_name;
    }

    private String tech_name;

    public void setId_ex(@NonNull Integer id_ex) {
        this.id_ex = id_ex;

    }

    @NonNull
    public Integer getId_ex() {
        return id_ex;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

