package com.step.envocab;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "dbgroupsandwords", primaryKeys = {"id", "id_group"})
public class Dbgroupsandwords {
//    @PrimaryKey(autoGenerate = true)
//    @NonNull
//    private Integer id_groups_and_words;
    @NonNull
    private Integer id_group;
    @NonNull
    private Integer id;

//    public Integer getId_groups_and_words() {
//        return id_groups_and_words;
//    }
//
//    public void setId_groups_and_words(Integer id_groups_and_words) {
//        this.id_groups_and_words = id_groups_and_words;
//    }

    public void setId_group(Integer id_group) {
        this.id_group = id_group;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_group() {
        return id_group;
    }

    public Integer getId() {
        return id;
    }
}
