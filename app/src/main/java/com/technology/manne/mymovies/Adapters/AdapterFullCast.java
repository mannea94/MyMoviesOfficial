package com.technology.manne.mymovies.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.technology.manne.mymovies.Listener.OnRowClickListener;
import com.technology.manne.mymovies.Model.MyMovies;
import com.technology.manne.mymovies.R;
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
    OnRowClickListener onRowClickListener;

    public void setItems(ArrayList<MyMovies> myMovies_){
        myMovies=myMovies_;
    }

    public AdapterFullCast(Context context_, ArrayList<MyMovies> myMovies_, OnRowClickListener onRowClickListener_){
        context=context_;
        myMovies=myMovies_;
        onRowClickListener=onRowClickListener_;
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final MyMovies myMovie = myMovies.get(position);
            if(myMovie.getProfile_path()!=null){
                Picasso.with(context)
                        .load("https://image.tmdb.org/t/p/w500/"+myMovie.getProfile_path())
                        .into(holder.imageView);
            }
            else{
                Picasso.with(context)
                        .load(R.drawable.profle_pic)
                        .into(holder.imageView);
            }

            holder.textView.setText(myMovie.getName());
            holder.character.setText(myMovie.getCharacter());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRowClickListener.onRowClick(myMovie, position);
                }
            });

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
        @BindView(R.id.textCharacter)
        TextView character;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
