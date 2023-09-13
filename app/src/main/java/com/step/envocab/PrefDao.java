package com.step.envocab;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface PrefDao {

//    @Query("update Dbwords set word=:word, translate = :trans, transcript =:transcript, train1 =:train where id = :id")
//    int upWord(Integer id, String word, String trans, String transcript, Boolean train);
//
//    @Query("insert into Dbwords ('word','translate','transcript','train1') VALUES(:word,:trans,:transcript,:train)")
//    Long insWord( String word, String trans, String transcript, Boolean train);

    @Query("Select * FROM dbpref WHERE id_pref LIKE :id_pref")
    DbPref findById(int id_pref);
}
