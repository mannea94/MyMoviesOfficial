package com.technology.manne.mymovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.technology.manne.mymovies.Api.RestApi;
import com.technology.manne.mymovies.Listener.OnRowClickListener;
import com.technology.manne.mymovies.Model.MovieModel;
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
        if(myMovie.getPoster_path()!=null) {
            Picasso.with(context)
                    .load("https://image.tmdb.org/t/p/w500/" + myMovie.getPoster_path())
                    .fit()
                    .into(holder.mainImage);
        }
        else {
            Picasso.with(context)
                    .load(R.drawable.movie_pic)
                    .fit()
                    .into(holder.mainImage);
        }
        holder.movieTitle.setText(myMovie.getTitle());
        if(myMovie.getVote_average().length()<4){
            holder.ratingText.setText(myMovie.getVote_average());
        }
        else{
            holder.ratingText.setText(myMovie.getVote_average().substring(0,3));
        }
        holder.ratingText2.setText(myMovie.getRating());
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
                            PreferenceManager.addMovies(movieModel, context);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();

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
        @BindView(R.id.ratingText2)
        TextView ratingText2;
        String session_id;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
