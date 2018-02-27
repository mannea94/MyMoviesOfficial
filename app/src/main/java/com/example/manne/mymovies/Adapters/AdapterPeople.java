package com.example.manne.mymovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manne.mymovies.Listener.OnRowClickListener;
import com.example.manne.mymovies.Listener.OnRowClickListenerPeople;
import com.example.manne.mymovies.Model.MovieModel;
import com.example.manne.mymovies.Model.MyMovies;
import com.example.manne.mymovies.Model.People;
import com.example.manne.mymovies.Model.PeopleModel;
import com.example.manne.mymovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manne on 08.2.2018.
 */

public class AdapterPeople extends RecyclerView.Adapter<AdapterPeople.ViewHolder> {

    Context context;
    ArrayList<People> myPeople = new ArrayList<>();
    OnRowClickListenerPeople onRowClickListenerPeople;
//    PeopleModel peopleModel = new PeopleModel();

    public void setItems(ArrayList<People> myPeople_){
        myPeople=myPeople_;
    }

    public AdapterPeople(Context context_, ArrayList<People> myPeople_, OnRowClickListenerPeople onRowClickListenerPeople_){
        context=context_;
        myPeople=myPeople_;
        onRowClickListenerPeople=onRowClickListenerPeople_;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row_people, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final People peopleModels = myPeople.get(position);
        Picasso.with(context)
                .load("https://image.tmdb.org/t/p/w500/"+peopleModels.getProfile_path())
                .into(holder.actorImage);
        holder.actorName.setText(peopleModels.getName());
//        holder.description.setText(.getOverview());
        for(int i=0; i<peopleModels.known_for.size(); i++){
            if(holder.description.equals("")){
                holder.description.setText(holder.description.getText() + " " + peopleModels.getKnown_for().get(i).getTitle());
            }
            holder.description.setText(holder.description.getText() + peopleModels.getKnown_for().get(i).getTitle()  + ", ");
        }

        holder.actorNumber.setText(position+1+"");

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

        @BindView(R.id.actorImage)
        ImageView actorImage;
        @BindView(R.id.actorNumber)
        TextView actorNumber;
        @BindView(R.id.actorName)
        TextView actorName;
        @BindView(R.id.typePerson)
        TextView typePerson;
        @BindView(R.id.description)
        TextView description;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
