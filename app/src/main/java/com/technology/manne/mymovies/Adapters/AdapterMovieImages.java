package com.technology.manne.mymovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.technology.manne.mymovies.Listener.OnRowClickListener;
import com.technology.manne.mymovies.Model.MyMovies;
import com.technology.manne.mymovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manne on 08.2.2018.
 */

public class AdapterMovieImages extends RecyclerView.Adapter<AdapterMovieImages.ViewHolder> {

    Context context;
    ArrayList<MyMovies> movie_images = new ArrayList<>();
    OnRowClickListener onClickListener;


    public void setItems(ArrayList<MyMovies> movie_images_){
        movie_images=movie_images_;
    }

    public AdapterMovieImages(Context context_, OnRowClickListener onClickListener_){
        context=context_;
        onClickListener=onClickListener_;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row_movie_images, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final MyMovies movie_image = movie_images.get(position);
        if(movie_image.image_movie!=null) {
            Picasso.with(context)
                    .load("https://image.tmdb.org/t/p/w500" + movie_image.image_movie)
                    .fit()
                    .into(holder.mainImage);
        }
        else{
            Picasso.with(context)
                    .load(R.drawable.movie_pic)
                    .fit()
                    .into(holder.mainImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onRowClick(movie_image, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movie_images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.mainImage)
        ImageView mainImage;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
