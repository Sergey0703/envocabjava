package com.step.envocab;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WordDao {
    @Insert
    void insertWord(Dbwords word);

    @Query("Select * FROM Dbwords WHERE id LIKE :id")
    Dbwords findById(int id);

    @Query("Select * FROM Dbwords ORDER BY trainDate ASC Limit 1")
    Dbwords findLast();

    @Query("Select * FROM Dbwords WHERE trainDate > :trainDate ORDER BY trainDate ASC Limit 1")
    Dbwords findNext(Long trainDate);

    @Query("Select * FROM Dbwords WHERE trainDate < :trainDate ORDER BY trainDate DESC Limit 1")  //
    Dbwords findPrev(Long trainDate);

    @Query("Select * FROM Dbwords ORDER BY trainDate DESC Limit 1")  //
    Dbwords findPrevAdd();
    @Update
    void updateWord(Dbwords word);

    @Query("SELECT * FROM Dbwords ORDER BY trainDate ASC ")
    List<Dbwords> getAll();

    @Query("SELECT COUNT(*) FROM Dbwords WHERE train1 LIKE :train AND trainDate BETWEEN :startDate AND :endDate")
    int countToday(Long startDate,Long endDate, int train);

    @Query("SELECT COUNT(*) FROM Dbwords ")
    int countAll();
    @Query("SELECT * FROM Dbwords WHERE train1 LIKE :train AND trainDate BETWEEN :startDate AND :endDate")
    List<Dbwords> wordsForList(Long startDate, Long endDate, int train);

    @Query("SELECT * FROM Dbwords WHERE  trainDate BETWEEN :startDate AND :endDate")
    List<Dbwords> wordsForListAll(Long startDate, Long endDate);

    @Query("SELECT * FROM Dbwords WHERE train1 LIKE :train")
    List<Dbwords> wordsForListAll(int train);

    // Dao query with filter
    @Query("SELECT * from Dbwords WHERE word LIKE :filter ORDER BY word")
    List<Dbwords> getItemsFiltered(String filter);

    //fun getItemsFiltered(filter: String): LiveData<List<MyItem>>
}
