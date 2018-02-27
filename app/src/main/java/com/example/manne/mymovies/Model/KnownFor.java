package com.example.manne.mymovies.Model;

import java.io.Serializable;

/**
 * Created by manne on 12.2.2018.
 */

public class KnownFor implements Serializable {
    public String title;
    public String vote_average;
    public String poster_path;

    public String getTitle() {
        return title;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getPoster_path() {
        return poster_path;
    }
}
