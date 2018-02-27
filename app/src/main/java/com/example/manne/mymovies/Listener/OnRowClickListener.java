package com.example.manne.mymovies.Listener;

import com.example.manne.mymovies.Model.MyMovies;

import butterknife.OnClick;

/**
 * Created by manne on 09.2.2018.
 */

public interface OnRowClickListener {
    public void onRowClick(MyMovies movies, int position);

}
