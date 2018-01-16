package com.finiq.mediaplayer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.finiq.mediaplayer.network.NetworkCalls;
import com.finiq.mediaplayer.pojo.RegisterUserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    public static final String Base_Url = "http://finiqwebapp.azurewebsites.net";
    AutoCompleteTextView mName, mUsername, mPassword;
    TextView skipTextView, closeButton;
    Button registerButton;

    String id, name, username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Hiding ActionBar on register activity
        ActionBar myActionBar = getSupportActionBar();
        myActionBar.hide();

        registerButton = (Button) findViewById(R.id.register_buttton);
        mName = (AutoCompleteTextView) findViewById(R.id.editText_name);
        mUsername = (AutoCompleteTextView) findViewById(R.id.editText_username);
        mPassword = (AutoCompleteTextView) findViewById(R.id.editText_password);
        closeButton = (TextView) findViewById(R.id.imageButton_close);
        skipTextView = (TextView) findViewById(R.id.skipButton);

        mName.setText("kuldeep");
        mUsername.setText("kuldeep74");
        mPassword.setText("02051996");

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
        skipTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }
    //to check internet connectivity is there or not
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void login(View v) {
        if(!isNetworkAvailable()) {
            Toast.makeText(LoginActivity.this,
                    "No Internet Connection",
                    Toast.LENGTH_SHORT).show();
        } else
        if(!mName.getText().toString().equals("") &&!mUsername.getText().toString().equals("") && !mPassword.getText().toString().equals("")) {
                Retrofit login = new Retrofit.Builder()
                        .baseUrl(Base_Url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                NetworkCalls services = login.create(NetworkCalls.class);
                final Call<RegisterUserResponse> registerUser = services.registerUser(mName.getText().toString(),mUsername.getText().toString(),
                        mPassword.getText().toString());
                //The network call was first skipping 143 frames (according to Choreographer)
                //After making the network                                                                                                 call in a separate thread rather than the main thread
                //the the number of frames skipped reduced to 53! (HENCE FASTER)
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        registerUser.enqueue(new Callback<RegisterUserResponse>() {
                            @Override
                            public void onResponse(Call<RegisterUserResponse> call, Response<RegisterUserResponse> response) {


                                if (response.isSuccessful() && response.body() != null) {
                                    Toast.makeText(LoginActivity.this,
                                            "Login Success",
                                            Toast.LENGTH_SHORT).show();

                                    id = response.body().getId().toString();
                                    name = response.body().getName().toString();
                                    username = response.body().getUsername().toString();
                                    password = response.body().getPassword().toString();
                                    Log.d("responseID.......", id);
                                    Log.d("Name.......", name);
                                    Log.d("UserName.......", username);
                                    Log.d("Password.......", password);
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                                }
                                else
                                    Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_SHORT ).show();

                            }

                            @Override
                            public void onFailure(Call<RegisterUserResponse> call, Throwable t) {
                                try {
                                    Toast.makeText(LoginActivity.this, t.getCause().getMessage(), Toast.LENGTH_SHORT).show();
                                } catch (NullPointerException e) {
                                    Toast.makeText(LoginActivity.this,
                                            "Login failed. \nPlease check Username & Password.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).run();

        } else {
            Toast.makeText(this, "Please enter Name, Username and Password", Toast.LENGTH_SHORT).show();
        }
    }
}

