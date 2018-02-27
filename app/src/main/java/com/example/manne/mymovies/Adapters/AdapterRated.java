package com.example.manne.mymovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manne.mymovies.Activities.RatedActivity;
import com.example.manne.mymovies.Api.RestApi;
import com.example.manne.mymovies.Listener.OnRowClickListener;
import com.example.manne.mymovies.Model.MovieModel;
import com.example.manne.mymovies.Model.MyMovies;
import com.example.manne.mymovies.PreferenceManager;
import com.example.manne.mymovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by manne on 08.2.2018.
 */

public class AdapterRated extends RecyclerView.Adapter<AdapterRated.ViewHolder> {

    Context context;
    ArrayList<MyMovies> myMovies = new ArrayList<>();
    MovieModel movieModel;
    OnRowClickListener onRowClickListener;
    RestApi api = new RestApi(context);


    public void setItems(ArrayList<MyMovies> myMovies_){
        myMovies=myMovies_;
    }

    public AdapterRated(Context context_, MovieModel movieModel_, OnRowClickListener onRowClickListener_){
        context=context_;
        movieModel=movieModel_;
        onRowClickListener=onRowClickListener_;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row_rated, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MyMovies myMovie = movieModel.results.get(position);
        Picasso.with(context)
                .load("https://image.tmdb.org/t/p/w500/"+myMovie.getPoster_path())
                .into(holder.mainImage);
        holder.movieTitle.setText(myMovie.getTitle());
        holder.ratingText.setText(myMovie.getRating());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRowClickListener.onRowClick(myMovie, position);
            }
        });
        holder.session_id= PreferenceManager.getSessionID(context);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<MyMovies> call = api.deleteRatingMovie(movieModel.results.get(position).getId(), "application/json;charset=utf-8;", holder.session_id);
                call.enqueue(new Callback<MyMovies>() {
                    @Override
                    public void onResponse(Call<MyMovies> call, Response<MyMovies> response) {
                        if(response.isSuccessful()){
                            movieModel.results.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MyMovies> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieModel.results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.mainImage)
        ImageView mainImage;
        @BindView(R.id.ratingImageDelete)
        ImageView delete;
        @BindView(R.id.movieTitle)
        TextView movieTitle;
        @BindView(R.id.ratingImage)
        ImageView ratingImage;
        @BindView(R.id.ratingText)
        TextView ratingText;
        String session_id;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
