package com.technology.manne.mymovies.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.technology.manne.mymovies.Adapters.AdapterMovieImages;
import com.technology.manne.mymovies.Api.RestApi;
import com.technology.manne.mymovies.Listener.OnRowClickListener;
import com.technology.manne.mymovies.Model.KnownFor;
import com.technology.manne.mymovies.Model.MovieModel;
import com.technology.manne.mymovies.Model.MovieResponse;
import com.technology.manne.mymovies.Model.MyMovies;
import com.technology.manne.mymovies.PreferenceManager;
import com.technology.manne.mymovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
    Button buttonCastCrew;
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
    @BindView(R.id.rateText)
    TextView ratingText;
    @BindView(R.id.buttonCast)
    TextView buttonCast;
    @BindView(R.id.buttonCrew)
    TextView buttonCrew;
    @BindView(R.id.recyclerViewMovieImages)
    RecyclerView recyclerView;
    @BindView(R.id.viewImages)
    TextView viewImages;
    AdapterMovieImages adapter;
    ArrayList<MyMovies> myMoviesImg;
//    @BindView(R.id.delete)
//    ImageView delete;
    MovieResponse movieResponse;
    MyMovies myMovies;
    KnownFor knownFor;
//    String isFav="no";
    boolean isFav=false;
    String isWat="no";
    String isRat="no";
    boolean rat=true;
    boolean bar=true;
    int pos=0;
    double value=0.5;
    String session_id;
    String user_id;
    String url="";
    String id;
    String past_rating="";
    String images="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        buttonCast.setVisibility(View.GONE);
        buttonCrew.setVisibility(View.GONE);
        api=new RestApi(this);
        movieModel=new MovieModel();
        movieResponse=new MovieResponse();
        myMovies=new MyMovies();
        knownFor=new KnownFor();
        myMoviesImg=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MovieDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        ratingBar.setVisibility(View.GONE);
        yourVote.setVisibility(View.GONE);
//        delete.setVisibility(View.GONE);
//        delete.setVisibility(View.GONE);
        final Intent intent = getIntent();
        user_id=PreferenceManager.getUserID(this);
        session_id=PreferenceManager.getSessionID(this);
        api.checkInternet(new Runnable() {
            @Override
            public void run() {

                if(intent.hasExtra("EXTRA1")){
                    id=intent.getStringExtra("EXTRA_ID");
                    apiCall();
                }

                if(intent.hasExtra("SEARCH_1")){
                    id=intent.getStringExtra("SEARCH_ID1");
                    apiCall();
                }

                if(intent.hasExtra("EXTRA2")){
                    id=intent.getStringExtra("EXTRA_ID2");
                    apiCall();
                }

                if(intent.hasExtra("SEARCH_2")){
                    id=intent.getStringExtra("SEARCH_ID2");
                    apiCall();
                }

                if(intent.hasExtra("EXTRA3")){
                    id=intent.getStringExtra("EXTRA_ID3");
                    apiCall();

                }

                if(intent.hasExtra("SEARCH_3")){
                    id=intent.getStringExtra("SEARCH_ID3");
                    apiCall();
                }

                if(intent.hasExtra("EXTRA4")){
                    id=intent.getStringExtra("EXTRA_ID4");
                    apiCall();

                }

                if(intent.hasExtra("SEARCH_4")){
                    id=intent.getStringExtra("SEARCH_ID4");
                    apiCall();
                }

                if(intent.hasExtra("FAVORITES")){
                    id=intent.getStringExtra("FAVORITES_ID");
                    apiCall();
                }

                if(intent.hasExtra("RATED")){
                    id=intent.getStringExtra("RATED_ID");
                    apiCall();
                }

                if(intent.hasExtra("WATCHLIST")){
                    id=intent.getStringExtra("WATCHLIST_ID");
                    apiCall();
                }

                if(intent.hasExtra("KNOWN")){
                    knownFor=PreferenceManager.getKnownFor(MovieDetailsActivity.this);
                    id=knownFor.getId();
                    if(knownFor.getPoster_path()!=null) {
                        Picasso.with(MovieDetailsActivity.this)
                                .load("https://image.tmdb.org/t/p/w500/" + knownFor.getPoster_path())
                                .fit()
                                .into(movieWallpaper);
                    }
                    else{
                        Picasso.with(MovieDetailsActivity.this)
                                .load(R.drawable.movie_pic)
                                .fit()
                                .into(movieWallpaper);
                    }
                    movieName.setText(knownFor.getTitle());
                    ratingText.setText(knownFor.getVote_average());
                    description.setText(knownFor.getOverview()+"\n");
                    apiCall();
                }
            }
        });


    }

//    public void getSearch(){
//
//        if(movieModel.results.get(pos).getPoster_path()!=null ) {
//            Picasso.with(MovieDetailsActivity.this)
//                    .load("https://image.tmdb.org/t/p/w500/" + movieModel.results.get(pos).getPoster_path())
//                    .fit()
//                    .into(movieWallpaper);
//        }
//        else{
//            Picasso.with(MovieDetailsActivity.this)
//                    .load(R.drawable.movie_pic)
//                    .fit()
//                    .into(movieWallpaper);
//        }
//        movieName.setText(movieModel.results.get(pos).getOriginal_title());
//        ratingText.setText(movieModel.results.get(pos).getVote_average());
//        description.setText(movieModel.results.get(pos).getOverview()+"\n");
//
//    }

    public void apiCall(){
        api.checkInternet(new Runnable() {
            @Override
            public void run() {
                Call<MyMovies> call1 = api.getMovieIDDD(id);
                call1.enqueue(new Callback<MyMovies>() {
                    @Override
                    public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                        if(response.isSuccessful()){
                            myMovies=response.body();
                            if(myMovies.getPoster_path()!=null){
                                Picasso.with(MovieDetailsActivity.this)
                                        .load("https://image.tmdb.org/t/p/w500/"+myMovies.getPoster_path())
                                        .fit()
                                        .into(movieWallpaper);
                            }
                            else{
                                Picasso.with(MovieDetailsActivity.this)
                                        .load(R.drawable.movie_pic)
                                        .fit()
                                        .into(movieWallpaper);
                            }
                            movieName.setText(myMovies.getOriginal_title());
                            if(myMovies.getVote_average().length()<4){
                                ratingText.setText(myMovies.getVote_average());
                            }
                            else{
                                ratingText.setText(myMovies.getVote_average().substring(0,3));
                            }

                            description.setText(myMovies.getOverview()+"\n");

                        }
                        else{

                        }
                    }

                    @Override
                    public void onFailure(Call<MyMovies> call, Throwable t) {
                        Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();

                    }
                });

                Call<MovieModel> callID = api.getMovieIDD(id);
                callID.enqueue(new Callback<MovieModel>() {
                    @Override
                    public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                        if(response.isSuccessful()){
                            movieModel=response.body();
                            String date = movieModel.release_date;
                            String date2;
                            if(date!=null && !date.isEmpty()) {
                                date2 = date.substring(0, 4);
                            }
                            else{
                                date2="";
                            }
                            String genre="";
                            for(int i=0; i<movieModel.genres.size(); i++){
                                genre+=movieModel.genres.get(i).getName()+" ";
                            }
                            releaseDate.setText(date2);
                            genres.setText(genre);

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

                            if(movieModel.cast!=null && movieModel.cast.size()!=0) {
                                String star = "";
                                for(int i=0; i<movieModel.cast.size(); i++) {
                                    if(i==0 || i==1) {
                                        star += movieModel.cast.get(i).name + ", ";
                                    }
                                }
                               stars.setText(star);
                            }
                            else{
                                stars.setText("");
                            }
                        }
                        else{
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieModel> call, Throwable t) {
                        Toast.makeText(MovieDetailsActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();
                    }
                });

                buttonCastCrew.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(rat){
                            buttonCast.setVisibility(View.VISIBLE);
                            buttonCrew.setVisibility(View.VISIBLE);
                            buttonCast.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent1 = new Intent(MovieDetailsActivity.this, FullCastActivity.class);
                                    intent1.putExtra("EXTRA_CAST", id);
                                    startActivity(intent1);
                                }
                            });
                            buttonCrew.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent1 = new Intent(MovieDetailsActivity.this, FullCrewActivity.class);
                                    intent1.putExtra("EXTRA_CREW", id);
                                    startActivity(intent1);
                                }
                            });
                            rat=false;
                        }
                        else{
                            buttonCast.setVisibility(View.GONE);
                            buttonCrew.setVisibility(View.GONE);
                            rat=true;
                        }


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
                                    favoriteImage.setImageResource(R.mipmap.favourites_full_xhdpi);
                                    isFav=false;
                                }
                                else{
                                    isFav=true;
                                }
                            }

                        }
                        else{
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

                       if(isFav) {
                           movieResponse.media_type = "movie";
                           movieResponse.media_id = Integer.valueOf(id);
                           movieResponse.favorite = true;
                           Call<MyMovies> call5 = api.postFavouriteMovie(session_id, "application/json;charset=utf-8;", movieResponse);
                           call5.enqueue(new Callback<MyMovies>() {
                               @Override
                               public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                                   if (response.isSuccessful()) {
                                       favoriteImage.setImageResource(R.mipmap.favourites_full_xhdpi);
                                       Toast.makeText(MovieDetailsActivity.this, "Added to favourites", Toast.LENGTH_SHORT).show();
                                       isFav=false;
                                   } else {
                                       Toast.makeText(MovieDetailsActivity.this, "Please log in", Toast.LENGTH_SHORT).show();
                                   }
                               }

                               @Override
                               public void onFailure(Call<MyMovies> call, Throwable t) {
                                   Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                               }
                           });

                       }
                       else{
                           movieResponse.media_type="movie";
                           movieResponse.media_id=Integer.valueOf(id);
                           movieResponse.favorite=false;
                           Call<MyMovies> call4 = api.postFavouriteMovie(session_id,"application/json;charset=utf-8;", movieResponse);
                           call4.enqueue(new Callback<MyMovies>() {
                               @Override
                               public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                                   if(response.isSuccessful()){
                                       favoriteImage.setImageResource(R.mipmap.favourites_empty_xhdpi);
                                       Toast.makeText(MovieDetailsActivity.this, "Removed from favourites", Toast.LENGTH_SHORT).show();
                                       isFav=true;
                                   }
                                   else{
                                   }
                               }

                               @Override
                               public void onFailure(Call<MyMovies> call, Throwable t) {
                                   Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                               }
                           });

                       }
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
                                    watchlistImage.setImageResource(R.mipmap.watchlist_remove_xhdpi);
                                    isWat="no";
                                }
                                else{
                                    isWat="yes";
                                }
                            }

                        }
                        else{
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
                        if(isWat.equals("yes")){
                            movieResponse.media_type="movie";
                            movieResponse.media_id=Integer.valueOf(id);
                            movieResponse.watchlist=true;
                            Call<MyMovies> call5 = api.postWatchlistMovie(session_id,"application/json;charset=utf-8;", movieResponse);
                            call5.enqueue(new Callback<MyMovies>() {
                                @Override
                                public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                                    if(response.isSuccessful()){
                                        watchlistImage.setImageResource(R.mipmap.watchlist_remove_xhdpi);
                                        Toast.makeText(MovieDetailsActivity.this, "Added to watchlist", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(MovieDetailsActivity.this, "Please log in", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyMovies> call, Throwable t) {
                                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                                }
                            });
                            isWat="no";
                        }
                        else{
                            movieResponse.media_type="movie";
                            movieResponse.media_id=Integer.valueOf(id);
                            movieResponse.watchlist=false;
                            Call<MyMovies> call5 = api.postWatchlistMovie(session_id,"application/json;charset=utf-8;", movieResponse);
                            call5.enqueue(new Callback<MyMovies>() {
                                @Override
                                public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                                    if(response.isSuccessful()){
                                        watchlistImage.setImageResource(R.mipmap.watchlist_add_xhdpi);
                                        Toast.makeText(MovieDetailsActivity.this, "Removed from watchlist", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyMovies> call, Throwable t) {
                                    Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                                }
                            });
                            isWat="yes";
                        }

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
                                    ratingImage.setImageResource(R.mipmap.full_star_hdpi);
                                    past_rating=movieModel.results.get(i).getRating();
                                    isRat="no";
                                    rat=false;
                                }
                                else{
                                    isRat="yes";
                                    rat=true;
                                }
                            }
                            if(!past_rating.isEmpty()){
                                ratingBar.setRating(Float.valueOf(past_rating)/2);
                                yourVote.setText("Your rating is: "+past_rating);
//                                bar=false;
                                isRat="no";
                            }

                        }
                        else{
                        }
                    }


                    @Override
                    public void onFailure(Call<MovieModel> call, Throwable t) {
                        Toast.makeText(MovieDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
                if(!session_id.isEmpty()) {
                    ratingImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (bar) {
//                                delete.setVisibility(View.VISIBLE);
                                ratingBar.setVisibility(View.VISIBLE);
                                if (!past_rating.isEmpty()) {
                                    yourVote.setVisibility(View.VISIBLE);
                                }
                                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                    @Override
                                    public void onRatingChanged(final RatingBar ratingBar, final float v, boolean b) {
//                                    if(isRat.equals("yes")){
                                        yourVote.setVisibility(View.VISIBLE);
                                        yourVote.setText("Your rating is: " + v * 2);
                                        value = v * 2;
                                        movieResponse.value = value;

                                        Call<MyMovies> call8 = api.postRatingMovie(id, "application/json;charset=utf-8;", movieResponse, session_id);
                                        call8.enqueue(new Callback<MyMovies>() {
                                            @Override
                                            public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                                                if (response.isSuccessful()) {
                                                    Toast.makeText(MovieDetailsActivity.this, "Added to rated", Toast.LENGTH_SHORT).show();
                                                    ratingImage.setImageResource(R.mipmap.full_star_hdpi);
//                                                    isRat="no";
                                                }
                                                else {
                                                    Toast.makeText(MovieDetailsActivity.this, "Please log in", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<MyMovies> call, Throwable t) {

                                            }
                                        });
//                                    }

//                                    else{
//                                        delete.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//                                                Call<MyMovies>call = api.deleteRatingMovie(id, "application/json;charset=utf-8;", session_id);
//                                                call.enqueue(new Callback<MyMovies>() {
//                                                    @Override
//                                                    public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
//                                                        ratingImage.setImageResource(R.mipmap.rate_empty_xxhdpi);
//                                                        Toast.makeText(MovieDetailsActivity.this, "Removed from rated", Toast.LENGTH_SHORT).show();
//                                                        ratingBar.setRating(0);
//                                                        yourVote.setVisibility(View.GONE);
//                                                        isRat="yes";
//                                                    }
//
//                                                    @Override
//                                                    public void onFailure(Call<MyMovies> call, Throwable t) {
//
//                                                    }
//                                                });
//                                            }
//                                        });
//
//
//                                    }


                                    }
                                });
                                bar = false;
                            } else {
                                ratingBar.setVisibility(View.GONE);
//                                delete.setVisibility(View.GONE);
                                yourVote.setVisibility(View.GONE);
                                bar = true;
                            }

                        }
                    });
                }


                if(!session_id.isEmpty()){
                    ratingImage.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            Call<MyMovies>call = api.deleteRatingMovie(id, "application/json;charset=utf-8;", session_id);
                            call.enqueue(new Callback<MyMovies>() {
                                @Override
                                public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                                    if(response.isSuccessful()){
                                        ratingImage.setImageResource(R.mipmap.rate_empty_xxhdpi);
                                        Toast.makeText(MovieDetailsActivity.this, "Removed from rated", Toast.LENGTH_SHORT).show();
                                        ratingBar.setRating(0);
                                        yourVote.setVisibility(View.GONE);
                                        isRat="yes";
                                    }
                                    else{
                                        Toast.makeText(MovieDetailsActivity.this, "Please log in", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyMovies> call, Throwable t) {

                                }
                            });
                            return true;
                        }
                    });
                }




            }
        });

        Call<MyMovies> call = api.getMovieImages(id);
        call.enqueue(new Callback<MyMovies>() {
            @Override
            public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                if(response.isSuccessful()){
                    myMovies=response.body();
                    PreferenceManager.addMovieImage(myMovies, MovieDetailsActivity.this);
                    for(int i=0; i<myMovies.backdrops.size(); i++){
                        images=myMovies.backdrops.get(i).file_path;
                        myMoviesImg.add(new MyMovies(images));
                    }
                    adapter = new AdapterMovieImages(MovieDetailsActivity.this, new OnRowClickListener() {
                        @Override
                        public void onRowClick(MyMovies movies, int position) {
                            Intent intent = new Intent(MovieDetailsActivity.this, ImageActivity.class);
                            intent.putExtra("EXTRA_IMAGE", position);
                            startActivity(intent);
                        }
                    });
                    adapter.setItems(myMoviesImg);
                    recyclerView.setAdapter(adapter);

                }
                else{

                }
            }

            @Override
            public void onFailure(Call<MyMovies> call, Throwable t) {

            }
        });

       viewImages.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Call<MyMovies> call = api.getMovieImages(id);
               call.enqueue(new Callback<MyMovies>() {
                   @Override
                   public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                       if(response.isSuccessful()){
                           myMovies=response.body();
                           PreferenceManager.addMovieImage(myMovies, MovieDetailsActivity.this);
                           if(myMovies.backdrops!=null && myMovies.backdrops.size()!=0) {
                               Intent intent = new Intent(MovieDetailsActivity.this, FragmentActivity.class);
                               startActivity(intent);
                           }
                           else{
                               Toast.makeText(MovieDetailsActivity.this, "No posters found", Toast.LENGTH_SHORT).show();
                           }
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

}
