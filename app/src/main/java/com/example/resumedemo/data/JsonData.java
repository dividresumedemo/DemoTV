package com.example.resumedemo.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
public class JsonData {
    @SerializedName("data")
    private ArrayList<TvData> dataArray;
    public ArrayList<TvData> getDataArray() {
        return dataArray;
    }
}
