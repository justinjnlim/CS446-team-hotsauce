package com.hotsauce.meem.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MemeDao {

    @Insert
    void insert(Meme meme);

    @Query("DELETE FROM meme_table")
    void deleteAll();

    @Delete
    void delete(Meme meme);

    @Query("SELECT * from meme_table ORDER BY id ASC")
    LiveData<List<Meme>> getAllMemes();

    @Insert
    void insert(MemeTemplate memeTemplate);

    @Delete
    void delete(MemeTemplate memeTemplate);

    @Query("SELECT * from template_table ORDER BY id ASC")
    LiveData<List<MemeTemplate>> getAllMemeTemplates();
}
