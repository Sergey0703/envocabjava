package com.step.envocab;

import androidx.constraintlayout.widget.Group;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GroupDao {
    //@Insert
   // void insertGroup(Dbgroups group);
//    @Query("SELECT `group` from Dbgroups ORDER BY id_group")
//    List<String> getGroupsForSpinner();

    @Query("SELECT dbgroups.`group` from Dbgroups INNER JOIN (SELECT id_group ,COUNT (*) description from dbgroupsandwords GROUP BY id_group) " +
            "AS sel ON dbgroups.id_group=sel.id_group WHERE sel.description>0 ORDER BY dbgroups.id_group ")
    List<String> getGroupsForSpinner();

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

}
