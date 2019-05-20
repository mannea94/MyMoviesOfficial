package com.technology.manne.mymovies.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by manne on 08.2.2018.
 */

public class MovieModel implements Serializable {
    public String release_date;
    public ArrayList<MyMovies> results = new ArrayList<>();
    public ArrayList<MyMovies> cast = new ArrayList<>();
    public ArrayList<MyMovies> crew = new ArrayList<>();
    public ArrayList<MyMovies> genres = new ArrayList<>();

    public String getRelease_date() {
        return release_date;
    }
}
