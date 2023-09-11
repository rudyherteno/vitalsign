package com.clj.blesample;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.clj.blesample.api.ResponseGlobal;
import com.clj.blesample.api.APIInterface;
import com.clj.blesample.api.APIClient;
import com.clj.blesample.api.SessionManager;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText mUser, mPass;
    Button mButton;
    ProgressBar loading;
    APIInterface apiService;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUser = (EditText) findViewById(R.id.mUsername);
        mPass = (EditText) findViewById(R.id.mPassword);
        mButton = (Button) findViewById(R.id.mButton);
        loading = (ProgressBar) findViewById(R.id.loading_spinner);
        mButton.setOnClickListener(this);

    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mButton:
                login(mUser.getText().toString(), mPass.getText().toString());
                break;
        }
    }

    private void login(String user, String password) {
        loading.setVisibility(View.VISIBLE);
        mButton.setText("");
        mButton.setEnabled(false);
        apiService = APIClient.getClient().create(APIInterface.class);
        Call<ResponseGlobal> loginCall = apiService.doLogin(user, password);
        Log.d("login", loginCall.request().toString());
        loginCall.enqueue(new Callback<ResponseGlobal>() {
            @Override
            public void onResponse(Call<ResponseGlobal> call, Response<ResponseGlobal> response) {
                if(response.body() != null && response.isSuccessful() && response.body().isStatus()){

                    sessionManager = new SessionManager(LoginActivity.this);
//                    AlumniModel loginData = response.body().getData();
                    sessionManager.createLoginSession(user, password);
                    sessionManager.createUserSession(response.body().getData());
//
//                    Toast.makeText(LoginActivity.this, response.body().getData().getMhsNama(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
                Log.d("response", response.raw().toString());
                loading.setVisibility(View.INVISIBLE);
                mButton.setText("Log In");
                mButton.setEnabled(true);
            }

            @Override
            public void onFailure(Call<ResponseGlobal> call, Throwable t) {
                loading.setVisibility(View.INVISIBLE);
                mButton.setText("Log In");
                mButton.setEnabled(true);
                Toast.makeText(LoginActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}