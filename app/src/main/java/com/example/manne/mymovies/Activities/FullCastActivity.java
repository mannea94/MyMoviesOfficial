package com.example.manne.mymovies.Activities;

import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manne.mymovies.Adapters.AdapterFullCast;
import com.example.manne.mymovies.Api.RestApi;
import com.example.manne.mymovies.Model.MovieModel;
import com.example.manne.mymovies.Model.MyMovies;
import com.example.manne.mymovies.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullCastActivity extends AppCompatActivity {

//    @BindView(R.id.imageAkter)
//    ImageView imageView;
//    @BindView(R.id.textAkter)
//    TextView textView;
    @BindView(R.id.recyclerViewFullCast)
    RecyclerView recyclerView;
    MovieModel myMovies;
    RestApi restApi;
    AdapterFullCast adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_cast);
        ButterKnife.bind(this);
        myMovies=new MovieModel();
        restApi=new RestApi(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        Intent intent = getIntent();
        Call<MovieModel> call = restApi.getMovieCredits(intent.getStringExtra("EXTRA_CAST"));
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if(response.isSuccessful()) {
                    myMovies=response.body();
                    adapter=new AdapterFullCast(FullCastActivity.this, myMovies.cast);
                    recyclerView.setAdapter(adapter);
//                    for (int i = 0; i < myMovies.cast.size(); i++) {
//                        Picasso.with(FullCastActivity.this)
//                                .load("https://image.tmdb.org/t/p/w500/"+myMovies.cast.get(i).getProfile_path())
//                                .into(imageView);
//                        textView.setText(myMovies.cast.get(i).getName());
//                    }
                }
                else{
                    Toast.makeText(FullCastActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                Toast.makeText(FullCastActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
