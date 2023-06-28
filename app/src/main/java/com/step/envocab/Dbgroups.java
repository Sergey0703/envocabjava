package com.step.envocab;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "dbgroups")

@TypeConverters(Converters.class)
public class Dbgroups {
    //defaultValue="undefined"
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    @NonNull
    //type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1, defaultValue='undefined'
    private Integer id;
    private String group;
   // private Integer id_word;

    private Boolean native1;
    private String description;

    public Dbgroups(String group, String description, Boolean native1){
        this.group=group;
        this.description=description;

        this.native1=false;
    }


//    public void setTrainDate(Date trainDate) {
//        this.trainDate = trainDate;
//    }
//    public Date getTrainDate() {
//        return trainDate;
//    }

    public Boolean getNative1() {
        return native1;
    }

    public void setNative1(Boolean native1) {
        this.native1 = native1;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String transcript) {
        this.description = description;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", group='" + group + '\'' +
                ", description='" + description + '\'' +
                ", native1=" + native1 +

                '}';
    }

    public void setGroup(String group) {
        this.group = group;
    }


}

