package com.clj.blesample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.clj.blesample.api.APIClient;
import com.clj.blesample.api.APIInterface;
import com.clj.blesample.api.ResponseGlobal;
import com.clj.blesample.api.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    SessionManager sessionManager;
    APIInterface apiService;
    ProgressBar loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loading = (ProgressBar) findViewById(R.id.splash_loading_spinner);
        final Handler handler = new Handler();
        sessionManager = new SessionManager(this);
        //if (!(sessionManager.getEmail().isEmpty() && sessionManager.getPassword().isEmpty())) {
            apiService = APIClient.getClient().create(APIInterface.class);
            Call<ResponseGlobal> loginCall = apiService.doLogin(sessionManager.getEmail(), sessionManager.getPassword());
            Log.d("login", loginCall.request().toString());
            loginCall.enqueue(new Callback<ResponseGlobal>() {
                @Override
                public void onResponse(Call<ResponseGlobal> call, Response<ResponseGlobal> response) {
                    if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {

                        sessionManager = new SessionManager(SplashActivity.this);
                        sessionManager.createLoginSession(sessionManager.getEmail(), sessionManager.getPassword());
                        sessionManager.createUserSession(response.body().getData());

                    } else {
                        Toast.makeText(SplashActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("response", response.raw().toString());
                }

                @Override
                public void onFailure(Call<ResponseGlobal> call, Throwable t) {
                    Toast.makeText(SplashActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        //}
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(sessionManager.isLoggedIn()){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
                finish();
            }
        }, 5000L);
    }
}