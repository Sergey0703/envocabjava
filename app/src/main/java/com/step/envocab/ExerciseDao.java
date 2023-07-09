package com.step.envocab;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExerciseDao {


    @Query("insert into dbexercises (`name`,`tech_name`,`destination`) VALUES(:name,:tech_name,:destination)")
    Long insExercise( String name, String tech_name, String destination);

    @Query("Select * FROM dbexercises WHERE id_ex LIKE :id_ex")
    Dbexercises findById(int id_ex);

    @Query("Select * FROM dbexercises ")
    List<Dbexercises> findExercises();


}
