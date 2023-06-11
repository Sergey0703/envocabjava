package com.step.envocab;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Word.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract WordDao wordDao();
    private static volatile AppDatabase INSTANCE;
    static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "dictdb").build();
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