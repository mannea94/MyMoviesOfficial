package com.example.manne.mymovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manne.mymovies.Activities.MovieDetailsActivity;
import com.example.manne.mymovies.Api.RestApi;
import com.example.manne.mymovies.Listener.OnRowClickListener;
import com.example.manne.mymovies.MainDrawerActivity;
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
 * Created by manne on 06.2.2018.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    Context context;
    ArrayList<MyMovies> myMovies = new ArrayList<>();
    MovieModel movieModel = new MovieModel();
    OnRowClickListener onRowClickListener;

    public void setItems(ArrayList<MyMovies> myMovies1){
        myMovies=myMovies1;
    }

    public Adapter(Context context1, MovieModel movieModel_, OnRowClickListener onRowClickListener_){
        context=context1;
        movieModel=movieModel_;
        onRowClickListener=onRowClickListener_;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //Inflate the custom layout
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        //Return a new holder instance
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
        holder.ratingText.setText(myMovie.getVote_average());

//        holder.favorite.setImageResource(R.mipmap.favourites_empty_hdpi);
//        holder.ratingImage.setImageResource(R.mipmap.rate_empty_hdpi);
//        holder.viewsImage.setImageResource(R.mipmap.watchlist_add_hdpi);
//        holder.viewsText.setText("");
        if(myMovie.isFavorite()){
            holder.favorite.setImageResource(R.mipmap.favourites_full_hdpi);
        }
        else{
            holder.favorite.setImageResource(R.mipmap.favourites_empty_hdpi);
        }
        if(myMovie.isRated()){
            holder.ratingImage.setImageResource(R.drawable.full_star_hdpi);
        }
        else{
            holder.ratingImage.setImageResource(R.mipmap.rate_empty_hdpi);
        }
        if(myMovie.isWatchlist()){
            holder.viewsImage.setImageResource(R.mipmap.watchlist_remove_hdpi);
            holder.viewsText.setText("added");
        }
        else{
            holder.viewsImage.setImageResource(R.mipmap.watchlist_add_hdpi);
            holder.viewsText.setText("");
        }

        holder.mainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRowClickListener.onRowClick(myMovie, position);
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
        @BindView(R.id.movieName)
        ImageView favorite;
        @BindView(R.id.movieTitle)
        TextView movieTitle;
        @BindView(R.id.ratingImage)
        ImageView ratingImage;
        @BindView(R.id.ratingText)
        TextView ratingText;
        @BindView(R.id.viewsImage)
        ImageView viewsImage;
        @BindView(R.id.viewsText)
        TextView viewsText;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
