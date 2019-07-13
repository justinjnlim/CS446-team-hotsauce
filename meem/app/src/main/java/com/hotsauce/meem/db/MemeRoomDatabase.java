package com.hotsauce.meem.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Meme.class, MemeTemplate.class}, version = 2, exportSchema = true)
public abstract class MemeRoomDatabase extends RoomDatabase {

    public abstract MemeDao memeDao();

    private static volatile MemeRoomDatabase INSTANCE;

    public static MemeRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MemeRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MemeRoomDatabase.class, "meme_database")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `template_table` (`id` TEXT NOT NULL, `coord_string` TEXT NOT NULL, PRIMARY KEY(`id`))");
        }
    };
}
