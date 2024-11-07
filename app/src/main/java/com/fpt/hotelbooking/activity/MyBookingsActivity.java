package com.fpt.hotelbooking.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fpt.hotelbooking.R;
import com.fpt.hotelbooking.adapter.BookingAdapter;
import com.fpt.hotelbooking.model.Booking;

import com.fpt.hotelbooking.model.response.MyBookingResponse;
import com.fpt.hotelbooking.network.ApiClient;
import com.fpt.hotelbooking.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBookingsActivity extends AppCompatActivity {

    private RecyclerView bookingsRecyclerView;
    private BookingAdapter bookingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        bookingsRecyclerView = findViewById(R.id.bookingsRecyclerView);
        bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get userId and token
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);
        String token = prefs.getString("token", null);

        if (userId == null || token == null) {
            Toast.makeText(this, "Please log in to view your bookings.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String authToken = "Bearer " + token;

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<MyBookingResponse> call = apiService.getBookingsByUserId(authToken, userId);

        call.enqueue(new Callback<MyBookingResponse>() {
            @Override
            public void onResponse(Call<MyBookingResponse> call, Response<MyBookingResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSucceed()) {
                    List<Booking> bookings = response.body().getData();
                    if (bookings != null && !bookings.isEmpty()) {
                        bookingAdapter = new BookingAdapter(bookings);
                        bookingsRecyclerView.setAdapter(bookingAdapter);
                    } else {
                        Toast.makeText(MyBookingsActivity.this, "You have no bookings.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MyBookingsActivity.this, "Failed to load bookings.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyBookingResponse> call, Throwable t) {
                Toast.makeText(MyBookingsActivity.this, "Error fetching bookings.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}
