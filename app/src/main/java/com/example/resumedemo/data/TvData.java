package com.example.resumedemo.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class TvData {
    @SerializedName("drama_id")
    @PrimaryKey
    private int drama_id;

    @SerializedName("name")
    private String name;

    @SerializedName("total_views")
    private int total_views;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("thumb")
    private String thumb;

    @SerializedName("rating")
    private float rating;

    public int getDrama_id() {
        return drama_id;
    }

    public String getName() {
        return name;
    }

    public int getTotal_views() {
        return total_views;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getThumb() {
        return thumb;
    }

    public float getRating() {
        return rating;
    }

    public void setDrama_id(int drama_id) {
        this.drama_id = drama_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotal_views(int total_views) {
        this.total_views = total_views;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}