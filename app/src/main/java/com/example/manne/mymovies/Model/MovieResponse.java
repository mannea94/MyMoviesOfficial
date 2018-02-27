package com.example.manne.mymovies.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by manne on 22.2.2018.
 */

public class MovieResponse {
    @SerializedName("media_type")
    public String media_type;
    @SerializedName("media_id")
    public int media_id;
    @SerializedName("favorite")
    public boolean favorite;
    @SerializedName("watchlist")
    public boolean watchlist;
    @SerializedName("value")
    public double value;

    public MovieResponse(){

    }
}
