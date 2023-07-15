package com.step.envocab;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface CountDao {
    //@Insert
   // void insertGroup(Dbgroups group);

    @Query("SELECT * from Dbcounts WHERE id_exercice=:id_exercise ORDER BY id_count  ")
    List<Dbcounts> getCounts(int id_exercise);

    @Query("SELECT id_group from Dbcounts WHERE id_exercice=:id_exercise ORDER BY trainDate DESC  ")
    Long lastGroup(int id_exercise);

    @Query("SELECT * from Dbcounts WHERE id_exercice = :id_exercise AND id_word=:id_word AND id_group=:id_group ORDER BY id_count")
    Dbcounts getCountsFiltered(Integer id_exercise, Integer id_word, Long id_group);

    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    @Query("insert into Dbcounts ('id_exercice','id_word','id_group','train','trainDate') VALUES(:id_exercise,:id_word,:id_group,:train,:train_date)")
    Long insCount( Integer id_exercise, Integer id_word, Long id_group, Boolean train, Date train_date);

    @Query("update Dbcounts set id_exercice = :id_exercise, id_word=:id_word,  id_group =:id_group, train =:train, trainDate=:train_date WHERE id_count = :id_count")
    int upCount(Integer id_count, Integer id_exercise, Integer id_word, Long id_group, Boolean train, Date train_date);

    default void insertOrUpdate(Integer id_exercise, Integer id_word, Long id_group, Boolean train, Date train_date) {
        Dbcounts countsFromDB = getCountsFiltered(id_exercise, id_word,  id_group);
        if (countsFromDB!=null) {
            upCount(countsFromDB.getId_count(),id_exercise, id_word,  id_group, train, train_date );
        }else {

            insCount(id_exercise, id_word, id_group, train, train_date);
        }
    }

}
