package com.technology.manne.mymovies.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.technology.manne.mymovies.Adapters.AdapterFullCrew;
import com.technology.manne.mymovies.Api.RestApi;
import com.technology.manne.mymovies.Listener.OnRowClickListener;
import com.technology.manne.mymovies.Model.MovieModel;
import com.technology.manne.mymovies.Model.MyMovies;
import com.technology.manne.mymovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullCrewActivity extends AppCompatActivity {


    @BindView(R.id.recyclerViewFullCast)
    RecyclerView recyclerView;
    MovieModel myMovies;
    RestApi restApi;
    AdapterFullCrew adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_cast);
        ButterKnife.bind(this);
        myMovies=new MovieModel();
        restApi=new RestApi(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        final Intent intent = getIntent();
        restApi.checkInternet(new Runnable() {
            @Override
            public void run() {
                Call<MovieModel> call = restApi.getMovieCredits(intent.getStringExtra("EXTRA_CREW"));
                call.enqueue(new Callback<MovieModel>() {
                    @Override
                    public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                        if(response.isSuccessful()) {
                            myMovies=response.body();
                            adapter=new AdapterFullCrew(FullCrewActivity.this, myMovies.crew, new OnRowClickListener() {
                                @Override
                                public void onRowClick(MyMovies movies, int position) {
                                    Intent intent = new Intent(FullCrewActivity.this, PeopleDetailsActivity.class);
                                    intent.putExtra("CREW_ID", myMovies.crew.get(position).getId());
                                    startActivity(intent);
                                }
                            });
                            recyclerView.setAdapter(adapter);

                        }
                        else{
                            Toast.makeText(FullCrewActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieModel> call, Throwable t) {
                        Toast.makeText(FullCrewActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}
