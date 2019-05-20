package com.technology.manne.mymovies.Activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.technology.manne.mymovies.Adapters.FragmentAdapterImages;
import com.technology.manne.mymovies.Model.MyMovies;
import com.technology.manne.mymovies.Model.Posters;
import com.technology.manne.mymovies.PreferenceManager;
import com.technology.manne.mymovies.R;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentActivity extends AppCompatActivity {

    @BindView(R.id.pagerImages)
    ViewPager pager;
    @BindView(R.id.totalImages)
    TextView totalImages;
    MyMovies myMovies;
    int total=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image);
        setContentView(R.layout.activity_fragment);
        ButterKnife.bind(this);
        myMovies=new MyMovies();
        myMovies =PreferenceManager.getMovieImage(this);
        total=myMovies.posters.size();
        if(total!=1) {
            totalImages.setText(total + " posters");
        }
        else{
            totalImages.setText(total + " poster");
        }

        setUpViewPager(pager);


    }

    public void setUpViewPager(ViewPager upViewPager){
        FragmentAdapterImages adapter = new FragmentAdapterImages(this.getSupportFragmentManager(), getList());
        pager.setAdapter(adapter);
    }

    ArrayList <Posters> getList(){
        return myMovies.posters;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
