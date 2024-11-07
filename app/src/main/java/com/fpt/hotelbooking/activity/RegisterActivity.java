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

import com.fpt.hotelbooking.R;
import com.fpt.hotelbooking.model.response.RegisterRequest;
import com.fpt.hotelbooking.model.response.RegisterResponse;
import com.fpt.hotelbooking.network.ApiClient;
import com.fpt.hotelbooking.network.ApiService;
import com.fpt.hotelbooking.utils.InputValidator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText edEmail, edUsername, edPassword, edConfirmPassword, edPhone;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edEmail = findViewById(R.id.edSingUpEmail);
        edUsername = findViewById(R.id.edSingUpUsername);
        edPassword = findViewById(R.id.edSignUpPassword);
        edConfirmPassword = findViewById(R.id.edConfirmSignUpPassword);
        edPhone = findViewById(R.id.edtPhone);
        progressBar = findViewById(R.id.progress);

        TextView btnSignUp = findViewById(R.id.tv_SignUp_btn);
        btnSignUp.setOnClickListener(view -> validateAndRegister());
    }

    private void validateAndRegister() {
        String email = edEmail.getText().toString().trim();
        String username = edUsername.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        String confirmPassword = edConfirmPassword.getText().toString().trim();
        String phone = edPhone.getText().toString().trim();

        // Validate input fields
        if (!InputValidator.isValidEmail(email)) {
            showErrorDialog("Invalid email format.");
            return;
        }
        if (!InputValidator.isValidPhone(phone)) {
            showErrorDialog("Invalid phone number format.");
            return;
        }
        if (!InputValidator.isValidPassword(password)) {
            showErrorDialog("Password must be at least 6 characters.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showErrorDialog("Passwords do not match.");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Make API request for registration
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        RegisterRequest request = new RegisterRequest(username, email, phone, password);
        Call<RegisterResponse> call = apiService.register(request);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().isSucceed()) {
                    // Save login status in SharedPreferences
                    SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.apply();

                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    // Navigate to HomeActivity
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showErrorDialog("Registration failed. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showErrorDialog("Error connecting to server. Please try again.");
                t.printStackTrace();
            }
        });
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
