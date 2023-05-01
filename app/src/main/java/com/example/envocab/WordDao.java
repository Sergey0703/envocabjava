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

    @Update
    void updateWord(Word word);

    //@Query("Select * FROM word ")
    //Word findById(int id);

    @Query("SELECT * FROM dbwords")
    List<Word> getAll();
}
