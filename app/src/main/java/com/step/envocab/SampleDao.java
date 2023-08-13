package com.step.envocab;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SampleDao {


    @Query("Select * FROM dbsample WHERE id_sample LIKE :id_sample")
    Dbsample findById(int id_sample);


}
