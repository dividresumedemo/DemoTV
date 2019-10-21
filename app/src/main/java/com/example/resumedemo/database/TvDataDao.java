package com.example.resumedemo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.resumedemo.data.TvData;

import java.util.List;

@Dao
public interface TvDataDao {
    @Query("select * from tvdata")
    List<TvData> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TvData data);

    @Query("select * from tvdata where drama_id = :drama_id")
    TvData get(String drama_id);

    @Query("SELECT * FROM tvdata WHERE name LIKE :queryText")
    LiveData<List<TvData>> getDataQueryList(String queryText);
}
