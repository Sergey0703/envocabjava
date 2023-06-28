package com.step.envocab;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GroupDao {
    @Insert
    void insertGroup(Dbgroups group);

    @Query("SELECT * from Dbgroups WHERE 'group' LIKE :filter ORDER BY 'group'")
    List<Dbgroups> getGroupsFiltered(String filter);

//    //@Modifying
//    @Query("update Dbwords set word=:word, translate = :trans, transcript =:transcript, train1 =:train where id = :id")
//    int upWord(Integer id, String word, String trans, String transcript, Boolean train);
//
//    @Query("insert into Dbwords ('word','translate','transcript','train1') VALUES(:word,:trans,:transcript,:train)")
//    Long insWord( String word, String trans, String transcript, Boolean train);
//    @Query("Select * FROM Dbwords WHERE id LIKE :id")
//    Dbwords findById(int id);
//
//    @Query("Select * FROM Dbwords ORDER BY trainDate ASC Limit 1")
//    Dbwords findLast();
//
//    @Query("Select * FROM Dbwords WHERE trainDate > :trainDate ORDER BY trainDate ASC Limit 1")
//    Dbwords findNext(Long trainDate);
//
//    @Query("Select * FROM Dbwords WHERE trainDate < :trainDate ORDER BY trainDate DESC Limit 1")  //
//    Dbwords findPrev(Long trainDate);
//
//    @Query("Select * FROM Dbwords ORDER BY trainDate DESC Limit 1")  //
//    Dbwords findPrevAdd();
//    @Update
//    void updateWord(Dbwords word);
//
//    @Query("SELECT * FROM Dbwords ORDER BY trainDate ASC ")
//    List<Dbwords> getAll();
//
//    @Query("SELECT COUNT(*) FROM Dbwords WHERE train1 LIKE :train AND trainDate BETWEEN :startDate AND :endDate")
//    int countToday(Long startDate,Long endDate, int train);
//
//    @Query("SELECT COUNT(*) FROM Dbwords ")
//    int countAll();
//    @Query("SELECT * FROM Dbwords WHERE train1 LIKE :train AND trainDate BETWEEN :startDate AND :endDate")
//    List<Dbwords> wordsForList(Long startDate, Long endDate, int train);
//
//    @Query("SELECT * FROM Dbwords WHERE  trainDate BETWEEN :startDate AND :endDate")
//    List<Dbwords> wordsForListAll(Long startDate, Long endDate);
//
//    @Query("SELECT * FROM Dbwords WHERE train1 LIKE :train")
//    List<Dbwords> wordsForListAll(int train);
//
//    // Dao query with filter
//    @Query("SELECT * from Dbwords WHERE word LIKE :filter ORDER BY word")
//    List<Dbwords> getItemsFiltered(String filter);


}
