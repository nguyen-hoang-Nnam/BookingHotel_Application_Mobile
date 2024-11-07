package com.fpt.hotelbooking.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.auth0.android.jwt.JWT;
import com.fpt.hotelbooking.R;
import com.fpt.hotelbooking.model.response.LoginRequest;
import com.fpt.hotelbooking.model.response.LoginResponse;
import com.fpt.hotelbooking.network.ApiClient;
import com.fpt.hotelbooking.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText edEmail, edPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        edEmail = findViewById(R.id.edSingInEmail);
        edPassword = findViewById(R.id.edSignInPassword);
        progressBar = findViewById(R.id.progress);

        TextView btnSignIn = findViewById(R.id.btn_SignIn);
        btnSignIn.setOnClickListener(view -> validateAndLogin());
    }

    private void validateAndLogin() {
        String email = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<LoginResponse> call = apiService.login(new LoginRequest(email, password));

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().isSucceed()) {
                    String token = response.body().getData().getToken();
                    String userId = extractUserIdFromToken(token);

                    if (userId != null) {
                        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("token", token);
                        editor.putString("userId", userId); // Save userId extracted from token
                        editor.apply();

                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                        // Navigate to HomeActivity
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        showErrorDialog("Failed to extract user ID from token.");
                    }
                } else {
                    showErrorDialog("Login failed. Please check your credentials.");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showErrorDialog("Error connecting to server. Please try again.");
                t.printStackTrace();
            }
        });
    }

    private String extractUserIdFromToken(String token) {
        try {
            JWT jwt = new JWT(token);
            String userId = jwt.getClaim("nameid").asString();
            return userId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
