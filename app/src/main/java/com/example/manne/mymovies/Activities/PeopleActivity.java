package com.example.manne.mymovies.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manne.mymovies.Adapters.AdapterPeople;
import com.example.manne.mymovies.Api.RestApi;
import com.example.manne.mymovies.Listener.OnRowClickListenerPeople;
import com.example.manne.mymovies.Model.MovieModel;
import com.example.manne.mymovies.Model.People;
import com.example.manne.mymovies.Model.PeopleModel;
import com.example.manne.mymovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeopleActivity extends AppCompatActivity {
    @BindView(R.id.recyclerViewPeople)
    RecyclerView recyclerView;
    @BindView(R.id.search_field)
    EditText search;
    AdapterPeople adapter;
    PeopleModel peopleModel;
    RestApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        peopleModel=new PeopleModel();
        api=new RestApi(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(search.getText().length()>=3){
                    searchPerson(search.getText().toString());
                }
            }
        });


        Call<PeopleModel> call = api.getPeople();
        call.enqueue(new Callback<PeopleModel>() {
            @Override
            public void onResponse(Call<PeopleModel> call, Response<PeopleModel> response) {
                if(response.code()==200){
                    peopleModel=response.body();
                    adapter=new AdapterPeople(PeopleActivity.this, peopleModel.results, new OnRowClickListenerPeople() {
                        @Override
                        public void onRowClick(People people, int position) {
                            Intent intent = new Intent(PeopleActivity.this, PeopleDetailsActivity.class);
                            intent.putExtra("EXTRA_PEOPLE_ID", peopleModel.results.get(position).getId());
                            intent.putExtra("EXTRA_PEOPLE", "EXTRA_PEOPLE");
//                            intent.putExtra("EXTRA_PEOPLE_POSITION", position);
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(adapter);

                }
                else if(response.code()==401){
                    Toast.makeText(PeopleActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PeopleModel> call, Throwable t) {
                Toast.makeText(PeopleActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void searchPerson(final String person){
        Call<PeopleModel> call = api.getPersonSearch(person);
        call.enqueue(new Callback<PeopleModel>() {
            @Override
            public void onResponse(Call<PeopleModel> call, Response<PeopleModel> response) {
                if(response.code()==200){
                    peopleModel=response.body();
                    adapter=new AdapterPeople(PeopleActivity.this, peopleModel.results, new OnRowClickListenerPeople() {
                        @Override
                        public void onRowClick(People people, int position) {
                            Intent intent = new Intent(PeopleActivity.this, PeopleDetailsActivity.class);
                            intent.putExtra("SEARCH_PEOPLE", "SEARCH_PEOPLE");
                            intent.putExtra("SEARCH_PEOPLE_ID", peopleModel.results.get(position).getId());
//                            intent.putExtra("SEARCH_PEOPLE_POSITION", position);
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                    if(peopleModel.results==null || peopleModel.results.size()==0){
                        Toast.makeText(PeopleActivity.this, "No such person", Toast.LENGTH_SHORT).show();
                    }

                }
                else if(response.code()==401){
                    Toast.makeText(PeopleActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PeopleModel> call, Throwable t) {
                Toast.makeText(PeopleActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
