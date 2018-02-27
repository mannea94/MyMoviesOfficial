package com.example.manne.mymovies.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.manne.mymovies.Adapters.Adapter;
import com.example.manne.mymovies.Adapters.AdapterRated;
import com.example.manne.mymovies.Api.RestApi;
import com.example.manne.mymovies.Listener.OnRowClickListener;
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

public class RatedActivity extends AppCompatActivity {

    @BindView(R.id.recyclerViewRated)
    RecyclerView recyclerView;
    RestApi api;
    AdapterRated adapter;
    MovieModel movieModel;
    MyMovies myMovies;
    String session_id;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rated);
        ButterKnife.bind(this);
        api=new RestApi(this);
        session_id= PreferenceManager.getSessionID(this);
        user_id=PreferenceManager.getUserID(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        Call<MovieModel> call = api.getRatedMovies(user_id, session_id);
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.isSuccessful()) {
                    movieModel = response.body();
                    adapter = new AdapterRated(RatedActivity.this, movieModel, new OnRowClickListener() {
                        @Override
                        public void onRowClick(MyMovies movies, int position) {
//                            Call<MyMovies> call = api.deleteRatingMovie(movieModel.results.get(position).getId(), "application/json;charset=utf-8;", session_id);
//                            call.enqueue(new Callback<MyMovies>() {
//                                @Override
//                                public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
//                                    if(response.isSuccessful()){
//                                        Toast.makeText(RatedActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
//                                        recyclerView.setAdapter(adapter);
//                                        adapter.notifyDataSetChanged();
//                                    }
//                                    else{
//                                        Toast.makeText(RatedActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<MyMovies> call, Throwable t) {
//
//                                }
//                            });

                        }

                    });
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
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
