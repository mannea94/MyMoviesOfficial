package com.example.manne.mymovies.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by manne on 06.2.2018.
 */

public class MyMovies implements Serializable {
    public String vote_count;
    public String id;
    public String vote_average;
    public String title;
    public String poster_path;
    public String original_title;
    public String profile_path;
    public String name;
    public String overview;
    public String key;
    public String job;
    public String department;
    public String rating;
    public String media_type;
    public String media_id;
    public String release_date;
    public transient boolean favorite;
    public transient boolean rated;
    public transient boolean watchlist;
    public ArrayList<MyMovies> known_for = new ArrayList<>();
    public ArrayList<Cast> cast = new ArrayList<>();
    public ArrayList<Crew> crew = new ArrayList<>();
    public MyMovies(){

    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getName() {
        return name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public ArrayList<MyMovies> getKnown_for() {
        return known_for;
    }

    public ArrayList<Cast> getCast() {
        return cast;
    }

    public ArrayList<Crew> getCrew() {
        return crew;
    }

    public String getKey() {
        return key;
    }

    public String getJob() {
        return job;
    }

    public String getDepartment() {
        return department;
    }

    public String getRating() {
        return rating;
    }

    public String getMedia_id() {
        return media_id;
    }

    public String getMedia_type() {
        return media_type;
    }

    public String getRelease_date() {
        return release_date;
    }

    public boolean isWatchlist() {
        return watchlist;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public boolean isRated() {
        return rated;
    }
}
