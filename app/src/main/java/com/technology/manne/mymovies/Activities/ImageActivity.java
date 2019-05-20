package com.technology.manne.mymovies.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.technology.manne.mymovies.Model.MyMovies;
import com.technology.manne.mymovies.PreferenceManager;
import com.technology.manne.mymovies.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageActivity extends AppCompatActivity {
    @BindView(R.id.imageMovie)
    ImageView imageView;
    MyMovies myMovies;
    String image="";
    int pos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        myMovies=new MyMovies();
        myMovies= PreferenceManager.getMovieImage(this);
        Intent intent = getIntent();
        if(intent.hasExtra("EXTRA_IMAGE")){
          pos=intent.getIntExtra("EXTRA_IMAGE", 0);
          for(int i=0; i<myMovies.backdrops.size(); i++){
              if(i==pos){
                  image=myMovies.backdrops.get(i).file_path;
              }
          }

            Picasso.with(this)
                    .load("https://image.tmdb.org/t/p/w500"+image)
//                    .fit()
                    .into(imageView);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
