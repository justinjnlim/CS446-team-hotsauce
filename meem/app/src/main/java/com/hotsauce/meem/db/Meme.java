package com.hotsauce.meem.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "meme_table")
public class Meme {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    public Meme(@NonNull String id) {this.id = id;}

    public String getId(){ return this.id;}
}
