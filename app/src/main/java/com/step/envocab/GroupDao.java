package com.step.envocab;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GroupDao {
    //@Insert
   // void insertGroup(Dbgroups group);

    @Query("SELECT * from Dbgroups WHERE `group` LIKE :filter ORDER BY id_group")
    List<Dbgroups> getGroupsFiltered(String filter);

    @Query("SELECT id_group , COUNT(*) description from dbgroupsandwords GROUP BY id_group ")
    List<Dbgroups> getGroupsFiltered2();

    @Query("SELECT dbgroups.id_group, dbgroups.`group`, dbgroups.use_group, sel.description from dbgroups LEFT JOIN (SELECT id_group , COUNT(*) description from dbgroupsandwords GROUP BY id_group) AS sel ON dbgroups.id_group = sel.id_group WHERE dbgroups.`group` LIKE :filter")
    List<Dbgroups> getGroupsFiltered3(String filter);

//    @Query("update Dbgroups set `group`=:group, description = :descript,  native1 =:native1 where id_group = :id_group")
//    int upGroup(Integer id_group, String group, String descript, Boolean native1);
    @Query("update Dbgroups set `group`=:group where id_group = :id_group")
    int upGroup(Integer id_group, String group);

    @Query("update Dbgroups set use_group =:use_group where id_group = :id_group")
    int upGroupTrain(Integer id_group, int use_group);


    @Query("insert into Dbgroups (`group`,'description','native1',`use_group`) VALUES(:group,:descript,:native1,1)")
    Long insGroup( String group, String descript, Boolean native1);

    @Query("Select * FROM dbgroups WHERE id_group LIKE :id_group")
    Dbgroups findById(int id_group);

    @Query("Select * FROM dbgroups WHERE use_group >0")
    List<Dbgroups> findUseGroup();

//    //@Modifying
//    @Query("update Dbwords set word=:word, translate = :trans, transcript =:transcript, train1 =:train where id = :id")
//    int upWord(Integer id, String word, String trans, String transcript, Boolean train);
//
//    @Query("insert into Dbwords ('word','translate','transcript','train1') VALUES(:word,:trans,:transcript,:train)")
//    Long insWord( String word, String trans, String transcript, Boolean train);

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
