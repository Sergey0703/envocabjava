package com.step.envocab;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface GroupsAndWordsDao {

//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    insert(join: CourseWithInstructor)
//    @Transaction
//    @Query("SELECT * FROM Course")
//    fun getCourses(): List<CourseInstructorPair>
//    @Query("update Dbgroups set `group`=:group, description = :descript,  native1 =:native1 where id_group = :id_group")
//    int upGroup(Integer id_group, String group, String descript, Boolean native1);
//    @Query("insert into dbgroupsandwords (id,id_group) VALUES(:id,:id_group)")
//    Long insGroupAndWord( int id, int id_group);

    @Query("Delete from dbgroupsandwords Where id Like :id AND id_group Like:id_group")
    int del( int id, int id_group);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertWord(Dbwords word);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertGroup(Dbgroups group);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertGroupWithWord(Dbgroupsandwords dbgroupsandwords);

    @Transaction
    @Query("SELECT * FROM dbgroupsandwords")
    public List<GroupWithWords> getGroupWithWordsAll();

    @Transaction
    @Query("SELECT * FROM dbgroupsandwords WHERE id_group LIKE :id_group ")
    public List<GroupWithWords> getGroupWithWords(int id_group);

    @Transaction
    @Query("SELECT * FROM dbgroups  WHERE id_group LIKE :id_group ")
    public List<GroupWithWords> getGroupWithWords2(int id_group);

//    @Transaction
//    @Query("SELECT * FROM dbgroupsandwords WHERE id_group LIKE :id_group ")
//    public List<Dbwords> getGroupWithWords3(int id_group);

//    @Transaction
//    @Query("SELECT dbwords.id, dbwords.word, dbgroupsandwords.id_group FROM dbwords INNER JOIN dbgroupsandwords ON dbwords.id = dbgroupsandwords.id WHERE dbgroupsandwords.id_group LIKE:id_group")
//    public List<Dbwords> getGroupWithWords4(int id_group);

    @Transaction
    @Query("SELECT dbwords.id, dbgroupsandwords.id_group, dbwords.word, dbwords.translate FROM dbwords INNER JOIN dbgroupsandwords ON dbwords.id = dbgroupsandwords.id WHERE dbgroupsandwords.id_group LIKE:id_group ORDER BY dbwords.word")
    public List<GroupWithWords2> getGroupWithWords5(int id_group);

    @Transaction
    @Query("SELECT dbwords.id, sel.id_group, dbwords.word, dbwords.translate FROM dbwords LEFT JOIN (SELECT dbgroupsandwords.id, dbgroupsandwords.id_group FROM dbgroupsandwords WHERE  dbgroupsandwords.id_group LIKE:id_group) AS sel ON dbwords.id = sel.id WHERE dbwords.word LIKE:filter ORDER BY dbwords.word")
    public List<GroupWithWords2> getGroupWithWords6(int id_group, String filter);
    //public List<GroupWithWords2> getGroupWithWords6(int id_group);
    //AND dbgroupsandwords.id_group LIKE:id_group

}
