package com.example.envocab;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Word.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WordDao wordDao();

    private static volatile AppDatabase INSTANCE;

    static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "dbname").build();
                }
            }
        }


        return INSTANCE;
    }
}
