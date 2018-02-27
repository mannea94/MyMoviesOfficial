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

public class NowPlayingFragment extends Fragment {
    public Unbinder mUnBinder;
    @BindView(R.id.recyclerViewNowPlaying)
    RecyclerView recyclerView;
    @BindView(R.id.search_field)
    EditText search;
    Adapter adapter;
    MovieModel movieModel;
    RestApi api;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nowplaying_fragment, null);
        mUnBinder = ButterKnife.bind(this, view);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        api=new RestApi(getActivity());
        movieModel=new MovieModel();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
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
                    Call<MovieModel> call = api.getNowPlaying();
                    call.enqueue(new Callback<MovieModel>() {
                        @Override
                        public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                            if(response.code()==200){
                                movieModel=response.body();
                                adapter=new Adapter(getContext(), movieModel, new OnRowClickListener() {
                                    @Override
                                    public void onRowClick(MyMovies movies, int position) {
                                        Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                                        intent.putExtra("EXTRA_4", "EXTRA_4");
                                        intent.putExtra("EXTRA4", position);
                                        intent.putExtra("EXTRA_ID4", movieModel.results.get(position).getId());
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

        Call<MovieModel> call = api.getNowPlaying();
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if(response.code()==200){
                    movieModel=response.body();
                    adapter=new Adapter(getContext(), movieModel, new OnRowClickListener() {
                        @Override
                        public void onRowClick(MyMovies movies, int position) {
                            Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                            intent.putExtra("EXTRA_4", "EXTRA_4");
                            intent.putExtra("EXTRA4", position);
                            intent.putExtra("EXTRA_ID4", movieModel.results.get(position).getId());
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
        return view;
    }

    public void searchMovies(String stringMovie) {
        Call<MovieModel> call = api.getMovieSearch(stringMovie);
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.code() == 200) {
                    movieModel = response.body();
                    PreferenceManager.addMovies(movieModel, getActivity());
                    adapter = new Adapter(getContext(), movieModel, new OnRowClickListener() {
                        @Override
                        public void onRowClick(MyMovies movies, int position) {
                            Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                            intent.putExtra("SEARCH_4", "SEARCH_4");
                            intent.putExtra("SEARCH_POS4", position);
                            intent.putExtra("SEARCH_ID4", movieModel.results.get(position).getId());
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                    if (movieModel.results == null || movieModel.results.size() == 0) {
                        Toast.makeText(getContext(), "No such Movie", Toast.LENGTH_SHORT).show();
                    }

                } else if (response.code() == 401) {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
    }
}
