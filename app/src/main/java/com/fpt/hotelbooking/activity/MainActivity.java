package com.fpt.hotelbooking.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fpt.hotelbooking.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.up).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegisterActivity.class)));
        findViewById(R.id.in).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
        findViewById(R.id.later).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HomeActivity.class)));
    }
}