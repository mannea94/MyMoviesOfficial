package com.example.manne.mymovies.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manne.mymovies.Adapters.Adapter;
import com.example.manne.mymovies.Adapters.AdapterKnownFor;
import com.example.manne.mymovies.Api.ApiService;
import com.example.manne.mymovies.Api.RestApi;
import com.example.manne.mymovies.Listener.OnRowClickListenerPeople;
import com.example.manne.mymovies.Model.People;
import com.example.manne.mymovies.Model.PeopleModel;
import com.example.manne.mymovies.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeopleDetailsActivity extends AppCompatActivity {
    @BindView(R.id.nameActor)
    TextView nameActor;
    @BindView(R.id.imageActor)
    ImageView imageActor;
    @BindView(R.id.birthday)
    TextView birthday;
    @BindView(R.id.biography)
    TextView biography;
    @BindView(R.id.deathday)
    TextView deathday;
    @BindView(R.id.recyclerViewKnownFor)
    RecyclerView recyclerView;
    AdapterKnownFor adapter;
    RestApi api;
    People peopleModel;
    PeopleModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_details);
        ButterKnife.bind(this);
        api=new RestApi(this);
        peopleModel=new People();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(PeopleDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        deathday.setVisibility(View.GONE);
        final Intent intent = getIntent();
        if(intent.hasExtra("EXTRA_PEOPLE")) {
            Call<People> call = api.getPersonID(intent.getStringExtra("EXTRA_PEOPLE_ID"));
            call.enqueue(new Callback<People>() {
                @Override
                public void onResponse(Call<People> call, Response<People> response) {
                    if (response.isSuccessful()) {
                        peopleModel = response.body();
                        nameActor.setText(peopleModel.getName());
                        Picasso.with(PeopleDetailsActivity.this)
                                .load("https://image.tmdb.org/t/p/w500/" + peopleModel.getProfile_path())
                                .into(imageActor);
                        birthday.setText("Born: " + peopleModel.getBirthday());
                        if(peopleModel.getDeathday()!=null){
                            deathday.setVisibility(View.VISIBLE);
                            deathday.setText("Died: "+ peopleModel.getDeathday());
                        }
                        biography.setText(peopleModel.getBiography());
                    } else {
                        Toast.makeText(PeopleDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<People> call, Throwable t) {
                    Toast.makeText(PeopleDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });

            Call<PeopleModel> call2 = api.getPeople();
            call2.enqueue(new Callback<PeopleModel>() {
                @Override
                public void onResponse(Call<PeopleModel> call, Response<PeopleModel> response) {
                    if(response.isSuccessful()){
                        model=response.body();
                        adapter=new AdapterKnownFor(PeopleDetailsActivity.this, model.results, new OnRowClickListenerPeople() {
                            @Override
                            public void onRowClick(People people, int position) {

                            }
                        });
                        recyclerView.setAdapter(adapter);
                    }
                    else{

                    }
                }

                @Override
                public void onFailure(Call<PeopleModel> call, Throwable t) {

                }
            });

        }

        if(intent.hasExtra("SEARCH_PEOPLE")){
            Call<People> call2 = api.getPersonID(intent.getStringExtra("SEARCH_PEOPLE_ID"));
            call2.enqueue(new Callback<People>() {
                @Override
                public void onResponse(Call<People> call, Response<People> response) {
                    if(response.isSuccessful()) {
                        peopleModel = response.body();
                        nameActor.setText(peopleModel.getName());
                        Picasso.with(PeopleDetailsActivity.this)
                                .load("https://image.tmdb.org/t/p/w500/" + peopleModel.getProfile_path())
                                .into(imageActor);
                        birthday.setText("Born: "+peopleModel.getBirthday());
                        if(peopleModel.getDeathday()!=null){
                            deathday.setVisibility(View.VISIBLE);
                            deathday.setText("Died: "+ peopleModel.getDeathday());
                        }
                        biography.setText(peopleModel.getBiography());
                    }
                    else{
                        Toast.makeText(PeopleDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<People> call, Throwable t) {
                    Toast.makeText(PeopleDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });

            Call<PeopleModel> call3 = api.getPeople();
            call3.enqueue(new Callback<PeopleModel>() {
                @Override
                public void onResponse(Call<PeopleModel> call, Response<PeopleModel> response) {
                    if(response.isSuccessful()){
                        model=response.body();
                        adapter=new AdapterKnownFor(PeopleDetailsActivity.this, model.results, new OnRowClickListenerPeople() {
                            @Override
                            public void onRowClick(People people, int position) {

                            }
                        });
                        recyclerView.setAdapter(adapter);
                    }
                    else{

                    }
                }

                @Override
                public void onFailure(Call<PeopleModel> call, Throwable t) {

                }
            });
        }
    }



}
