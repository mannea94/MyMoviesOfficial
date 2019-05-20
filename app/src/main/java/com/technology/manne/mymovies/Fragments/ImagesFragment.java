package com.technology.manne.mymovies.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.technology.manne.mymovies.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by manne on 22.12.2017.
 */

public class ImagesFragment extends Fragment {
    public Unbinder unbinder;
    @BindView(R.id.imageFragment)
    ImageView imageFragment;


    public static ImagesFragment newInstance(String imageUrl){
        Bundle args = new Bundle();
        args.putString("imageUrl", imageUrl);
        ImagesFragment fragment = new ImagesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_images, null);
        unbinder= ButterKnife.bind(this, view);

            if (getArguments() != null && getArguments().getString("imageUrl") != null) {
                Picasso.with(getActivity())
                        .load("https://image.tmdb.org/t/p/w500"+getArguments().getString("imageUrl"))
                        .centerInside()
                        .fit()
                        .into(imageFragment);
            }


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
