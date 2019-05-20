package com.technology.manne.mymovies.Activities;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import com.technology.manne.mymovies.Adapters.AdapterWatchlist;
import com.technology.manne.mymovies.Api.RestApi;
import com.technology.manne.mymovies.Listener.OnRowClickListener;
import com.technology.manne.mymovies.Model.MovieModel;
import com.technology.manne.mymovies.Model.MyMovies;
import com.technology.manne.mymovies.PreferenceManager;
import com.technology.manne.mymovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchlistActivity extends AppCompatActivity {
    @BindView(R.id.recyclerViewWatchlist)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    RestApi api;
    AdapterWatchlist adapter;
    MovieModel movieModel;
    String session_id;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_watchlist);
        ButterKnife.bind(this);
        api=new RestApi(this);
        session_id= PreferenceManager.getSessionID(this);
        user_id=PreferenceManager.getUserID(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        refreshApi();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                refreshApi();
            }
        });

    }

    public void refreshApi(){
        api.checkInternet(new Runnable() {
            @Override
            public void run() {
                Call<MovieModel> call = api.getWatchlistMovies(user_id, session_id);
                call.enqueue(new Callback<MovieModel>() {
                    @Override
                    public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                        if (response.isSuccessful()) {
                            movieModel = response.body();
                            PreferenceManager.addMovies(movieModel, WatchlistActivity.this);
                            adapter = new AdapterWatchlist(WatchlistActivity.this, movieModel, new OnRowClickListener() {
                                @Override
                                public void onRowClick(MyMovies movies, int position) {
                                    Intent intent = new Intent(WatchlistActivity.this, MovieDetailsActivity.class);
                                    intent.putExtra("WATCHLIST", position);
                                    intent.putExtra("WATCHLIST_ID", movieModel.results.get(position).getId());
                                    startActivity(intent);
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
        });
    }
}
