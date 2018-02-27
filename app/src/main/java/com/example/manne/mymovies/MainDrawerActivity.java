package com.example.manne.mymovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manne.mymovies.Activities.FavouritesActivity;
import com.example.manne.mymovies.Activities.LoginActivity;
import com.example.manne.mymovies.Activities.PeopleActivity;
import com.example.manne.mymovies.Activities.RatedActivity;
import com.example.manne.mymovies.Activities.WatchlistActivity;
import com.example.manne.mymovies.Adapters.Adapter;
import com.example.manne.mymovies.Adapters.FragmentAdapter;
import com.example.manne.mymovies.Api.RestApi;
import com.example.manne.mymovies.Fragments.NowPlayingFragment;
import com.example.manne.mymovies.Fragments.PopularFragment;
import com.example.manne.mymovies.Fragments.TopRatedFragment;
import com.example.manne.mymovies.Fragments.UpcomingFragment;
import com.example.manne.mymovies.Model.MovieModel;
import com.example.manne.mymovies.Model.MyMovies;
import com.example.manne.mymovies.Model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    public MovieModel movieModel;

    String [] activityTitles;
    String sessionID="";
    String token="";
    String userID="";
    User user;
    RestApi api;
    int index=0;
    public ArrayList<MyMovies> favorites;
    public ArrayList<MyMovies> rated;
    public ArrayList<MyMovies> watchlist;
    ImageView imageView;
    TextView name;
    TextView username;
    String itemLog="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        userID=PreferenceManager.getUserID(this);
        sessionID=PreferenceManager.getSessionID(this);
        token=PreferenceManager.getToken(this);
//        user=new User();
        activityTitles=getResources().getStringArray(R.array.string_array);
        api=new RestApi(this);
        movieModel = new MovieModel();
//        model = new MovieModel();
        favorites = new ArrayList<>();
        rated = new ArrayList<>();
        watchlist = new ArrayList<>();


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        setUpViewPager(pager);
        tabLayout.setupWithViewPager(pager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        username = (TextView) view.findViewById(R.id.userName);
        name  = (TextView) view.findViewById(R.id.textView);

        Menu menu = navigationView.getMenu();
        MenuItem logout_item = menu.findItem(R.id.nav_login);

        if(!token.isEmpty()){
            logout_item.setTitle("LOGOUT");
            itemLog="Logout";
        }
        else{
            logout_item.setTitle("LOGIN");
            itemLog="Login";
        }



    }

    @Override
    protected void onResume() {
        if(!sessionID.isEmpty()){

            Call<User> call = api.getUserDetails(sessionID);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()){
                        user=response.body();
                        Picasso.with(MainDrawerActivity.this)
                                .load("http://www.gravatar.com/avatar/"+user.avatar.gravatar.hash)
                                .into(imageView);
                        username.setText(user.username);
                        name.setText(user.name);
                        PreferenceManager.setUserID(user.getId(), MainDrawerActivity.this);
                    }
                    else{
                        Toast.makeText(MainDrawerActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(MainDrawerActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Picasso.with(MainDrawerActivity.this)
                    .load(R.drawable.user)
                    .into(imageView);
            name.setText("Guest");
            username.setText("Please login");
        }

        Call <MovieModel> call = api.getFavouriteMovies(userID, sessionID);
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {

                if(response.isSuccessful()){
                    movieModel=response.body();

                        for(int i=0; i<movieModel.results.size(); i++) {
                            favorites.add(i, movieModel.results.get(i));
                        }

                }
                else{

                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });

        Call <MovieModel> call2 = api.getRatedMovies(userID, sessionID);
        call2.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {

                if(response.isSuccessful()){
                    movieModel=response.body();

                    for(int i=0; i<movieModel.results.size(); i++) {
                        rated.add(i, movieModel.results.get(i));
                    }

                }
                else{

                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });

        Call <MovieModel> call3 = api.getWatchlistMovies(userID, sessionID);
        call3.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {

                if(response.isSuccessful()){
                    movieModel=response.body();

                    for(int i=0; i<movieModel.results.size(); i++) {
                        watchlist.add(i, movieModel.results.get(i));
                    }

                }
                else{

                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });
        super.onResume();
    }

    public void setUpViewPager(ViewPager viewPager){
        FragmentAdapter adapter = new FragmentAdapter(this.getSupportFragmentManager());
        adapter.addFragment(new PopularFragment(), "POPULAR");
        adapter.addFragment(new TopRatedFragment(), "TOP RATED");
        adapter.addFragment(new UpcomingFragment(), "UPCOMING");
        adapter.addFragment(new NowPlayingFragment(), "NOW PLAYING");
        pager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_drawer, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

//        if(id==R.id.nav_login){
//            if(!sessionID.equals("")) {
//                item.setTitle("Logout");
//            }
//            else{
//                item.setTitle("Login");
//            }
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void setToolbarTitle(){
        getSupportActionBar().setTitle(activityTitles[index]);
        invalidateOptionsMenu();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_explorer) {
            // Handle the camera action
            Intent intent = new Intent(MainDrawerActivity.this, MainDrawerActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_favourites) {
            if(!sessionID.equals("")) {
                Intent intent = new Intent(MainDrawerActivity.this, FavouritesActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(this, "Please login", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_rated) {
            if(!sessionID.equals("")) {
                Intent intent = new Intent(MainDrawerActivity.this, RatedActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(this, "Please login", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_watchlist) {
            if(!sessionID.equals("")) {
                Intent intent = new Intent(MainDrawerActivity.this, WatchlistActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(this, "Please login", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_people) {
            Intent intent = new Intent(this, PeopleActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_login) {
                if(itemLog.equals("Logout")) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    PreferenceManager.addSessionID("", MainDrawerActivity.this);
                    PreferenceManager.addToken("", MainDrawerActivity.this);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
