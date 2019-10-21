package com.example.resumedemo.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.resumedemo.database.TvDataBase;
import com.example.resumedemo.database.TvDataDao;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataRepository {
    private TvDataDao dao;
    private ExecutorService ioExecutor;
    private static DataRepository instance;

    public static DataRepository getInstance(Application application) {
        if (instance == null) {
            TvDataBase database = TvDataBase.getInstance(application);
            instance = new DataRepository(database.tvDataDao(), Executors.newSingleThreadExecutor());
        }
        return instance;
    }

    public DataRepository(TvDataDao dao, ExecutorService ioExecutor) {
        this.dao = dao;
        this.ioExecutor = ioExecutor;
    }

    public List<TvData> getAll() {
        try {
            return ioExecutor.submit(dao::getAll).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public TvData getItem(String drama_id) {
        return dao.get(drama_id);
    }

    public LiveData<List<TvData>> getDealsListInfo(String query){
        return dao.getDataQueryList(query);
    }
}
