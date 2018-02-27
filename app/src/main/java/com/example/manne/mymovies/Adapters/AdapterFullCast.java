package com.example.manne.mymovies.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manne.mymovies.Activities.FullCastActivity;
import com.example.manne.mymovies.Model.MyMovies;
import com.example.manne.mymovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manne on 19.2.2018.
 */

public class AdapterFullCast extends RecyclerView.Adapter<AdapterFullCast.ViewHolder>{

    Context context;
    ArrayList<MyMovies> myMovies = new ArrayList<>();

    public void setItems(ArrayList<MyMovies> myMovies_){
        myMovies=myMovies_;
    }

    public AdapterFullCast(Context context_, ArrayList<MyMovies> myMovies_){
        context=context_;
        myMovies=myMovies_;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row_full_cast, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyMovies myMovie = myMovies.get(position);

            Picasso.with(context)
                    .load("https://image.tmdb.org/t/p/w500/"+myMovie.getProfile_path())
                    .into(holder.imageView);
            holder.textView.setText(myMovie.getName());

    }

    @Override
    public int getItemCount() {
        return myMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.imageAkter)
        ImageView imageView;
        @BindView(R.id.textAkter)
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
