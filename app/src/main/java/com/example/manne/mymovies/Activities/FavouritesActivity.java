package com.example.manne.mymovies.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.manne.mymovies.Adapters.Adapter;
import com.example.manne.mymovies.Adapters.AdapterFavourite;
import com.example.manne.mymovies.Api.RestApi;
import com.example.manne.mymovies.Listener.OnRowClickListener;
import com.example.manne.mymovies.MainDrawerActivity;
import com.example.manne.mymovies.Model.MovieModel;
import com.example.manne.mymovies.Model.MovieResponse;
import com.example.manne.mymovies.Model.MyMovies;
import com.example.manne.mymovies.PreferenceManager;
import com.example.manne.mymovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouritesActivity extends AppCompatActivity {
    @BindView(R.id.recyclerViewFavourites)
    RecyclerView recyclerView;
    RestApi api;
    AdapterFavourite adapter;
    MovieModel movieModel;
    String session_id;
    String user_id;
    MovieResponse movieResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        ButterKnife.bind(this);
        api=new RestApi(this);
        movieResponse=new MovieResponse();
        session_id= PreferenceManager.getSessionID(this);
        user_id=PreferenceManager.getUserID(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        Call<MovieModel> call = api.getFavouriteMovies(user_id, session_id);
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.isSuccessful()) {
                    movieModel = response.body();
                    adapter = new AdapterFavourite(FavouritesActivity.this, movieModel, new OnRowClickListener() {
                        @Override
                        public void onRowClick(MyMovies movies, final int position) {


                        }

                    });
                    recyclerView.setAdapter(adapter);

                }
                else{

                }
            }


            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });
    }
}
