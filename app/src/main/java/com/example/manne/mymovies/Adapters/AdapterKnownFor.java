package com.example.manne.mymovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manne.mymovies.Listener.OnRowClickListenerPeople;
import com.example.manne.mymovies.Model.People;
import com.example.manne.mymovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manne on 08.2.2018.
 */

public class AdapterKnownFor extends RecyclerView.Adapter<AdapterKnownFor.ViewHolder> {

    Context context;
    ArrayList<People> myPeople = new ArrayList<>();
    OnRowClickListenerPeople onRowClickListenerPeople;
//    PeopleModel peopleModel = new PeopleModel();

    public void setItems(ArrayList<People> myPeople_){
        myPeople=myPeople_;
    }

    public AdapterKnownFor(Context context_, ArrayList<People> myPeople_, OnRowClickListenerPeople onRowClickListenerPeople_){
        context=context_;
        myPeople=myPeople_;
        onRowClickListenerPeople=onRowClickListenerPeople_;
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
        final People peopleModels = myPeople.get(position);
            for (int i = 0; i < peopleModels.known_for.size(); i++) {
                    holder.filmName = peopleModels.getKnown_for().get(i).getTitle();
                    holder.filmImage = peopleModels.getKnown_for().get(i).getPoster_path();
                    holder.filmRating = peopleModels.getKnown_for().get(i).getVote_average();
            }
        holder.movieTitle.setText(holder.filmName);
        Picasso.with(context)
                .load("https://image.tmdb.org/t/p/w500/"+holder.filmImage)
                .into(holder.mainImage);
        holder.ratingText.setText(holder.filmRating);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRowClickListenerPeople.onRowClick(peopleModels, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myPeople.size();
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
        String filmName;
        String filmImage;
        String filmRating;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
