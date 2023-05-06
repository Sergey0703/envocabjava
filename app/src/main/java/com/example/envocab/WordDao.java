package com.example.envocab;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WordDao {
    @Insert
    void insertWord(Word word);

    @Query("Select * FROM dbwords WHERE id LIKE :id")
    Word findById(int id);

    @Query("Select * FROM dbwords ORDER BY trainDate ASC Limit 1")
    Word findLast();

    @Query("Select * FROM dbwords WHERE trainDate > :trainDate ORDER BY trainDate ASC Limit 1")
    Word findNext(Long trainDate);

    @Query("Select * FROM dbwords WHERE trainDate < :trainDate ORDER BY trainDate DESC Limit 1")  //
    Word findPrev(Long trainDate);

    @Query("Select * FROM dbwords ORDER BY trainDate DESC Limit 1")  //
    Word findPrevAdd();
    @Update
    void updateWord(Word word);

    @Query("SELECT * FROM dbwords ORDER BY trainDate ASC")
    List<Word> getAll();

    @Query("SELECT COUNT(*) FROM dbwords WHERE train1 LIKE :train AND trainDate BETWEEN :startDate AND :endDate")
    int countToday(Long startDate,Long endDate, int train);
}
