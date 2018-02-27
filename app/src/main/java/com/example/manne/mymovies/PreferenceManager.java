package com.example.manne.mymovies;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.manne.mymovies.Model.MovieModel;
import com.example.manne.mymovies.Model.RequestToken;
import com.example.manne.mymovies.Model.User;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by manne on 08.2.2018.
 */

public class PreferenceManager {
    private static final String USERID = "USERID";
    private static final String USER = "USER";

    //
    public static void addUser(User user, Context c) {
        Gson gson = new Gson();
        String mapString = gson.toJson(user);
        getPreferences(c).edit().putString(USER, mapString).apply();
    }

    public static User getUser(Context c) {
        return new Gson().fromJson(getPreferences(c).getString(USER, ""), User.class);
    }

    public static void removeUser(Context c) {
        getPreferences(c).edit().remove(USER).apply();
    }

    public static void setUserID(String firstName, Context c) {
        getPreferences(c).edit().putString(USERID, firstName).apply();
    }

    public static String getUserID(Context c) {
        return getPreferences(c).getString(USERID, "");
    }

    public static void removeUserID(Context c) {
        getPreferences(c).edit().remove(USERID).apply();
    }

    //
    public static void addMovies(MovieModel user, Context c){
        Gson gson = new Gson();
        String mapString = gson.toJson(user);
        getPreferences(c).edit().putString("myMovie", mapString).apply();
    }

    public static MovieModel getMovies(Context c){
        return new Gson().fromJson(getPreferences(c).getString("myMovie",null), MovieModel.class);
    }

    public static void addToken(String token, Context c){
        getPreferences(c).edit().putString("token", token).apply();
    }

    public static String getToken(Context c){
        return getPreferences(c).getString("token","");
    }

    public static void addSessionID(String sessionID, Context c){
        getPreferences(c).edit().putString("sessionID", sessionID).apply();
    }

    public static String getSessionID(Context c){
        return getPreferences(c).getString("sessionID","");
    }

    public static void addFlag(int sessionID, Context c){
        getPreferences(c).edit().putInt("flag", sessionID).apply();
    }

    public static int getFlag(Context c){
        return getPreferences(c).getInt("flag", 0);
    }


    private  static SharedPreferences getPreferences(Context c){
        return c.getApplicationContext().getSharedPreferences("MySharedPreferences", Activity.MODE_PRIVATE);
    }



}
