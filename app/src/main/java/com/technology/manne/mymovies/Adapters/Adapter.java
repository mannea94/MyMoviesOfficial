package com.technology.manne.mymovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.technology.manne.mymovies.Listener.OnRowClickListener;
import com.technology.manne.mymovies.Model.MovieModel;
import com.technology.manne.mymovies.Model.MyMovies;
import com.technology.manne.mymovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        if(myMovie.getPoster_path()!=null){
            Picasso.with(context)
                    .load("https://image.tmdb.org/t/p/w500/"+myMovie.getPoster_path())
                    .fit()
                    .into(holder.mainImage);
        }
        else{
            Picasso.with(context)
                    .load(R.drawable.movie_pic)
                    .fit()
                    .into(holder.mainImage);
        }

        holder.movieTitle.setText(myMovie.getTitle());
        holder.ratingText.setText(myMovie.getVote_average());
        holder.favorite.setImageResource(R.mipmap.favourites_empty_hdpi);
        holder.ratingImage.setImageResource(R.mipmap.rate_empty_hdpi);
        holder.viewsImage.setImageResource(R.mipmap.watchlist_add_hdpi);
        if(myMovie.isFavorite()){
            holder.favorite.setImageResource(R.mipmap.favourites_full_hdpi);
        }
        if(myMovie.isRated()){
            holder.ratingImage.setImageResource(R.mipmap.full_star_hdpi);
        }
        if(myMovie.isWatchlist()){
            holder.viewsImage.setImageResource(R.mipmap.watchlist_remove_hdpi);
//            holder.viewsText.setText("added");
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
