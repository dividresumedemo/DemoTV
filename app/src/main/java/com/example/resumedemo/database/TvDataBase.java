package com.example.resumedemo.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.resumedemo.data.TvData;

@Database(entities = {TvData.class}, version = 1)
public abstract class TvDataBase extends RoomDatabase {
    public abstract TvDataDao tvDataDao();
    private static volatile TvDataBase instance = null;

    public static TvDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                    TvDataBase.class,
                    "tv-database")
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                        }
                    })
                    .build();
        }
        return instance;
    }
}
