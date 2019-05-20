package com.technology.manne.mymovies.Model;

import java.io.Serializable;

/**
 * Created by manne on 18.2.2018.
 */

public class User implements Serializable {
    public String username;
    public String password;
    public String name;
    public Avatar avatar;
    public String id;

    public User(){}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
