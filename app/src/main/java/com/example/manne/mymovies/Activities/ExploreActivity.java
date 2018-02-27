package com.example.manne.mymovies.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.manne.mymovies.R;

import butterknife.ButterKnife;

public class ExploreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);
        ButterKnife.bind(this);
    }
}
