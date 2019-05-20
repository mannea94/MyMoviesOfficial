package com.technology.manne.mymovies.Listener;

import com.technology.manne.mymovies.Model.KnownFor;

/**
 * Created by manne on 02.3.2018.
 */

public interface OnClickListener {
    public void OnRowClick(KnownFor knownFor, int position);
}
