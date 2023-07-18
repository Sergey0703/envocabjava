package com.step.envocab;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface WordDao {
    @Insert
    void insertWord(Dbwords word);

    //@Modifying
    @Query("update Dbwords set word=:word, translate = :trans, transcript =:transcript, train1 =:train where id = :id")
    int upWord(Integer id, String word, String trans, String transcript, Boolean train);

    @Query("insert into Dbwords ('word','translate','transcript','train1') VALUES(:word,:trans,:transcript,:train)")
    Long insWord( String word, String trans, String transcript, Boolean train);

//    @Query("SELECT dbwords.id, dbwords.word, dbwords.transcript, dbwords.translate from dbwords INNER JOIN (SELECT id from dbgroupsandwords WHERE dbgroupsandwords.`id_group` =:id_group ) AS sel ON dbwords.id = sel.id WHERE dbwords.id NOT IN (SELECT id_word FROM dbcounts WHERE id_group=:id_group AND id_exercice=:id_exercise AND id_word IS NOT NULL ) Limit :limit   ")
//    List<Dbwords> getWordsTrain(int id_exercise, Long id_group, int limit);

    @Query("SELECT dbwords.id, dbwords.word, dbwords.transcript, dbwords.translate, sel2.trainDate, sel2.train1 from dbwords " +
            "INNER JOIN (SELECT id from dbgroupsandwords WHERE dbgroupsandwords.`id_group` =:id_group ) AS sel ON dbwords.id = sel.id " +
            "LEFT JOIN (SELECT id_word, id_group, trainDate, train AS train1 FROM dbcounts WHERE id_exercice=:id_exercise AND id_group=:id_group  ) AS sel2 ON dbwords.id=sel2.id_word " +
            "WHERE :isnull IS NULL OR sel2.train1 LIKE :train1 ORDER BY sel2.trainDate ASC Limit :limit   ")
    List<Dbwords> getWordsTrain2(int id_exercise, int id_group, int limit, Integer isnull, boolean train1);

    @Query("SELECT id, word, translate, transcript, sel.trainDate from dbwords INNER JOIN " +
            "(SELECT trainDate, id_word FROM dbcounts WHERE dbcounts.id_exercice=:id_exercise AND dbcounts.id_group=:id_group AND dbcounts.trainDate<:trainDate ORDER BY dbcounts.trainDate DESC LIMIT :limit Offset :offset )" +
            " AS sel ON dbwords.id =sel.id_word  ")
    List<Dbwords> getWordsTrainPrev(Integer id_exercise, int id_group, Long trainDate,Integer limit, int offset );

    @Query("Select * FROM Dbwords WHERE trainDate > :trainDate ORDER BY trainDate ASC Limit 1")
    Dbwords getCounts(Long trainDate);

    @Query("SELECT dbwords.id, dbwords.word, dbwords.transcript, dbwords.translate, dbwords.trainDate from dbwords " +
            "INNER JOIN (SELECT id from dbgroupsandwords WHERE dbgroupsandwords.`id_group` =:id_group ) AS sel ON dbwords.id = sel.id " +
            "ORDER BY dbwords.trainDate ASC  Limit :limit Offset :offset ")
    List<Dbwords> getWordsSound(int id_group, int limit, int offset);

//    @Query("SELECT dbwords.id, dbwords.word, dbwords.transcript, dbwords.translate from dbwords WHERE dbwords.id NOT IN (SELECT id_word FROM dbcounts WHERE id_exercice=:id_exercise AND id_word IS NOT NULL ) Limit :limit  ")
//    List<Dbwords> getWordsTrainWithoutGroup(int id_exercise, int limit);
    @Query("SELECT dbwords.id, dbwords.word, dbwords.transcript, dbwords.translate from dbwords LEFT JOIN (SELECT id_word, trainDate FROM dbcounts WHERE id_exercice=:id_exercise) AS sel ON dbwords.id=sel.id_word ORDER BY sel.trainDate ASC Limit :limit  ")
    List<Dbwords> getWordsTrainWithoutGroup2(int id_exercise, int limit);
    @Query("SELECT * FROM Dbwords Limit :limit OFFSET :offset")
    List<Dbwords> wordsForListLimit( int limit, int offset);
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
