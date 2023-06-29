package com.step.envocab;


import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class GroupWithWords {
    @Embedded
    public Dbgroups dbgroups;
    @Relation(
            parentColumn = "id_group",
            entityColumn = "id",
            associateBy = @Junction(Dbgroupsandwords.class)
    )
    public List<Dbwords> listDbWords;
}
