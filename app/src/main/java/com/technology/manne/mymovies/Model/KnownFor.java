package com.technology.manne.mymovies.Model;

import java.io.Serializable;

/**
 * Created by manne on 12.2.2018.
 */

public class KnownFor implements Serializable {
    public String title;
    public String vote_average;
    public String poster_path;
    public String id;
    public String overview;
    public KnownFor(){

    }

    public KnownFor(String title, String vote_average, String poster_path, String overview, String id) {
        this.title = title;
        this.vote_average = vote_average;
        this.poster_path = poster_path;
        this.overview=overview;
        this.id=id;
    }

    public String getTitle() {
        return title;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getId() {
        return id;
    }

    public String getOverview() {
        return overview;
    }
}
