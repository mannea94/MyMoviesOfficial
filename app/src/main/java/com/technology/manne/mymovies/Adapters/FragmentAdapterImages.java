package com.technology.manne.mymovies.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.technology.manne.mymovies.Fragments.ImagesFragment;
import com.technology.manne.mymovies.Model.Posters;

import java.util.ArrayList;

/**
 * Created by manne on 22.12.2017.
 */

public class FragmentAdapterImages extends FragmentPagerAdapter {
    public FragmentAdapterImages(FragmentManager fm, ArrayList<Posters> backdrops_) {
        super(fm);
        backdrops=backdrops_;
    }

    public ArrayList<Posters> backdrops = new ArrayList<>();

    @Override
    public Fragment getItem(int position) {
        return ImagesFragment.newInstance(backdrops.get(position).file_path);
    }

    @Override
    public int getCount() {
        return backdrops.size();
    }
}
