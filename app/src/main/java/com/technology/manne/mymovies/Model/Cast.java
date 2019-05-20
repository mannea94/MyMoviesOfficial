package com.technology.manne.mymovies.Model;

import java.io.Serializable;

/**
 * Created by manne on 18.2.2018.
 */

public class Cast implements Serializable {
    public String name;
    public String profile_path;

    public Cast(){

    }

    public String getName() {
        return name;
    }

    public String getProfile_path() {
        return profile_path;
    }
}
