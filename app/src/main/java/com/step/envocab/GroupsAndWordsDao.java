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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertWord(Dbwords word);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertGroup(Dbgroups group);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertGroupWithWord(Dbgroupsandwords dbgroupsandwords);

    @Transaction
    @Query("SELECT * FROM dbgroups")
    public List<GroupWithWords> getGroupWithWords();

}
