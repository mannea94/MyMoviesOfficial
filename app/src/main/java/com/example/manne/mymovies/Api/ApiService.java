package com.example.manne.mymovies.Api;

import com.example.manne.mymovies.Model.MovieModel;
import com.example.manne.mymovies.Model.MovieResponse;
import com.example.manne.mymovies.Model.MyMovies;
import com.example.manne.mymovies.Model.People;
import com.example.manne.mymovies.Model.PeopleModel;
import com.example.manne.mymovies.Model.RequestToken;
import com.example.manne.mymovies.Model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by manne on 08.2.2018.
 */

public interface ApiService {
    @GET("movie/popular?"+ApiConstants.api_key)
    Call<MovieModel> getMovies ();

    @GET("person/popular?"+ApiConstants.api_key)
    Call<PeopleModel> getPeople();

    @GET("movie/top_rated?"+ApiConstants.api_key)
    Call<MovieModel> getTopRated();

    @GET("movie/upcoming?"+ApiConstants.api_key)
    Call<MovieModel> getUpcoming();

    @GET("movie/now_playing?"+ApiConstants.api_key)
    Call<MovieModel> getNowPlaying();

    @GET("movie/{id}?"+ApiConstants.api_key)
    Call<MovieModel> getMovieIDD(@Path("id") String id);


    @GET("search/movie?"+ApiConstants.api_key)
    Call<MovieModel> getSearchMovie(@Query("query") String stringFeature);

    @GET("search/person?"+ApiConstants.api_key)
    Call<PeopleModel> getSearchPerson(@Query("query") String query);
    @GET("movie/{id}/videos?"+ApiConstants.api_key)
    Call<MovieModel> getMovieID(@Path("id") String id);

    @GET("person/{id}?"+ApiConstants.api_key)
    Call<People> getPersonID(@Path("id") String id);

    @GET("movie/{id}/credits?"+ApiConstants.api_key)
    Call<MovieModel> getMovieCredits(@Path("id") String id);

    @GET("authentication/token/new?"+ApiConstants.api_key)
    Call<RequestToken> getRequestToken();

    @GET("authentication/token/validate_with_login?"+ApiConstants.api_key)
    Call<RequestToken> getSessionWithLogin(@Query("username") String username, @Query("password")String password, @Query("request_token")String request_token);

    @GET("authentication/session/new?"+ApiConstants.api_key)
    Call<RequestToken> getSessionToken(@Query("request_token") String request_token);

    @GET("account?"+ApiConstants.api_key)
    Call<User> getUserDetails(@Query("session_id") String session_id);

    @GET("account/{account_id}/favorite/movies?"+ApiConstants.api_key)
    Call<MovieModel> getFavouriteMovies(@Path("account_id") String id, @Query("session_id") String session_id);

    @GET("account/{account_id}/rated/movies?"+ApiConstants.api_key)
    Call<MovieModel> getRatedMovies(@Path("account_id") String id, @Query("session_id") String session_id);

    @GET("account/{account_id}/watchlist/movies?"+ApiConstants.api_key)
    Call<MovieModel> getWatchlistMovies(@Path("account_id") String id, @Query("session_id") String session_id);

    @POST("account/{account_id}/favorite?"+ApiConstants.api_key)
    Call<MyMovies> postFavouriteMovie( @Query("session_id") String session_id, @Header("json/application") String header, @Body MovieResponse movieResponse);

    @POST("account/{account_id}/watchlist?"+ApiConstants.api_key)
    Call<MyMovies> postWatchlistMovie(@Query("session_id") String session_id, @Header("json/application") String header, @Body MovieResponse movieResponse);

    @POST("movie/{movie_id}/rating?"+ApiConstants.api_key)
    Call<MyMovies> postRatingMovie(@Path("movie_id") String movie_id, @Header("json/application") String header, @Body MovieResponse movieResponse, @Query("session_id") String session_id);

    @DELETE("movie/{movie_id}/rating?"+ApiConstants.api_key)
    Call<MyMovies> deleteRatingMovie(@Path("movie_id") String movie_id, @Header("json/application") String header, @Query("session_id") String session_id);

}
