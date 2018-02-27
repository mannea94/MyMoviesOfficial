package com.example.manne.mymovies.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manne.mymovies.Activities.MovieDetailsActivity;
import com.example.manne.mymovies.Adapters.Adapter;
import com.example.manne.mymovies.Api.RestApi;
import com.example.manne.mymovies.Listener.OnRowClickListener;
import com.example.manne.mymovies.MainDrawerActivity;
import com.example.manne.mymovies.Model.MovieModel;
import com.example.manne.mymovies.Model.MyMovies;
import com.example.manne.mymovies.PreferenceManager;
import com.example.manne.mymovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by manne on 06.2.2018.
 */

public class PopularFragment extends Fragment {
    public Unbinder mUnBinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.search_field)
    EditText search;
    Adapter adapter;
    MovieModel movieModel;
    RestApi api;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popular_fragment, null);
        mUnBinder = ButterKnife.bind(this, view);
        api=new RestApi(getActivity());
        movieModel=new MovieModel();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(search.getText().length()>=2){
                    searchMovies(search.getText().toString());
                }
                else{
                    Call<MovieModel> call = api.getMovies();
                    call.enqueue(new Callback<MovieModel>() {
                        @Override
                        public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                            if(response.code()==200){
                                movieModel=response.body();
                                adapter=new Adapter(getContext(), movieModel, new OnRowClickListener() {
                                    @Override
                                    public void onRowClick(MyMovies movies, int position) {
                                        Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                                        intent.putExtra("EXTRA_ID", movieModel.results.get(position).getId());
                                        intent.putExtra("EXTRA1", position);
                                        startActivity(intent);
                                    }
                                });
                                recyclerView.setAdapter(adapter);


                            }
                            else if(response.code()==401){
                                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MovieModel> call, Throwable t) {
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        api.checkInternet(new Runnable() {
            @Override
            public void run() {
                Call<MovieModel> call = api.getMovies();
                call.enqueue(new Callback<MovieModel>() {
                    @Override
                    public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                        if(response.code()==200){
                            movieModel=response.body();
                            for(int i=0; i<movieModel.results.size(); i++){
                                for(int j=0; j<((MainDrawerActivity)getActivity()).favorites.size(); j++) {
                                    if ((((MainDrawerActivity) getActivity()).favorites.get(j).getId()).equals(movieModel.results.get(i).getId())) {
                                        movieModel.results.get(i).favorite = true;
                                    }
                                }
                                for(int x=0; x<((MainDrawerActivity)getActivity()).rated.size(); x++) {
                                    if (movieModel.results.get(i).getId().equals(((MainDrawerActivity) getActivity()).rated.get(x).getId())) {
                                        movieModel.results.get(i).rated = true;
                                    }
                                }
                                for (int y = 0; y < ((MainDrawerActivity) getActivity()).watchlist.size(); y++) {
                                    if (movieModel.results.get(i).getId().equals(((MainDrawerActivity) getActivity()).watchlist.get(y).getId())) {
                                        movieModel.results.get(i).watchlist = true;
                                    }
                                }
                            }
                            adapter=new Adapter(getContext(), movieModel, new OnRowClickListener() {
                                @Override
                                public void onRowClick(MyMovies movies, int position) {
                                    Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                                    intent.putExtra("EXTRA_ID", movieModel.results.get(position).getId());
                                    intent.putExtra("EXTRA1", position);
                                    startActivity(intent);
                                }
                            });
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }
                        else if(response.code()==401){
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieModel> call, Throwable t) {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        return view;
    }



    public void searchMovies(final String stringMovie){
        api.checkInternet(new Runnable() {
            @Override
            public void run() {
                Call<MovieModel> call = api.getMovieSearch(stringMovie);
                call.enqueue(new Callback<MovieModel>() {
                    @Override
                    public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                        if(response.code()==200){
                            movieModel=response.body();
                            PreferenceManager.addMovies(movieModel, getActivity());
                            adapter=new Adapter(getContext(), movieModel, new OnRowClickListener() {
                                @Override
                                public void onRowClick(MyMovies movies, int position) {
                                    Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                                    intent.putExtra("SEARCH_1", "SEARCH_1");
                                    intent.putExtra("SEARCH_POS1", position);
                                    intent.putExtra("SEARCH_ID1", movieModel.results.get(position).getId());
                                    startActivity(intent);
                                }
                            });
                            recyclerView.setAdapter(adapter);
                            if(movieModel.results==null || movieModel.results.size()==0){
                                Toast.makeText(getContext(), "No such Movie", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else if(response.code()==401){
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieModel> call, Throwable t) {
                        Toast.makeText(getContext(), "Something went WRONG", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
    }
}
