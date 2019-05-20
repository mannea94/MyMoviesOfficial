package com.technology.manne.mymovies.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.technology.manne.mymovies.Adapters.AdapterFullCast;
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
                Call<MovieModel> call = restApi.getMovieCredits(intent.getStringExtra("EXTRA_CAST"));
                call.enqueue(new Callback<MovieModel>() {
                    @Override
                    public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                        if(response.isSuccessful()) {
                            myMovies=response.body();
                            adapter=new AdapterFullCast(FullCastActivity.this, myMovies.cast, new OnRowClickListener() {
                                @Override
                                public void onRowClick(MyMovies movies, int position) {
                                    Intent intent = new Intent(FullCastActivity.this, PeopleDetailsActivity.class);
                                    intent.putExtra("CAST_ID", myMovies.cast.get(position).getId());
                                    startActivity(intent);
                                }
                            });
                            recyclerView.setAdapter(adapter);

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
        });


    }
}
