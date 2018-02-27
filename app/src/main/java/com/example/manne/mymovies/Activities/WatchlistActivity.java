package com.example.manne.mymovies.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.manne.mymovies.Adapters.Adapter;
import com.example.manne.mymovies.Adapters.AdapterWatchlist;
import com.example.manne.mymovies.Api.RestApi;
import com.example.manne.mymovies.Listener.OnRowClickListener;
import com.example.manne.mymovies.Model.MovieModel;
import com.example.manne.mymovies.Model.MyMovies;
import com.example.manne.mymovies.PreferenceManager;
import com.example.manne.mymovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchlistActivity extends AppCompatActivity {
    @BindView(R.id.recyclerViewWatchlist)
    RecyclerView recyclerView;
    RestApi api;
    AdapterWatchlist adapter;
    MovieModel movieModel;
    String session_id;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist);
        ButterKnife.bind(this);
        api=new RestApi(this);
        session_id= PreferenceManager.getSessionID(this);
        user_id=PreferenceManager.getUserID(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        Call<MovieModel> call = api.getWatchlistMovies(user_id, session_id);
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.isSuccessful()) {
                    movieModel = response.body();
                    adapter = new AdapterWatchlist(WatchlistActivity.this, movieModel, new OnRowClickListener() {
                        @Override
                        public void onRowClick(MyMovies movies, int position) {

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
