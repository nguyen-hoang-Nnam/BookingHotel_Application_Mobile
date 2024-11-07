package com.fpt.hotelbooking.activity;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fpt.hotelbooking.R;
import com.fpt.hotelbooking.adapter.HotelListAdapter;
import com.fpt.hotelbooking.model.Hotel;
import com.fpt.hotelbooking.network.ApiClient;
import com.fpt.hotelbooking.network.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HotelListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_list);

        recyclerView = findViewById(R.id.recyclerViewHotels);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve data from intent
        Intent intent = getIntent();
        int countryId = intent.getIntExtra("countryId", -1);
        String startDate = intent.getStringExtra("startDate");
        String endDate = intent.getStringExtra("endDate");
        int peopleCount = intent.getIntExtra("peopleCount", 1);

        if (countryId != -1) {
            fetchHotels(countryId);
        } else {
            Toast.makeText(this, "Invalid country selection", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchHotels(int countryId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Hotel>> call = apiService.getHotelsByCountry(countryId);
        call.enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Hotel> hotels = response.body();
                    adapter = new HotelListAdapter(HotelListActivity.this, hotels);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Hotel>> call, @NonNull Throwable t) {
                Toast.makeText(HotelListActivity.this, "Failed to load hotels", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
