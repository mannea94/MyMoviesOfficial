package com.example.manne.mymovies.Activities;

import android.content.Intent;
import android.net.Uri;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manne.mymovies.Adapters.Adapter;
import com.example.manne.mymovies.Api.RestApi;
import com.example.manne.mymovies.Listener.OnRowClickListener;
import com.example.manne.mymovies.MainDrawerActivity;
import com.example.manne.mymovies.Model.MovieModel;
import com.example.manne.mymovies.Model.MovieResponse;
import com.example.manne.mymovies.Model.MyMovies;
import com.example.manne.mymovies.PreferenceManager;
import com.example.manne.mymovies.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {

    RestApi api;
    public MovieModel movieModel;
    @BindView(R.id.imageFavourite)
    ImageView favoriteImage;
    @BindView(R.id.movieName)
    TextView movieName;
    @BindView(R.id.movieWallpaper)
    ImageView movieWallpaper;
    @BindView(R.id.buttonFullCast)
    Button buttonCast;
    @BindView(R.id.descriptionText)
    TextView description;
    @BindView(R.id.buttonPlay)
    FrameLayout imagePlay;
    @BindView(R.id.director)
    TextView director;
    @BindView(R.id.writers)
    TextView writers;
    @BindView(R.id.stars)
    TextView stars;
    @BindView(R.id.ratingImage)
    ImageView ratingImage;
    @BindView(R.id.viewsImage)
    ImageView watchlistImage;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.yourVote)
    TextView yourVote;
    @BindView(R.id.genres)
    TextView genres;
    @BindView(R.id.releaseDate)
    TextView releaseDate;
    MovieResponse movieResponse;
    MyMovies myMovies;
    int pos=0;
    double value=0.5;
    String session_id;
    String user_id;
    String url="";
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        api=new RestApi(this);
        movieModel=new MovieModel();
        movieResponse=new MovieResponse();
        myMovies=new MyMovies();
        ratingBar.setVisibility(View.GONE);
        yourVote.setVisibility(View.GONE);
        final Intent intent = getIntent();
        user_id=PreferenceManager.getUserID(this);
        session_id=PreferenceManager.getSessionID(this);
        if(intent.hasExtra("EXTRA1")){
            id=intent.getStringExtra("EXTRA_ID");
            Call<MovieModel> call = api.getMovies();
            call.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if(response.isSuccessful()) {

                        movieModel = response.body();
                        pos=intent.getIntExtra("EXTRA1", 0);
                        Picasso.with(MovieDetailsActivity.this)
                                .load("https://image.tmdb.org/t/p/w500/"+movieModel.results.get(pos).getPoster_path())
                                .into(movieWallpaper);
                        movieName.setText(movieModel.results.get(pos).getOriginal_title());
                        description.setText(movieModel.results.get(pos).getOverview());

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                    }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();
                }
            });

            Call<MovieModel> callID = api.getMovieIDD(id);
            callID.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if(response.isSuccessful()){
                        movieModel=response.body();
                        String date = movieModel.release_date;
                        String date2 = date.substring(0,4);
                        String genre="";
                        for(int i=0; i<movieModel.genres.size(); i++){
                            genre+=movieModel.genres.get(i).getName()+" | ";
                        }
                        genres.setText(date2+" | "+genre);
                    }
                    else{

                    }

                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {

                }
            });

            Call <MovieModel> call2 = api.getMovieID(id);
            call2.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if(response.isSuccessful()) {
                        movieModel = response.body();
                        if(movieModel.results.size()!=0) {
                            url = movieModel.results.get(0).getKey();
                        }
                        else{
                            url="";
                        }
                        imagePlay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+url));
                                startActivity(browserIntent);
                            }
                        });

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();

                }
            });

            Call<MovieModel> call3 = api.getMovieCredits(id);
            call3.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        String writer = "";
                        for (int i = 0; i < movieModel.crew.size(); i++) {
                            if (movieModel.crew.get(i).getJob().equals("Director")) {
                                director.setText(movieModel.crew.get(i).getName());
                            }
                            if (movieModel.crew.get(i).getDepartment().equals("Writing")) {
                                writer += movieModel.crew.get(i).getName() + ", ";
                            }
                        }
                        writers.setText(writer);
                        stars.setText(movieModel.cast.get(0).name + ", " + movieModel.cast.get(1).name);
                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();
                }
            });

            buttonCast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(MovieDetailsActivity.this, FullCastActivity.class);
                    intent1.putExtra("EXTRA_CAST", id);
                    startActivity(intent1);
                }
            });

            Call<MovieModel> call4 = api.getFavouriteMovies(user_id, session_id);
            call4.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                favoriteImage.setImageResource(R.mipmap.favourites_full_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });


            favoriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieResponse.media_type="movie";
                    movieResponse.media_id=Integer.valueOf(id);
                    movieResponse.favorite=true;
                    Call<MyMovies> call5 = api.postFavouriteMovie(session_id,"application/json;charset=utf-8;", movieResponse);
                    call5.enqueue(new Callback<MyMovies>() {
                        @Override
                        public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                            if(response.isSuccessful()){
                                favoriteImage.setImageResource(R.mipmap.favourites_full_hdpi);
                                Toast.makeText(MovieDetailsActivity.this, "Added to favourites", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MyMovies> call, Throwable t) {
                            Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            favoriteImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    movieResponse.media_type="movie";
                    movieResponse.media_id=Integer.valueOf(id);
                    movieResponse.favorite=false;
                    Call<MyMovies> call5 = api.postFavouriteMovie(session_id,"application/json;charset=utf-8;", movieResponse);
                    call5.enqueue(new Callback<MyMovies>() {
                        @Override
                        public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                            if(response.isSuccessful()){
                                favoriteImage.setImageResource(R.mipmap.favourites_empty_hdpi);
                                Toast.makeText(MovieDetailsActivity.this, "Deleted from favourites", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MyMovies> call, Throwable t) {
                            Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return true;
                }
            });

            Call<MovieModel> call6 = api.getWatchlistMovies(user_id, session_id);
            call6.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                watchlistImage.setImageResource(R.mipmap.watchlist_remove_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });

            watchlistImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieResponse.media_type="movie";
                    movieResponse.media_id=Integer.valueOf(id);
                    movieResponse.watchlist=true;
                    Call<MyMovies> call5 = api.postWatchlistMovie(session_id,"application/json;charset=utf-8;", movieResponse);
                    call5.enqueue(new Callback<MyMovies>() {
                        @Override
                        public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                            if(response.isSuccessful()){
                                watchlistImage.setImageResource(R.mipmap.watchlist_remove_hdpi);
                                Toast.makeText(MovieDetailsActivity.this, "Added to watchlist", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MyMovies> call, Throwable t) {
                            Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            watchlistImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    movieResponse.media_type="movie";
                    movieResponse.media_id=Integer.valueOf(id);
                    movieResponse.watchlist=false;
                    Call<MyMovies> call5 = api.postWatchlistMovie(session_id,"application/json;charset=utf-8;", movieResponse);
                    call5.enqueue(new Callback<MyMovies>() {
                        @Override
                        public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                            if(response.isSuccessful()){
                                watchlistImage.setImageResource(R.mipmap.watchlist_add_hdpi);
                                Toast.makeText(MovieDetailsActivity.this, "Deleted from watchlist", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MyMovies> call, Throwable t) {
                            Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return true;
                }
            });

            Call<MovieModel> call7 = api.getRatedMovies(user_id, session_id);
            call7.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                ratingImage.setImageResource(R.drawable.full_star_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });

            ratingImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ratingBar.setVisibility(View.VISIBLE);
                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(final RatingBar ratingBar, float v, boolean b) {
                            yourVote.setVisibility(View.VISIBLE);
                            yourVote.setText("Your vote is: " + v*2);
                            value=v*2;
                            movieResponse.value=value;
                            Call<MyMovies> call8 = api.postRatingMovie(id, "application/json;charset=utf-8;", movieResponse, session_id);
                            call8.enqueue(new Callback<MyMovies>() {
                                @Override
                                public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                                    if(response.isSuccessful()){
                                        Toast.makeText(MovieDetailsActivity.this, "Added to rated", Toast.LENGTH_SHORT).show();
                                        ratingImage.setImageResource(R.drawable.full_star_hdpi);
                                    }
                                    else{

                                    }
                                }

                                @Override
                                public void onFailure(Call<MyMovies> call, Throwable t) {

                                }
                            });
                        }
                    });
                }
            });

            ratingImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Call<MyMovies>call = api.deleteRatingMovie(id, "application/json;charset=utf-8;", session_id);
                    call.enqueue(new Callback<MyMovies>() {
                        @Override
                        public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                            ratingImage.setImageResource(R.mipmap.rate_empty_hdpi);
                            Toast.makeText(MovieDetailsActivity.this, "Deleted from rated", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<MyMovies> call, Throwable t) {

                        }
                    });
                    return true;
                }
            });

        }



        if(intent.hasExtra("SEARCH_1")){
            movieModel=PreferenceManager.getMovies(this);
            pos=intent.getIntExtra("SEARCH_POS1", 0);
            Picasso.with(MovieDetailsActivity.this)
                    .load("https://image.tmdb.org/t/p/w500/"+movieModel.results.get(pos).getPoster_path())
                    .into(movieWallpaper);
            movieName.setText(movieModel.results.get(pos).getOriginal_title());
            description.setText(movieModel.results.get(pos).getOverview());
            id=intent.getStringExtra("SEARCH_ID1");

            Call<MovieModel> callID = api.getMovieIDD(id);
            callID.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if(response.isSuccessful()){
                        movieModel=response.body();
                        String date = movieModel.release_date;
                        String date2 = date.substring(0,4);
                        String genre="";
                        for(int i=0; i<movieModel.genres.size(); i++){
                            genre+=movieModel.genres.get(i).getName()+" | ";
                        }
                        genres.setText(date2+" | "+genre);
                    }
                    else{

                    }

                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {

                }
            });

            Call <MovieModel> call2 = api.getMovieID(id);
            call2.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if(response.isSuccessful()) {

                        movieModel = response.body();
                        if(movieModel.results.size()!=0) {
                            url = movieModel.results.get(0).getKey();
                        }
                        else{
                            url="";
                        }
                        imagePlay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+url));
                                startActivity(browserIntent);
                            }
                        });

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();

                }
            });

            Call<MovieModel> call3 = api.getMovieCredits(id);
            call3.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        String writer = "";
                        for (int i = 0; i < movieModel.crew.size(); i++) {
                            if (movieModel.crew.get(i).getJob().equals("Director")) {
                                director.setText(movieModel.crew.get(i).getName());
                            }
                            if (movieModel.crew.get(i).getDepartment().equals("Writing")) {
                                writer += movieModel.crew.get(i).getName() + ", ";
                            }
                        }
                        writers.setText(writer);
                        stars.setText(movieModel.cast.get(0).name + ", " + movieModel.cast.get(1).name);
                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();
                }
            });

            buttonCast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(MovieDetailsActivity.this, FullCastActivity.class);
                    intent1.putExtra("EXTRA_CAST", id);
                    startActivity(intent1);
                }
            });

            Call<MovieModel> call4 = api.getFavouriteMovies(user_id, session_id);
            call4.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                favoriteImage.setImageResource(R.mipmap.favourites_full_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });


            favoriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieResponse.media_type="movie";
                    movieResponse.media_id=Integer.valueOf(id);
                    movieResponse.favorite=true;
                    Call<MyMovies> call5 = api.postFavouriteMovie(session_id,"application/json;charset=utf-8;", movieResponse);
                    call5.enqueue(new Callback<MyMovies>() {
                        @Override
                        public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                            if(response.isSuccessful()){
                                favoriteImage.setImageResource(R.mipmap.favourites_full_hdpi);
                                Toast.makeText(MovieDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MyMovies> call, Throwable t) {
                            Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            favoriteImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    movieResponse.media_type="movie";
                    movieResponse.media_id=Integer.valueOf(id);
                    movieResponse.favorite=false;
                    Call<MyMovies> call5 = api.postFavouriteMovie(session_id,"application/json;charset=utf-8;", movieResponse);
                    call5.enqueue(new Callback<MyMovies>() {
                        @Override
                        public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                            if(response.isSuccessful()){
                                favoriteImage.setImageResource(R.mipmap.favourites_empty_hdpi);
                                Toast.makeText(MovieDetailsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MyMovies> call, Throwable t) {
                            Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return true;
                }
            });


            Call<MovieModel> call6 = api.getWatchlistMovies(user_id, session_id);
            call6.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                watchlistImage.setImageResource(R.mipmap.watchlist_remove_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });

            watchlistImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieResponse.media_type="movie";
                    movieResponse.media_id=Integer.valueOf(id);
                    movieResponse.watchlist=true;
                    Call<MyMovies> call5 = api.postWatchlistMovie(session_id,"application/json;charset=utf-8;", movieResponse);
                    call5.enqueue(new Callback<MyMovies>() {
                        @Override
                        public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                            if(response.isSuccessful()){
                                watchlistImage.setImageResource(R.mipmap.watchlist_remove_hdpi);
                                Toast.makeText(MovieDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MyMovies> call, Throwable t) {
                            Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            watchlistImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    movieResponse.media_type="movie";
                    movieResponse.media_id=Integer.valueOf(id);
                    movieResponse.watchlist=false;
                    Call<MyMovies> call5 = api.postWatchlistMovie(session_id,"application/json;charset=utf-8;", movieResponse);
                    call5.enqueue(new Callback<MyMovies>() {
                        @Override
                        public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                            if(response.isSuccessful()){
                                watchlistImage.setImageResource(R.mipmap.watchlist_add_hdpi);
                                Toast.makeText(MovieDetailsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MyMovies> call, Throwable t) {
                            Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return true;
                }
            });

            Call<MovieModel> call7 = api.getRatedMovies(user_id, session_id);
            call7.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                ratingImage.setImageResource(R.drawable.full_star_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });

            ratingImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ratingBar.setVisibility(View.VISIBLE);
                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                            yourVote.setVisibility(View.VISIBLE);
                            yourVote.setText("Your vote is: " + v*2);
                            value=v*2;
                            movieResponse.value=value;
                            Call<MyMovies> call8 = api.postRatingMovie(id, "application/json;charset=utf-8;", movieResponse, session_id);
                            call8.enqueue(new Callback<MyMovies>() {
                                @Override
                                public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                                    if(response.isSuccessful()){
                                        Toast.makeText(MovieDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        ratingImage.setImageResource(R.drawable.full_star_hdpi);
                                    }
                                    else{
                                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyMovies> call, Throwable t) {
                                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });

            ratingImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Call<MyMovies>call = api.deleteRatingMovie(id, "application/json;charset=utf-8;", session_id);
                    call.enqueue(new Callback<MyMovies>() {
                        @Override
                        public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                            ratingImage.setImageResource(R.mipmap.rate_empty_hdpi);
                            Toast.makeText(MovieDetailsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<MyMovies> call, Throwable t) {

                        }
                    });
                    return true;
                }
            });

        }


        if(intent.hasExtra("EXTRA2")){
            api=new RestApi(this);
            id=intent.getStringExtra("EXTRA_ID2");
            Call<MovieModel> call = api.getTopRated();
            call.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if(response.isSuccessful()) {
                        movieModel = response.body();

                        pos=intent.getIntExtra("EXTRA2", 0);
                        Picasso.with(MovieDetailsActivity.this)
                                .load("https://image.tmdb.org/t/p/w500/"+movieModel.results.get(pos).getPoster_path())
                                .into(movieWallpaper);
                        movieName.setText(movieModel.results.get(pos).getOriginal_title());
                        description.setText(movieModel.results.get(pos).getOverview());
                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();
                }
            });

            Call<MovieModel> callID = api.getMovieIDD(id);
            callID.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if(response.isSuccessful()){
                        movieModel=response.body();
                        String date = movieModel.release_date;
                        String date2 = date.substring(0,4);
                        String genre="";
                        for(int i=0; i<movieModel.genres.size(); i++){
                            genre+=movieModel.genres.get(i).getName()+" | ";
                        }
                        genres.setText(date2+" | "+genre);
                    }
                    else{

                    }

                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {

                }
            });

            Call <MovieModel> call2 = api.getMovieID(id);
            call2.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if(response.isSuccessful()) {

                        movieModel = response.body();
                        if(movieModel.results.size()!=0) {
                            url = movieModel.results.get(0).getKey();
                        }
                        else{
                            url="";
                        }
                        imagePlay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+url));
                                startActivity(browserIntent);
                            }
                        });

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();

                }
            });

            Call<MovieModel> call3 = api.getMovieCredits(id);
            call3.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        String writer = "";
                        for (int i = 0; i < movieModel.crew.size(); i++) {
                            if (movieModel.crew.get(i).getJob().equals("Director")) {
                                director.setText(movieModel.crew.get(i).getName());
                            }
                            if (movieModel.crew.get(i).getDepartment().equals("Writing")) {
                                writer += movieModel.crew.get(i).getName() + ", ";
                            }
                        }
                        writers.setText(writer);
                        stars.setText(movieModel.cast.get(0).name + ", " + movieModel.cast.get(1).name);
                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();
                }
            });

            buttonCast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(MovieDetailsActivity.this, FullCastActivity.class);
                    intent1.putExtra("EXTRA_CAST", id);
                    startActivity(intent1);
                }
            });

            Call<MovieModel> call4 = api.getFavouriteMovies(user_id, session_id);
            call4.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                favoriteImage.setImageResource(R.mipmap.favourites_full_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });


            favoriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieResponse.media_type="movie";
                    movieResponse.media_id=Integer.valueOf(id);
                    movieResponse.favorite=true;
                    Call<MyMovies> call5 = api.postFavouriteMovie(session_id,"application/json;charset=utf-8;", movieResponse);
                    call5.enqueue(new Callback<MyMovies>() {
                        @Override
                        public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                            if(response.isSuccessful()){
                                favoriteImage.setImageResource(R.mipmap.favourites_full_hdpi);
                                Toast.makeText(MovieDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MyMovies> call, Throwable t) {
                            Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            Call<MovieModel> call6 = api.getWatchlistMovies(user_id, session_id);
            call6.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                watchlistImage.setImageResource(R.mipmap.watchlist_remove_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });

            watchlistImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieResponse.media_type="movie";
                    movieResponse.media_id=Integer.valueOf(id);
                    movieResponse.watchlist=true;
                    Call<MyMovies> call5 = api.postWatchlistMovie(session_id,"application/json;charset=utf-8;", movieResponse);
                    call5.enqueue(new Callback<MyMovies>() {
                        @Override
                        public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                            if(response.isSuccessful()){
                                watchlistImage.setImageResource(R.mipmap.watchlist_remove_hdpi);
                                Toast.makeText(MovieDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MyMovies> call, Throwable t) {
                            Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            Call<MovieModel> call7 = api.getRatedMovies(user_id, session_id);
            call7.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                ratingImage.setImageResource(R.drawable.full_star_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });

            ratingImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ratingBar.setVisibility(View.VISIBLE);
                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                            yourVote.setVisibility(View.VISIBLE);
                            yourVote.setText("Your vote is: " + v*2);
                            value=v*2;
                            movieResponse.value=value;
                            Call<MyMovies> call8 = api.postRatingMovie(id, "application/json;charset=utf-8;", movieResponse, session_id);
                            call8.enqueue(new Callback<MyMovies>() {
                                @Override
                                public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                                    if(response.isSuccessful()){
                                        Toast.makeText(MovieDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        ratingImage.setImageResource(R.drawable.full_star_hdpi);
                                    }
                                    else{

                                    }
                                }

                                @Override
                                public void onFailure(Call<MyMovies> call, Throwable t) {

                                }
                            });
                        }
                    });
                }
            });
        }

        if(intent.hasExtra("SEARCH_2")){
            movieModel=PreferenceManager.getMovies(this);
            pos=intent.getIntExtra("SEARCH_POS2", 0);
            Picasso.with(MovieDetailsActivity.this)
                    .load("https://image.tmdb.org/t/p/w500/"+movieModel.results.get(pos).getPoster_path())
                    .into(movieWallpaper);
            movieName.setText(movieModel.results.get(pos).getOriginal_title());
            description.setText(movieModel.results.get(pos).getOverview());
            id=intent.getStringExtra("SEARCH_ID2");

            Call<MovieModel> callID = api.getMovieIDD(id);
            callID.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if(response.isSuccessful()){
                        movieModel=response.body();
                        String date = movieModel.release_date;
                        String date2 = date.substring(0,4);
                        String genre="";
                        for(int i=0; i<movieModel.genres.size(); i++){
                            genre+=movieModel.genres.get(i).getName()+" | ";
                        }
                        genres.setText(date2+" | "+genre);
                    }
                    else{

                    }

                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {

                }
            });

            Call <MovieModel> call2 = api.getMovieID(id);
            call2.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if(response.isSuccessful()) {

                        movieModel = response.body();
                        if(movieModel.results.size()!=0) {
                            url = movieModel.results.get(0).getKey();
                        }
                        else{
                            url="";
                        }
                        imagePlay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+url));
                                startActivity(browserIntent);
                            }
                        });

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();

                }
            });

            Call<MovieModel> call3 = api.getMovieCredits(id);
            call3.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        String writer = "";
                        for (int i = 0; i < movieModel.crew.size(); i++) {
                            if (movieModel.crew.get(i).getJob().equals("Director")) {
                                director.setText(movieModel.crew.get(i).getName());
                            }
                            if (movieModel.crew.get(i).getDepartment().equals("Writing")) {
                                writer += movieModel.crew.get(i).getName() + ", ";
                            }
                        }
                        writers.setText(writer);
                        stars.setText(movieModel.cast.get(0).name + ", " + movieModel.cast.get(1).name);
                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();
                }
            });

            buttonCast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(MovieDetailsActivity.this, FullCastActivity.class);
                    intent1.putExtra("EXTRA_CAST", id);
                    startActivity(intent1);
                }
            });

            Call<MovieModel> call4 = api.getFavouriteMovies(user_id, session_id);
            call4.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                favoriteImage.setImageResource(R.mipmap.favourites_full_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });


            favoriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieResponse.media_type="movie";
                    movieResponse.media_id=Integer.valueOf(id);
                    movieResponse.favorite=true;
                    Call<MyMovies> call5 = api.postFavouriteMovie(session_id,"application/json;charset=utf-8;", movieResponse);
                    call5.enqueue(new Callback<MyMovies>() {
                        @Override
                        public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                            if(response.isSuccessful()){
                                favoriteImage.setImageResource(R.mipmap.favourites_full_hdpi);
                                Toast.makeText(MovieDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MyMovies> call, Throwable t) {
                            Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            Call<MovieModel> call6 = api.getWatchlistMovies(user_id, session_id);
            call6.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                watchlistImage.setImageResource(R.mipmap.watchlist_remove_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });

            watchlistImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieResponse.media_type="movie";
                    movieResponse.media_id=Integer.valueOf(id);
                    movieResponse.watchlist=true;
                    Call<MyMovies> call5 = api.postWatchlistMovie(session_id,"application/json;charset=utf-8;", movieResponse);
                    call5.enqueue(new Callback<MyMovies>() {
                        @Override
                        public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                            if(response.isSuccessful()){
                                watchlistImage.setImageResource(R.mipmap.watchlist_remove_hdpi);
                                Toast.makeText(MovieDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MyMovies> call, Throwable t) {
                            Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            Call<MovieModel> call7 = api.getRatedMovies(user_id, session_id);
            call7.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                ratingImage.setImageResource(R.drawable.full_star_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });

            ratingImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ratingBar.setVisibility(View.VISIBLE);
                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                            yourVote.setVisibility(View.VISIBLE);
                            yourVote.setText("Your vote is: " + v*2);
                            value=v*2;
                            movieResponse.value=value;
                            Call<MyMovies> call8 = api.postRatingMovie(id, "application/json;charset=utf-8;", movieResponse, session_id);
                            call8.enqueue(new Callback<MyMovies>() {
                                @Override
                                public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                                    if(response.isSuccessful()){
                                        Toast.makeText(MovieDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        ratingImage.setImageResource(R.drawable.full_star_hdpi);
                                    }
                                    else{

                                    }
                                }

                                @Override
                                public void onFailure(Call<MyMovies> call, Throwable t) {

                                }
                            });
                        }
                    });
                }
            });

        }

        if(intent.hasExtra("EXTRA_3")){
            api=new RestApi(this);
            id=intent.getStringExtra("EXTRA_ID3");
            pos=intent.getIntExtra("EXTRA3", 0);
            Call<MovieModel> call = api.getUpcoming();
            call.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if(response.isSuccessful()) {
                        movieModel = response.body();
                        Picasso.with(MovieDetailsActivity.this)
                                .load("https://image.tmdb.org/t/p/w500/"+movieModel.results.get(pos).getPoster_path())
                                .into(movieWallpaper);
                        movieName.setText(movieModel.results.get(pos).getOriginal_title());
                        description.setText(movieModel.results.get(pos).getOverview());
                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();
                }
            });

            Call<MovieModel> callID = api.getMovieIDD(id);
            callID.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if(response.isSuccessful()){
                        movieModel=response.body();
                        String date = movieModel.release_date;
                        String date2 = date.substring(0,4);
                        String genre="";
                        for(int i=0; i<movieModel.genres.size(); i++){
                            genre+=movieModel.genres.get(i).getName()+" | ";
                        }
                        genres.setText(date2+" | "+genre);
                    }
                    else{

                    }

                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {

                }
            });

            Call <MovieModel> call2 = api.getMovieID(id);
            call2.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if(response.isSuccessful()) {

                        movieModel = response.body();
                        if(movieModel.results.size()!=0) {
                            url = movieModel.results.get(0).getKey();
                        }
                        else{
                            url="";
                        }
                        imagePlay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+url));
                                startActivity(browserIntent);
                            }
                        });

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();

                }
            });

            Call<MovieModel> call3 = api.getMovieCredits(id);
            call3.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        String writer = "";
                        for (int i = 0; i < movieModel.crew.size(); i++) {
                            if (movieModel.crew.get(i).getJob().equals("Director")) {
                                director.setText(movieModel.crew.get(i).getName());
                            }
                            if (movieModel.crew.get(i).getDepartment().equals("Writing")) {
                                writer += movieModel.crew.get(i).getName() + ", ";
                            }
                        }
                        writers.setText(writer);
                        stars.setText(movieModel.cast.get(0).name + ", " + movieModel.cast.get(1).name);
                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();
                }
            });

            buttonCast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(MovieDetailsActivity.this, FullCastActivity.class);
                    intent1.putExtra("EXTRA_CAST", id);
                    startActivity(intent1);
                }
            });

            Call<MovieModel> call4 = api.getFavouriteMovies(user_id, session_id);
            call4.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                favoriteImage.setImageResource(R.mipmap.favourites_full_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });


            favoriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieResponse.media_type="movie";
                    movieResponse.media_id=Integer.valueOf(id);
                    movieResponse.favorite=true;
                    Call<MyMovies> call5 = api.postFavouriteMovie(session_id,"application/json;charset=utf-8;", movieResponse);
                    call5.enqueue(new Callback<MyMovies>() {
                        @Override
                        public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                            if(response.isSuccessful()){
                                favoriteImage.setImageResource(R.mipmap.favourites_full_hdpi);
                                Toast.makeText(MovieDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MyMovies> call, Throwable t) {
                            Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            Call<MovieModel> call6 = api.getWatchlistMovies(user_id, session_id);
            call6.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                watchlistImage.setImageResource(R.mipmap.watchlist_remove_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });

            watchlistImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieResponse.media_type="movie";
                    movieResponse.media_id=Integer.valueOf(id);
                    movieResponse.watchlist=true;
                    Call<MyMovies> call5 = api.postWatchlistMovie(session_id,"application/json;charset=utf-8;", movieResponse);
                    call5.enqueue(new Callback<MyMovies>() {
                        @Override
                        public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                            if(response.isSuccessful()){
                                watchlistImage.setImageResource(R.mipmap.watchlist_remove_hdpi);
                                Toast.makeText(MovieDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MyMovies> call, Throwable t) {
                            Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            Call<MovieModel> call7 = api.getRatedMovies(user_id, session_id);
            call7.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                ratingImage.setImageResource(R.drawable.full_star_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });

            ratingImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ratingBar.setVisibility(View.VISIBLE);
                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                            yourVote.setVisibility(View.VISIBLE);
                            yourVote.setText("Your vote is: " + v*2);
                            value=v*2;
                            movieResponse.value=value;
                            Call<MyMovies> call8 = api.postRatingMovie(id, "application/json;charset=utf-8;", movieResponse, session_id);
                            call8.enqueue(new Callback<MyMovies>() {
                                @Override
                                public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                                    if(response.isSuccessful()){
                                        Toast.makeText(MovieDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        ratingImage.setImageResource(R.drawable.full_star_hdpi);
                                    }
                                    else{

                                    }
                                }

                                @Override
                                public void onFailure(Call<MyMovies> call, Throwable t) {

                                }
                            });
                        }
                    });
                }
            });


        }

        if(intent.hasExtra("SEARCH_3")){
            movieModel=PreferenceManager.getMovies(this);
            pos=intent.getIntExtra("SEARCH_POS3", 0);
            Picasso.with(MovieDetailsActivity.this)
                    .load("https://image.tmdb.org/t/p/w500/"+movieModel.results.get(pos).getPoster_path())
                    .into(movieWallpaper);
            movieName.setText(movieModel.results.get(pos).getOriginal_title());
            description.setText(movieModel.results.get(pos).getOverview());
            id=intent.getStringExtra("SEARCH_ID3");

            Call<MovieModel> callID = api.getMovieIDD(id);
            callID.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if(response.isSuccessful()){
                        movieModel=response.body();
                        String date = movieModel.release_date;
                        String date2 = date.substring(0,4);
                        String genre="";
                        for(int i=0; i<movieModel.genres.size(); i++){
                            genre+=movieModel.genres.get(i).getName()+" | ";
                        }
                        genres.setText(date2+" | "+genre);
                    }
                    else{

                    }

                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {

                }
            });

            Call <MovieModel> call2 = api.getMovieID(id);
            call2.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if(response.isSuccessful()) {

                        movieModel = response.body();
                        if(movieModel.results.size()!=0) {
                            url = movieModel.results.get(0).getKey();
                        }
                        else{
                            url="";
                        }
                        imagePlay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+url));
                                startActivity(browserIntent);
                            }
                        });

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();

                }
            });

            Call<MovieModel> call3 = api.getMovieCredits(id);
            call3.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        String writer = "";
                        for (int i = 0; i < movieModel.crew.size(); i++) {
                            if (movieModel.crew.get(i).getJob().equals("Director")) {
                                director.setText(movieModel.crew.get(i).getName());
                            }
                            if (movieModel.crew.get(i).getDepartment().equals("Writing")) {
                                writer += movieModel.crew.get(i).getName() + ", ";
                            }
                        }
                        writers.setText(writer);
                        stars.setText(movieModel.cast.get(0).name + ", " + movieModel.cast.get(1).name);
                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();
                }
            });

            buttonCast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(MovieDetailsActivity.this, FullCastActivity.class);
                    intent1.putExtra("EXTRA_CAST", id);
                    startActivity(intent1);
                }
            });

            Call<MovieModel> call4 = api.getFavouriteMovies(user_id, session_id);
            call4.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                favoriteImage.setImageResource(R.mipmap.favourites_full_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });


            favoriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieResponse.media_type="movie";
                    movieResponse.media_id=Integer.valueOf(id);
                    movieResponse.favorite=true;
                    Call<MyMovies> call5 = api.postFavouriteMovie(session_id,"application/json;charset=utf-8;", movieResponse);
                    call5.enqueue(new Callback<MyMovies>() {
                        @Override
                        public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                            if(response.isSuccessful()){
                                favoriteImage.setImageResource(R.mipmap.favourites_full_hdpi);
                                Toast.makeText(MovieDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MyMovies> call, Throwable t) {
                            Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            Call<MovieModel> call6 = api.getWatchlistMovies(user_id, session_id);
            call6.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                watchlistImage.setImageResource(R.mipmap.watchlist_remove_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });

            watchlistImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieResponse.media_type="movie";
                    movieResponse.media_id=Integer.valueOf(id);
                    movieResponse.watchlist=true;
                    Call<MyMovies> call5 = api.postWatchlistMovie(session_id,"application/json;charset=utf-8;", movieResponse);
                    call5.enqueue(new Callback<MyMovies>() {
                        @Override
                        public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                            if(response.isSuccessful()){
                                watchlistImage.setImageResource(R.mipmap.watchlist_remove_hdpi);
                                Toast.makeText(MovieDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MyMovies> call, Throwable t) {
                            Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            Call<MovieModel> call7 = api.getRatedMovies(user_id, session_id);
            call7.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                ratingImage.setImageResource(R.drawable.full_star_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });

            ratingImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ratingBar.setVisibility(View.VISIBLE);
                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                            yourVote.setVisibility(View.VISIBLE);
                            yourVote.setText("Your vote is: " + v*2);
                            value=v*2;
                            movieResponse.value=value;
                            Call<MyMovies> call8 = api.postRatingMovie(id, "application/json;charset=utf-8;", movieResponse, session_id);
                            call8.enqueue(new Callback<MyMovies>() {
                                @Override
                                public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                                    if(response.isSuccessful()){
                                        Toast.makeText(MovieDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        ratingImage.setImageResource(R.drawable.full_star_hdpi);
                                    }
                                    else{

                                    }
                                }

                                @Override
                                public void onFailure(Call<MyMovies> call, Throwable t) {

                                }
                            });
                        }
                    });
                }
            });

        }

        if(intent.hasExtra("EXTRA_4")){
            api=new RestApi(this);
            id=intent.getStringExtra("EXTRA_ID4");
            pos=intent.getIntExtra("EXTRA4", 0);
            Call<MovieModel> call = api.getNowPlaying();
            call.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if(response.isSuccessful()) {
                        movieModel = response.body();
                        Picasso.with(MovieDetailsActivity.this)
                                .load("https://image.tmdb.org/t/p/w500/"+movieModel.results.get(pos).getPoster_path())
                                .into(movieWallpaper);
                        movieName.setText(movieModel.results.get(pos).getOriginal_title());
                        description.setText(movieModel.results.get(pos).getOverview());
                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();
                }
            });

            Call<MovieModel> callID = api.getMovieIDD(id);
            callID.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if(response.isSuccessful()){
                        movieModel=response.body();
                        String date = movieModel.release_date;
                        String date2 = date.substring(0,4);
                        String genre="";
                        for(int i=0; i<movieModel.genres.size(); i++){
                            genre+=movieModel.genres.get(i).getName()+" | ";
                        }
                        genres.setText(date2+" | "+genre);
                    }
                    else{

                    }

                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {

                }
            });

            Call <MovieModel> call2 = api.getMovieID(id);
            call2.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if(response.isSuccessful()) {

                        movieModel = response.body();
                        if(movieModel.results.size()!=0) {
                            url = movieModel.results.get(0).getKey();
                        }
                        else{
                            url="";
                        }
                        imagePlay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+url));
                                startActivity(browserIntent);
                            }
                        });

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();

                }
            });

            Call<MovieModel> call3 = api.getMovieCredits(id);
            call3.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        String writer = "";
                        for (int i = 0; i < movieModel.crew.size(); i++) {
                            if (movieModel.crew.get(i).getJob().equals("Director")) {
                                director.setText(movieModel.crew.get(i).getName());
                            }
                            if (movieModel.crew.get(i).getDepartment().equals("Writing")) {
                                writer += movieModel.crew.get(i).getName() + ", ";
                            }
                        }
                        writers.setText(writer);
                        stars.setText(movieModel.cast.get(0).name + ", " + movieModel.cast.get(1).name);
                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();
                }
            });

            buttonCast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(MovieDetailsActivity.this, FullCastActivity.class);
                    intent1.putExtra("EXTRA_CAST", id);
                    startActivity(intent1);
                }
            });

            Call<MovieModel> call4 = api.getFavouriteMovies(user_id, session_id);
            call4.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                favoriteImage.setImageResource(R.mipmap.favourites_full_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });


            favoriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieResponse.media_type="movie";
                    movieResponse.media_id=Integer.valueOf(id);
                    movieResponse.favorite=true;
                    Call<MyMovies> call5 = api.postFavouriteMovie(session_id,"application/json;charset=utf-8;", movieResponse);
                    call5.enqueue(new Callback<MyMovies>() {
                        @Override
                        public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                            if(response.isSuccessful()){
                                favoriteImage.setImageResource(R.mipmap.favourites_full_hdpi);
                                Toast.makeText(MovieDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MyMovies> call, Throwable t) {
                            Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            Call<MovieModel> call6 = api.getWatchlistMovies(user_id, session_id);
            call6.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                watchlistImage.setImageResource(R.mipmap.watchlist_remove_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });

            watchlistImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieResponse.media_type="movie";
                    movieResponse.media_id=Integer.valueOf(id);
                    movieResponse.watchlist=true;
                    Call<MyMovies> call5 = api.postWatchlistMovie(session_id,"application/json;charset=utf-8;", movieResponse);
                    call5.enqueue(new Callback<MyMovies>() {
                        @Override
                        public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                            if(response.isSuccessful()){
                                watchlistImage.setImageResource(R.mipmap.watchlist_remove_hdpi);
                                Toast.makeText(MovieDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MyMovies> call, Throwable t) {
                            Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            Call<MovieModel> call7 = api.getRatedMovies(user_id, session_id);
            call7.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                ratingImage.setImageResource(R.drawable.full_star_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });

            ratingImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ratingBar.setVisibility(View.VISIBLE);
                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                            yourVote.setVisibility(View.VISIBLE);
                            yourVote.setText("Your vote is: " + v*2);
                            value=v*2;
                            movieResponse.value=value;
                            Call<MyMovies> call8 = api.postRatingMovie(id, "application/json;charset=utf-8;", movieResponse, session_id);
                            call8.enqueue(new Callback<MyMovies>() {
                                @Override
                                public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                                    if(response.isSuccessful()){
                                        Toast.makeText(MovieDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        ratingImage.setImageResource(R.drawable.full_star_hdpi);
                                    }
                                    else{

                                    }
                                }

                                @Override
                                public void onFailure(Call<MyMovies> call, Throwable t) {

                                }
                            });
                        }
                    });
                }
            });



        }

        if(intent.hasExtra("SEARCH_4")){
            movieModel=PreferenceManager.getMovies(this);
            pos=intent.getIntExtra("SEARCH_POS4", 0);
            Picasso.with(MovieDetailsActivity.this)
                    .load("https://image.tmdb.org/t/p/w500/"+movieModel.results.get(pos).getPoster_path())
                    .into(movieWallpaper);
            movieName.setText(movieModel.results.get(pos).getOriginal_title());
            description.setText(movieModel.results.get(pos).getOverview());
            id=intent.getStringExtra("SEARCH_ID4");

            Call<MovieModel> callID = api.getMovieIDD(id);
            callID.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if(response.isSuccessful()){
                        movieModel=response.body();
                        String date = movieModel.release_date;
                        String date2 = date.substring(0,4);
                        String genre="";
                        for(int i=0; i<movieModel.genres.size(); i++){
                            genre+=movieModel.genres.get(i).getName()+" | ";
                        }
                        genres.setText(date2+" | "+genre);
                    }
                    else{

                    }

                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {

                }
            });

            Call <MovieModel> call2 = api.getMovieID(id);
            call2.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if(response.isSuccessful()) {

                        movieModel = response.body();
                        if(movieModel.results.size()!=0) {
                            url = movieModel.results.get(0).getKey();
                        }
                        else{
                            url="";
                        }
                        imagePlay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+url));
                                startActivity(browserIntent);
                            }
                        });

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();

                }
            });

            Call<MovieModel> call3 = api.getMovieCredits(id);
            call3.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        String writer = "";
                        for (int i = 0; i < movieModel.crew.size(); i++) {
                            if (movieModel.crew.get(i).getJob().equals("Director")) {
                                director.setText(movieModel.crew.get(i).getName());
                            }
                            if (movieModel.crew.get(i).getDepartment().equals("Writing")) {
                                writer += movieModel.crew.get(i).getName() + ", ";
                            }
                        }
                        writers.setText(writer);
                        stars.setText(movieModel.cast.get(0).name + ", " + movieModel.cast.get(1).name);
                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();
                }
            });

            buttonCast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(MovieDetailsActivity.this, FullCastActivity.class);
                    intent1.putExtra("EXTRA_CAST", id);
                    startActivity(intent1);
                }
            });

            Call<MovieModel> call4 = api.getFavouriteMovies(user_id, session_id);
            call4.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                favoriteImage.setImageResource(R.mipmap.favourites_full_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });


            favoriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieResponse.media_type="movie";
                    movieResponse.media_id=Integer.valueOf(id);
                    movieResponse.favorite=true;
                    Call<MyMovies> call5 = api.postFavouriteMovie(session_id,"application/json;charset=utf-8;", movieResponse);
                    call5.enqueue(new Callback<MyMovies>() {
                        @Override
                        public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                            if(response.isSuccessful()){
                                favoriteImage.setImageResource(R.mipmap.favourites_full_hdpi);
                                Toast.makeText(MovieDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MyMovies> call, Throwable t) {
                            Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            Call<MovieModel> call6 = api.getWatchlistMovies(user_id, session_id);
            call6.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                watchlistImage.setImageResource(R.mipmap.watchlist_remove_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });

            watchlistImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieResponse.media_type="movie";
                    movieResponse.media_id=Integer.valueOf(id);
                    movieResponse.watchlist=true;
                    Call<MyMovies> call5 = api.postWatchlistMovie(session_id,"application/json;charset=utf-8;", movieResponse);
                    call5.enqueue(new Callback<MyMovies>() {
                        @Override
                        public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                            if(response.isSuccessful()){
                                watchlistImage.setImageResource(R.mipmap.watchlist_remove_hdpi);
                                Toast.makeText(MovieDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MyMovies> call, Throwable t) {
                            Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            Call<MovieModel> call7 = api.getRatedMovies(user_id, session_id);
            call7.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.isSuccessful()) {
                        movieModel = response.body();
                        for(int i=0; i<movieModel.results.size(); i++){
                            if(movieModel.results.get(i).getId().equals(id)){
                                ratingImage.setImageResource(R.drawable.full_star_hdpi);
                            }
                        }

                    }
                    else{
                        Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });

            ratingImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ratingBar.setVisibility(View.VISIBLE);
                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                            yourVote.setVisibility(View.VISIBLE);
                            yourVote.setText("Your vote is: " + v*2);
                            value=v*2;
                            movieResponse.value=value;
                            Call<MyMovies> call8 = api.postRatingMovie(id, "application/json;charset=utf-8;", movieResponse, session_id);
                            call8.enqueue(new Callback<MyMovies>() {
                                @Override
                                public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                                    if(response.isSuccessful()){
                                        Toast.makeText(MovieDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        ratingImage.setImageResource(R.drawable.full_star_hdpi);
                                    }
                                    else{

                                    }
                                }

                                @Override
                                public void onFailure(Call<MyMovies> call, Throwable t) {

                                }
                            });
                        }
                    });
                }
            });

        }

    }
}
