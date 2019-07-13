package com.hotsauce.meem.db;

import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.File;
import java.io.Serializable;

@Entity(tableName = "meme_table")
public class Meme implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    public Meme(@NonNull String id) {this.id = id;}

    public String getId(){ return this.id;}

    public String getFilepath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + id + ".png";
    }
}
