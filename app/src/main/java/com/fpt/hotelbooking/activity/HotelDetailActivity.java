package com.fpt.hotelbooking.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.fpt.hotelbooking.R;
import com.fpt.hotelbooking.adapter.RoomListAdapter;
import com.fpt.hotelbooking.model.Room;
import com.fpt.hotelbooking.model.response.RoomResponse;
import com.fpt.hotelbooking.network.ApiClient;
import com.fpt.hotelbooking.network.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelDetailActivity extends AppCompatActivity {

    protected ImageView hotelImage;
    protected TextView hotelName, hotelDescription, hotelAddress;
    protected RatingBar ratingBar;
    protected RecyclerView roomRecyclerView;
    protected RoomListAdapter roomListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

        hotelImage = findViewById(R.id.hotelImage);
        hotelName = findViewById(R.id.hotelName);
        hotelDescription = findViewById(R.id.hotelDescription);
        hotelAddress = findViewById(R.id.hotelAddress);
        ratingBar = findViewById(R.id.ratingBar);
        roomRecyclerView = findViewById(R.id.roomRecyclerView);

        roomRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Get hotel details
        String name = getIntent().getStringExtra("hotelName");
        String description = getIntent().getStringExtra("hotelDescription");
        String imageUrl = getIntent().getStringExtra("hotelImage");
        String address = getIntent().getStringExtra("hotelAddress");
        float ratings = getIntent().getFloatExtra("hotelRatings", 0);

        hotelName.setText(name);
        hotelDescription.setText(description);
        hotelAddress.setText(address);
        ratingBar.setRating(ratings);
        Glide.with(this).load(imageUrl).into(hotelImage);

        // Fetch rooms for this hotel
        int hotelId = getIntent().getIntExtra("hotelId", -1);
        fetchRoomsByHotelId(hotelId);


    }

    private void fetchRoomsByHotelId(int hotelId) {
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            Call<RoomResponse> call = apiService.getRoomsByHotelId(hotelId);
            call.enqueue(new Callback<RoomResponse>() {
                @Override
                public void onResponse(Call<RoomResponse> call, Response<RoomResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        RoomResponse roomResponse = response.body();

                        hotelName.setText(roomResponse.getHotelName());
                        hotelDescription.setText(roomResponse.getHotelDescription());
                        hotelAddress.setText(roomResponse.getAddress());
                        ratingBar.setRating(roomResponse.getRatings());
                        Glide.with(HotelDetailActivity.this).load(roomResponse.getImage()).into(hotelImage);

                        // Display room list in RecyclerView
                        List<Room> roomList = roomResponse.getRooms();
                        roomListAdapter = new RoomListAdapter(HotelDetailActivity.this, roomList);
                        roomRecyclerView.setAdapter(roomListAdapter);
                    } else {
                        Toast.makeText(HotelDetailActivity.this, "No rooms available for this hotel", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<RoomResponse> call, Throwable t) {
                    Toast.makeText(HotelDetailActivity.this, "Failed to load rooms: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
}
