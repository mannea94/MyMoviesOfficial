package com.technology.manne.mymovies.Api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.technology.manne.mymovies.BuildConfig;
import com.technology.manne.mymovies.Model.MovieModel;
import com.technology.manne.mymovies.Model.MovieResponse;
import com.technology.manne.mymovies.Model.MyMovies;
import com.technology.manne.mymovies.Model.People;
import com.technology.manne.mymovies.Model.PeopleModel;
import com.technology.manne.mymovies.Model.RequestToken;
import com.technology.manne.mymovies.Model.User;
import com.technology.manne.mymovies.Other.CheckConnection;
import com.technology.manne.mymovies.Other.LoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by manne on 08.2.2018.
 */

public class RestApi {
    public static final int request_max_time_tn_seconds=20;

    public Context activity;
    public RequestToken token;

    public RestApi(Context activity){
        this.activity=activity;
    }

    public Retrofit getRetrofitInstance(){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor(activity, token))
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Request request = chain.request();
//                        HttpUrl url = request.url().newBuilder().addQueryParameter("api_key","71d57011942fac8afe414ff91d8b1628").build();
//                        request = request.newBuilder().url(url).build();
//                        return chain.proceed(request);
//                    }
//                })
                .readTimeout(request_max_time_tn_seconds, TimeUnit.SECONDS)
                .connectTimeout(request_max_time_tn_seconds, TimeUnit.SECONDS)
                .build();
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
//                .baseUrl(ApiConstants.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public void checkInternet(Runnable callback) {
        if (CheckConnection.CheckInternetConnectivity(activity, true, callback))
            callback.run();
    }

    public void checkInternet(Runnable callback, boolean showConnectionDialog) {
        if (CheckConnection.CheckInternetConnectivity(activity, showConnectionDialog, callback))
            callback.run();
        else {
            Toast.makeText(activity, "Connection failed, please check your connection settings", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkInternet(Runnable callback, boolean showConnectionDialog, String message) {
        if (CheckConnection.CheckInternetConnectivity(activity, showConnectionDialog, callback))
            callback.run();
        else {
            if (showConnectionDialog)
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            else
                Log.d("Connection failed", "" + message);
        }
    }

    public ApiService request(){
        return getRetrofitInstance().create(ApiService.class);
    }

    public Call<MovieModel> getMovies(int page){
        return request().getMovies(page);
    }

    public Call<PeopleModel> getPeople(){
        return request().getPeople();
    }

    public Call<MovieModel> getTopRated(){
        return request().getTopRated();
    }

    public Call<MovieModel> getUpcoming(){
        return request().getUpcoming();
    }

    public Call<MovieModel> getNowPlaying(){
        return request().getNowPlaying();
    }

    public Call<MovieModel> getMovieSearch(String movie){
        return request().getSearchMovie(movie);
    }
    public Call<PeopleModel> getPersonSearch(String person){
        return request().getSearchPerson(person);
    }

    public Call<MyMovies> getMovieIDDD(String id){
        return request().getMovieIDDD(id);
    }

    public Call<MovieModel> getMovieCredits(String id){
        return request().getMovieCredits(id);
    }

    public Call<MovieModel> getMovieID(String id){
        return request().getMovieID(id);
    }

    public Call<People> getPersonID(String id){
        return request().getPersonID(id);
    }

    public Call<RequestToken> getRequestToken(){
        return request().getRequestToken();
    }

    public Call<RequestToken> getSessionWithLogin(String username, String password, String token){
        return request().getSessionWithLogin(username, password, token);
    }

    public Call<RequestToken> getSessionToken(String token){
        return request().getSessionToken(token);
    }

    public Call<User> getUserDetails(String session_id){
        return request().getUserDetails(session_id);
    }

    public Call<MovieModel> getFavouriteMovies(String account_id, String session_id){
        return request().getFavouriteMovies(account_id, session_id);
    }

    public Call<MovieModel> getRatedMovies(String account_id, String session_id){
        return request().getRatedMovies(account_id, session_id);
    }

    public Call<MovieModel> getWatchlistMovies(String account_id, String session_id){
        return request().getWatchlistMovies(account_id, session_id);
    }

    public Call<MyMovies> postFavouriteMovie(String session_id, String header, MovieResponse movieResponse){
        return request().postFavouriteMovie( session_id, header, movieResponse);
    }

    public Call<MyMovies> postWatchlistMovie(String session_id, String header, MovieResponse movieResponse){
        return request().postWatchlistMovie(session_id, header, movieResponse);
    }

    public Call<MyMovies> postRatingMovie(String movie_id, String header, MovieResponse movieResponse, String session_id){
        return request().postRatingMovie(movie_id, header, movieResponse, session_id);
    }

    public Call<MyMovies> deleteRatingMovie(String movie_id, String header, String session_id){
        return request().deleteRatingMovie(movie_id, header, session_id);
    }

    public Call<MovieModel> getMovieIDD(String id){
        return request().getMovieIDD(id);
    }

    public Call<MyMovies> getMovieImages(String id){
        return request().getMovieImages(id);
    }
}
