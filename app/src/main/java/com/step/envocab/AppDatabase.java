package com.step.envocab;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Dbwords.class, Dbgroups.class, Dbgroupsandwords.class,
        Dbexercises.class, Dbcounts.class, Dbsample.class, Dblangs.class, Dbscore.class, DbPref.class},
        version = 15)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract WordDao wordDao();
    public abstract GroupDao groupDao();
    public abstract GroupsAndWordsDao groupsAndWordsDao();
    public abstract ExerciseDao exerciseDao();

    public abstract CountDao countDao();
    public abstract SampleDao sampleDao();
    public abstract LangDao langDao();
    public abstract ScoreDao scoreDao();
    public abstract PrefDao PrefDao();
    private static volatile AppDatabase INSTANCE;
//    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            database.execSQL("CREATE TABLE `Fruit` (`id` INTEGER, "
//                    + "`name` TEXT, PRIMARY KEY(`id`))");
//        }
//    };
//
    //Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "dictdb").addMigrations(MIGRATION_1_2).build();

    static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "dictdb")
                            .createFromAsset("dictdb")
                            .fallbackToDestructiveMigration()
                            .build();
                    //System.out.println("make INSTANCE");
                }
            }
        }
        return INSTANCE;
    }
}
/*
@Database(entities = {Word.class},version = 2,autoMigrations = {@AutoMigration(from = 1, to = 2)})
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract WordDao wordDao();
    private static volatile AppDatabase INSTANCE;
    static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "wordsdb").build();
                    System.out.println("make INSTANCE");
                }
            }
        }
        return INSTANCE;
    }
}
*/