package com.technology.manne.mymovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.technology.manne.mymovies.Listener.OnClickListener;
import com.technology.manne.mymovies.Model.KnownFor;
import com.technology.manne.mymovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manne on 08.2.2018.
 */

public class AdapterKnownFor extends RecyclerView.Adapter<AdapterKnownFor.ViewHolder> {

    Context context;
    ArrayList<KnownFor> knownFor = new ArrayList<>();
    OnClickListener onClickListener;


    public void setItems(ArrayList<KnownFor> knownFor_){
        knownFor=knownFor_;
    }

    public AdapterKnownFor(Context context_, OnClickListener onClickListener_){
        context=context_;
        onClickListener=onClickListener_;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row_known_for, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final KnownFor knownFors = knownFor.get(position);
        if(knownFors.getPoster_path()!=null) {
            Picasso.with(context)
                    .load("https://image.tmdb.org/t/p/w500/" + knownFors.getPoster_path())
                    .fit()
                    .into(holder.mainImage);
        }
        else{
            Picasso.with(context)
                    .load(R.drawable.movie_pic)
                    .fit()
                    .into(holder.mainImage);
        }
        holder.movieTitle.setText(knownFors.getTitle());
        holder.ratingText.setText(knownFors.getVote_average());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.OnRowClick(knownFors, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return knownFor.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.mainImage)
        ImageView mainImage;
        @BindView(R.id.movieTitle)
        TextView movieTitle;
        @BindView(R.id.ratingImage)
        ImageView ratingImage;
        @BindView(R.id.ratingText)
        TextView ratingText;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
