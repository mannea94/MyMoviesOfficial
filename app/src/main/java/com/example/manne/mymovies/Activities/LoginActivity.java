package com.example.manne.mymovies.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manne.mymovies.Api.RestApi;
import com.example.manne.mymovies.MainDrawerActivity;
import com.example.manne.mymovies.Model.RequestToken;
import com.example.manne.mymovies.PreferenceManager;
import com.example.manne.mymovies.R;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.loginButton)
    Button loginButton;
    @BindView(R.id.guest)
    Button guest;

    RestApi api;
    RequestToken token;
    String request_token="";
    String session_id="";
    String username_="";
    String password_="";
    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        api = new RestApi(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshToken();
                if(request_token!=null){
                    if(!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
                        if(!PreferenceManager.getSessionID(LoginActivity.this).isEmpty()) {
                            Intent intentID = new Intent(LoginActivity.this, MainDrawerActivity.class);
                            startActivity(intentID);
                            finish();
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        if(username.getText().toString().isEmpty() && password.getText().toString().isEmpty()){
                            username.setError("Please input username");
                            password.setError("Please input password");
                            Toast.makeText(LoginActivity.this, "Please input username and password", Toast.LENGTH_SHORT).show();
                        }
                        else if(username.getText().toString().isEmpty()){
                            username.setError("Please input username");
                            Toast.makeText(LoginActivity.this, "Please input username", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            password.setError("Please input password");
                            Toast.makeText(LoginActivity.this, "Please input password", Toast.LENGTH_SHORT).show();

                        }
                    }

                }

            }
        });

        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent guest = new Intent(LoginActivity.this, MainDrawerActivity.class);
                PreferenceManager.addSessionID("", LoginActivity.this);
                startActivity(guest);
                finish();
            }
        });


    }

    public void refreshToken(){
        Call<RequestToken> call = api.getRequestToken();
        call.enqueue(new Callback<RequestToken>() {
            @Override
            public void onResponse(Call<RequestToken> call, Response<RequestToken> response) {
                if(response.isSuccessful()) {
                    token = response.body();
                    request_token=token.getRequest_token();
                    username_=username.getText().toString();
                    password_=password.getText().toString();

                    Call<RequestToken> call2 = api.getSessionWithLogin(username_, password_, request_token);
                    call2.enqueue(new Callback<RequestToken>() {
                        @Override
                        public void onResponse(Call<RequestToken> call, Response<RequestToken> response) {
                            if(response.isSuccessful()){
                                token=response.body();
                                request_token=token.getRequest_token();
                                PreferenceManager.addToken(request_token, LoginActivity.this);
                                flag=0;
                                Call<RequestToken> call3 = api.getSessionToken(request_token);
                                call3.enqueue(new Callback<RequestToken>() {
                                    @Override
                                    public void onResponse(Call<RequestToken> call, Response<RequestToken> response) {
                                        if(response.isSuccessful()) {
                                            token = response.body();
                                            session_id=token.getSession_id();
                                            PreferenceManager.addSessionID(session_id, LoginActivity.this);
                                        }
                                        else{
                                            Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<RequestToken> call, Throwable t) {
                                        Toast.makeText(LoginActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else{

                                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RequestToken> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<RequestToken> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Something went WRONG", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
