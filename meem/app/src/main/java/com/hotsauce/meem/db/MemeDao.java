package com.hotsauce.meem.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MemeDao {

    @Insert
    void insert(Meme meme);

    @Query("DELETE FROM meme_table")
    void deleteAll();

    @Query("SELECT * from meme_table ORDER BY id ASC")
    LiveData<List<Meme>> getAllMemes();
}
