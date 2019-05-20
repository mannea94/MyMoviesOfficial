package com.technology.manne.mymovies.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by manne on 12.2.2018.
 */

public class People implements Serializable {
    public String popularity;
    public String id;
    public String profile_path;
    public String name;
    public String biography;
    public String birthday;
    public String deathday;
    public ArrayList<KnownFor> known_for = new ArrayList<>();

    public String getPopularity() {
        return popularity;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public ArrayList<KnownFor> getKnown_for() {
        return known_for;
    }

    public String getBiography() {
        return biography;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getDeathday() {
        return deathday;
    }
}
