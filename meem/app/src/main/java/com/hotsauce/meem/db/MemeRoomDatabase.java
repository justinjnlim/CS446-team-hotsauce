package com.hotsauce.meem.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Meme.class}, version = 1)
public abstract class MemeRoomDatabase extends RoomDatabase {

    public abstract MemeDao memeDao();

    private static volatile MemeRoomDatabase INSTANCE;

    public static MemeRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MemeRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MemeRoomDatabase.class, "meme_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
