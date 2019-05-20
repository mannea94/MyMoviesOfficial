package com.technology.manne.mymovies.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.technology.manne.mymovies.Adapters.AdapterKnownFor;
import com.technology.manne.mymovies.Api.RestApi;
import com.technology.manne.mymovies.Listener.OnClickListener;
import com.technology.manne.mymovies.Model.KnownFor;
import com.technology.manne.mymovies.Model.People;
import com.technology.manne.mymovies.Model.PeopleModel;
import com.technology.manne.mymovies.PreferenceManager;
import com.technology.manne.mymovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
//    @BindView(R.id.knownFor)
//    TextView knownFor;
    @BindView(R.id.knownFor2)
    TextView knownFor2;
    @BindView(R.id.died)
    TextView died;
    String bday="";
    String dday="";
    AdapterKnownFor adapter;
    RestApi api;
    People peopleModel;
    PeopleModel model;
    ArrayList<KnownFor> knownFors;
    int pos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_people_details);
        ButterKnife.bind(this);
        api=new RestApi(this);
        peopleModel=new People();
        knownFors=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(PeopleDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        died.setVisibility(View.GONE);
        deathday.setVisibility(View.GONE);
        final Intent intent = getIntent();

        if(intent.hasExtra("CAST_ID")){
//            knownFor.setVisibility(View.GONE);
            knownFor2.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }

        if(intent.hasExtra("CREW_ID")){
//            knownFor.setVisibility(View.GONE);
            knownFor2.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }

        api.checkInternet(new Runnable() {
            @Override
            public void run() {
                if(intent.hasExtra("EXTRA_PEOPLE")) {
                    Call<People> call = api.getPersonID(intent.getStringExtra("EXTRA_PEOPLE_ID"));
                    call.enqueue(new Callback<People>() {
                        @Override
                        public void onResponse(Call<People> call, Response<People> response) {
                            if (response.isSuccessful()) {
                                peopleModel = response.body();
                                nameActor.setText(peopleModel.getName());
                                if(peopleModel.getProfile_path()!=null){
                                    Picasso.with(PeopleDetailsActivity.this)
                                            .load("https://image.tmdb.org/t/p/w500/" + peopleModel.getProfile_path())
                                            .fit()
                                            .into(imageActor);
                                }
                                else{
                                    Picasso.with(PeopleDetailsActivity.this)
                                            .load(R.drawable.profle_pic)
                                            .into(imageActor);
                                }

                                bday=peopleModel.getBirthday();
                                birthday.setText(getBirthday());
                                dday=peopleModel.getDeathday();
                                if(dday!=null && !dday.isEmpty()){
                                    died.setVisibility(View.VISIBLE);
                                    deathday.setVisibility(View.VISIBLE);
                                    deathday.setText(getDeathday());
                                }
                                if(peopleModel.getBiography()!=null) {
                                    biography.setText(peopleModel.getBiography() + "\n");
                                }
                                else{
                                    biography.setText("");
                                }
                                String known="";
                                pos = intent.getIntExtra("EXTRA_PEOPLE", 0);
                                model=PreferenceManager.getPeople(PeopleDetailsActivity.this);
                                String title="";
                                String rating="";
                                String poster="";
                                String overview="";
                                String id="";
                                for(int i=0; i<model.results.get(pos).known_for.size(); i++){
//                                    known=known + model.results.get(pos).known_for.get(i).getTitle() + ", ";
                                    title=model.results.get(pos).known_for.get(i).getTitle();
                                    rating=model.results.get(pos).known_for.get(i).getVote_average();
                                    poster=model.results.get(pos).known_for.get(i).getPoster_path();
                                    overview=model.results.get(pos).known_for.get(i).getOverview();
                                    id=model.results.get(pos).known_for.get(i).getId();
                                    knownFors.add(new KnownFor(title, rating, poster, overview, id));
                                }
//                                knownFor.setText(known);
                                adapter=new AdapterKnownFor(PeopleDetailsActivity.this, new OnClickListener() {
                                    @Override
                                    public void OnRowClick(KnownFor knownFor, int position) {
                                        Intent intent = new Intent(PeopleDetailsActivity.this, MovieDetailsActivity.class);
                                        intent.putExtra("KNOWN", position);
                                        PreferenceManager.addKnownFor(knownFor, PeopleDetailsActivity.this);
                                        startActivity(intent);
                                    }
                                });
                                adapter.setItems(knownFors);
                                recyclerView.setAdapter(adapter);

                            }
                            else {
                            }
                        }

                        @Override
                        public void onFailure(Call<People> call, Throwable t) {
                            Toast.makeText(PeopleDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });




                }

                if(intent.hasExtra("CAST_ID")){
                    String id = intent.getStringExtra("CAST_ID");
                    Call<People> call = api.getPersonID(id);
                    call.enqueue(new Callback<People>() {
                        @Override
                        public void onResponse(Call<People> call, Response<People> response) {
                            if (response.isSuccessful()) {
                                peopleModel = response.body();
                                nameActor.setText(peopleModel.getName());
                                if(peopleModel.getProfile_path()!=null){
                                    Picasso.with(PeopleDetailsActivity.this)
                                            .load("https://image.tmdb.org/t/p/w500/" + peopleModel.getProfile_path())
                                            .into(imageActor);
                                }
                                else{
                                    Picasso.with(PeopleDetailsActivity.this)
                                            .load(R.drawable.profle_pic)
                                            .into(imageActor);
                                }
                                bday=peopleModel.getBirthday();
                                birthday.setText(getBirthday());
                                dday=peopleModel.getDeathday();
                                if(dday!=null && !dday.isEmpty()){
                                    died.setVisibility(View.VISIBLE);
                                    deathday.setVisibility(View.VISIBLE);
                                    deathday.setText(getDeathday());
                                }
                                if(peopleModel.getBiography()!=null) {
                                    biography.setText(peopleModel.getBiography() + "\n");
                                }
                                else{
                                    biography.setText("");
                                }


                            } else {
                            }
                        }

                        @Override
                        public void onFailure(Call<People> call, Throwable t) {
                            Toast.makeText(PeopleDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });


                }

                if(intent.hasExtra("CREW_ID")){
                    String id = intent.getStringExtra("CREW_ID");
                    Call<People> call = api.getPersonID(id);
                    call.enqueue(new Callback<People>() {
                        @Override
                        public void onResponse(Call<People> call, Response<People> response) {
                            if (response.isSuccessful()) {
                                peopleModel = response.body();
                                nameActor.setText(peopleModel.getName());
                                if(peopleModel.getProfile_path()!=null){
                                    Picasso.with(PeopleDetailsActivity.this)
                                            .load("https://image.tmdb.org/t/p/w500/" + peopleModel.getProfile_path())
                                            .into(imageActor);
                                }
                                else{
                                    Picasso.with(PeopleDetailsActivity.this)
                                            .load(R.drawable.profle_pic)
                                            .into(imageActor);
                                }
                                bday=peopleModel.getBirthday();
                                birthday.setText(getBirthday());
                                dday=peopleModel.getDeathday();
                                if(dday!=null && !dday.isEmpty()){
                                    died.setVisibility(View.VISIBLE);
                                    deathday.setVisibility(View.VISIBLE);
                                    deathday.setText(getDeathday());
                                }
                                if(peopleModel.getBiography()!=null) {
                                    biography.setText(peopleModel.getBiography() + "\n");
                                }
                                else{
                                    biography.setText("");
                                }


                            } else {
                            }
                        }

                        @Override
                        public void onFailure(Call<People> call, Throwable t) {
                            Toast.makeText(PeopleDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });


                }

                if(intent.hasExtra("SEARCH_PEOPLE")){
                    pos = intent.getIntExtra("SEARCH_PEOPLE", 0);
                    Call<People> call2 = api.getPersonID(intent.getStringExtra("SEARCH_PEOPLE_ID"));
                    call2.enqueue(new Callback<People>() {
                        @Override
                        public void onResponse(Call<People> call, Response<People> response) {
                            if(response.isSuccessful()) {
                                peopleModel = response.body();
                                nameActor.setText(peopleModel.getName());
                                if(peopleModel.getProfile_path()!=null){
                                    Picasso.with(PeopleDetailsActivity.this)
                                            .load("https://image.tmdb.org/t/p/w500/" + peopleModel.getProfile_path())
                                            .into(imageActor);
                                }
                                else{
                                    Picasso.with(PeopleDetailsActivity.this)
                                            .load(R.drawable.profle_pic)
                                            .into(imageActor);
                                }
                                bday=peopleModel.getBirthday();
                                birthday.setText(getBirthday());
                                dday=peopleModel.getDeathday();
                                if(dday!=null && !dday.isEmpty()){
                                    died.setVisibility(View.VISIBLE);
                                    deathday.setVisibility(View.VISIBLE);
                                    deathday.setText(getDeathday());
                                }
                                if(peopleModel.getBiography()!=null) {
                                    biography.setText(peopleModel.getBiography() + "\n");
                                }
                                else{
                                    biography.setText("");
                                }
                                model=PreferenceManager.getPeople(PeopleDetailsActivity.this);
                                String known="";
                                String title="";
                                String rating="";
                                String poster="";
                                String overview="";
                                String id="";
                                for(int i=0; i<model.results.get(pos).known_for.size(); i++){
//                                    known=known + model.results.get(pos).known_for.get(i).getTitle() + ", ";
                                    title=model.results.get(pos).known_for.get(i).getTitle();
                                    rating=model.results.get(pos).known_for.get(i).getVote_average();
                                    poster=model.results.get(pos).known_for.get(i).getPoster_path();
                                    overview=model.results.get(pos).known_for.get(i).getOverview();
                                    id=model.results.get(pos).known_for.get(i).getId();
                                    knownFors.add(new KnownFor(title, rating, poster, overview, id));
                                }


//                                knownFor.setText(known);
                                adapter=new AdapterKnownFor(PeopleDetailsActivity.this, new OnClickListener() {
                                    @Override
                                    public void OnRowClick(KnownFor knownFor, int position) {
                                        Intent intent = new Intent(PeopleDetailsActivity.this, MovieDetailsActivity.class);
                                        intent.putExtra("KNOWN", position);
                                        PreferenceManager.addKnownFor(knownFor, PeopleDetailsActivity.this);
                                        startActivity(intent);
                                    }
                                });
                                adapter.setItems(knownFors);
                                recyclerView.setAdapter(adapter);
                            }
                            else{
                            }
                        }

                        @Override
                        public void onFailure(Call<People> call, Throwable t) {
                            Toast.makeText(PeopleDetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

    }

    public String getBirthday(){
        String [] months;
        months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String current_month="";
        String official_month="";
        String current_day="";
        String official_day="";
        String official_year="";

        if(bday!=null && !bday.isEmpty() && bday.length()>4) {
            current_day=bday.substring(8, 10);
            if(current_day.substring(0, 1).equals("0")){
                current_day=bday.substring(9, 10);
            }
            else{
                current_day=bday.substring(8, 10);
            }
            official_day=current_day + ", ";
            official_year=bday.substring(0, 4);
            current_month = bday.substring(5, 7);
            if (current_month.substring(0, 1).equals("0")) {
                current_month = bday.substring(6, 7);
            } else {
                current_month = bday.substring(5, 7);
            }
            for (int i = 1; i <= 12; i++) {
                int month = Integer.valueOf(current_month);
                if (i == month) {
                    int pos = i - 1;
                    for (int j = 0; j < months.length; j++) {
                        if (j == pos) {
                            official_month = months[j] + " ";
                        }
                    }
                }
            }
        }
        else if(bday!=null && bday.length()<5){
            official_day="";
            official_month=bday;
            official_year="";
        }
        else{
            official_day="";
            official_month="";
            official_year="";

        }
        return official_month + official_day + official_year;
    }

    public String getDeathday(){
        String [] months;
        months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String current_month="";
        String official_month="";
        String current_day="";
        String official_day="";
        String official_year="";

        if(dday!=null && !dday.isEmpty() && dday.length()>4) {
            current_day=dday.substring(8, 10);
            if(current_day.substring(0, 1).equals("0")){
                current_day=dday.substring(9, 10);
            }
            else{
                current_day=dday.substring(8, 10);
            }
            official_day=current_day + ", ";
            official_year=dday.substring(0, 4);
            current_month = dday.substring(5, 7);
            if (current_month.substring(0, 1).equals("0")) {
                current_month = dday.substring(6, 7);
            } else {
                current_month = dday.substring(5, 7);
            }
            for (int i = 1; i <= 12; i++) {
                int month = Integer.valueOf(current_month);
                if (i == month) {
                    int pos = i - 1;
                    for (int j = 0; j < months.length; j++) {
                        if (j == pos) {
                            official_month = months[j] + " ";
                        }
                    }
                }
            }
        }
        else if(dday!=null && dday.length()<5){
            official_day="";
            official_month=dday;
            official_year="";
        }
        else{
            official_day="";
            official_month="";
            official_year="";

        }
        return official_month + official_day + official_year;
    }



}
