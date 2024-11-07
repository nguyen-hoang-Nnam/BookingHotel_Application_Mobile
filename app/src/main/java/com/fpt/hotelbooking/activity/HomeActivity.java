package com.fpt.hotelbooking.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.fpt.hotelbooking.R;
import com.fpt.hotelbooking.adapter.CountryCarouselAdapter;
import com.fpt.hotelbooking.adapter.HotelCarouselAdapter;
import com.fpt.hotelbooking.model.Country;
import com.fpt.hotelbooking.model.Hotel;
import com.fpt.hotelbooking.model.response.CountryResponse;
import com.fpt.hotelbooking.model.response.HotelResponse;
import com.fpt.hotelbooking.network.ApiClient;
import com.fpt.hotelbooking.network.ApiService;
import com.fpt.hotelbooking.utils.CountryImageProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private ViewPager2 countryCarousel;
    private ViewPager2 hotelCarousel;
    private Spinner countrySpinner;
    private Button searchButton;
    private ArrayAdapter<String> countrySpinnerAdapter;
    private ArrayList<String> countryNames = new ArrayList<>();
    private ArrayList<Integer> countryIds = new ArrayList<>();
    private List<String> carouselCountryNames = new ArrayList<>();
    private List<String> carouselCountryImages = new ArrayList<>();  // Change to List<String>
    private List<Hotel> latestHotels = new ArrayList<>();
    private Handler handler;
    private Runnable countryRunnable, hotelRunnable;
    private int currentCountryPage = 0;
    private int currentHotelPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize UI elements
        TextView appTitle = findViewById(R.id.appNameHeader);
        countryCarousel = findViewById(R.id.countryCarousel);
        hotelCarousel = findViewById(R.id.hotelCarousel);
        countrySpinner = findViewById(R.id.spinnerCountry);
        searchButton = findViewById(R.id.btnSearch);
        // Initialize Handler
        handler = new Handler();

        // Set up Country Spinner Adapter
        countrySpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countryNames);
        countrySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countrySpinnerAdapter);

        // Button to perform search based on selected country
        searchButton.setOnClickListener(v -> {
            int selectedCountryPosition = countrySpinner.getSelectedItemPosition();
            if (selectedCountryPosition != -1) {
                int selectedCountryId = countryIds.get(selectedCountryPosition);
                showHotelsForCountry(selectedCountryId);
            } else {
                Toast.makeText(HomeActivity.this, "Please select a country", Toast.LENGTH_SHORT).show();
            }
        });

        // Load countries and latest rooms
        fetchCountries();
        fetchLatestRooms();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                // Home action
                return true;

            } else if (itemId == R.id.nav_my_booked) {
                SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
                if (isLoggedIn) {
                    Intent intent = new Intent(this, MyBookingsActivity.class);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                return true;
            } else if (itemId == R.id.nav_profile) {
                SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
                if (isLoggedIn) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.apply();

                    // Navigate to LoginActivity
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                return true;
            }

            return false;
        });

    }

    private void fetchCountries() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<CountryResponse> call = apiService.getAllCountries();
        call.enqueue(new Callback<CountryResponse>() {
            @Override
            public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSucceed()) {
                    List<Country> countries = response.body().getData();
                    for (Country country : countries) {
                        countryNames.add(country.getCountryName());
                        countryIds.add(country.getCountryId());

                        // Get a random image URL for the country and add it to the list as a String
                        String randomImageUrl = CountryImageProvider.getRandomImage();
                        carouselCountryNames.add(country.getCountryName());
                        carouselCountryImages.add(randomImageUrl);  // Store as String
                    }
                    countrySpinnerAdapter.notifyDataSetChanged();
                    setupCountryCarousel();
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to load countries", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CountryResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error fetching countries", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void setupCountryCarousel() {
        CountryCarouselAdapter adapter = new CountryCarouselAdapter(this, carouselCountryNames, carouselCountryImages, position -> {
            int selectedCountryId = countryIds.get(position);
            showHotelsForCountry(selectedCountryId);
        });
        countryCarousel.setAdapter(adapter);

        countryRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentCountryPage == carouselCountryNames.size()) {
                    currentCountryPage = 0;
                }
                countryCarousel.setCurrentItem(currentCountryPage++, true);
                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(countryRunnable, 2000);
    }

    private void fetchLatestRooms() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<HotelResponse> call = apiService.getPaginatedHotels(1, 5); // Page 1, Limit 5
        call.enqueue(new Callback<HotelResponse>() {
            @Override
            public void onResponse(Call<HotelResponse> call, Response<HotelResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSucceed()) {
                    latestHotels.clear();
                    latestHotels.addAll(response.body().getData().getHotels());  // Retrieve hotels list from data
                    if (latestHotels.isEmpty()) {
                        Toast.makeText(HomeActivity.this, "No latest hotels found", Toast.LENGTH_SHORT).show();
                    } else {
                        setupHotelCarousel();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to load latest rooms", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HotelResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error fetching latest rooms", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void setupHotelCarousel() {
        HotelCarouselAdapter adapter = new HotelCarouselAdapter(this, latestHotels, hotel -> {
            // Handle hotel item click
            // showHotelDetails(hotel);
        });
        hotelCarousel.setAdapter(adapter);

        hotelRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentHotelPage == latestHotels.size()) {
                    currentHotelPage = 0;
                }
                hotelCarousel.setCurrentItem(currentHotelPage++, true);
                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(hotelRunnable, 2000);
    }

    private void showHotelsForCountry(int countryId) {
        Intent intent = new Intent(HomeActivity.this, HotelListActivity.class);
        intent.putExtra("countryId", countryId);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove callbacks to prevent memory leaks
        if (handler != null) {
            handler.removeCallbacks(countryRunnable);
            handler.removeCallbacks(hotelRunnable);
        }
    }
}
