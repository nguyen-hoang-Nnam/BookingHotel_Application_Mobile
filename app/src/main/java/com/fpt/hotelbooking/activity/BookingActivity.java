package com.fpt.hotelbooking.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.fpt.hotelbooking.R;
import com.fpt.hotelbooking.model.request.BookingRequest;
import com.fpt.hotelbooking.model.response.BookingResponse;
import com.fpt.hotelbooking.network.ApiClient;
import com.fpt.hotelbooking.network.ApiService;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookingActivity extends AppCompatActivity {

    protected Button btnSelectStartDate, btnSelectEndDate, btnBook;
    protected TextView totalPriceTextView, roomNameTextView, roomPriceTextView;
    private EditText userPhoneEditText;
    private Date startDate, endDate;
    private double pricePerDay;
    private int roomId;
    private String userId;
    private double totalCost;
    protected ImageView roomImage;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Initialize views
        btnSelectStartDate = findViewById(R.id.btnSelectStartDate);
        btnSelectEndDate = findViewById(R.id.btnSelectEndDate);
        btnBook = findViewById(R.id.btnBook);
        totalPriceTextView = findViewById(R.id.totalPrice);
        roomNameTextView = findViewById(R.id.roomName);
        roomPriceTextView = findViewById(R.id.roomPrice);
        userPhoneEditText = findViewById(R.id.userPhone);
        roomImage = findViewById(R.id.roomImage);
        // Get room details and pricePerDay from intent or set it directly
        Intent intent = getIntent();
        roomId = intent.getIntExtra("roomId", -1);
        String roomName = intent.getStringExtra("roomName");
        pricePerDay = intent.getFloatExtra("price", 0);
        Log.d("BookingActivity", "Price per day: " + pricePerDay);

        String imageUrl = getIntent().getStringExtra("roomImage");
        // Set room details
        roomNameTextView.setText(roomName);
        roomPriceTextView.setText("Price per day: " + pricePerDay + " VND");
        Glide.with(this).load(imageUrl).into(roomImage);
        // Get userId from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = prefs.getString("userId", null);

        btnSelectStartDate.setOnClickListener(v -> selectStartDate());
        btnSelectEndDate.setOnClickListener(v -> selectEndDate());

        btnBook.setOnClickListener(v -> {
            if (validateInput()) {
                int totalDays = calculateDays(startDate, endDate);
                totalCost = totalDays * pricePerDay;
                totalPriceTextView.setText("Total: " + totalCost + " VND");

                performBooking();
            }
        });
    }

    private void selectStartDate() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);
                    startDate = selectedDate.getTime();
                    btnSelectStartDate.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(startDate));

                    updateTotalPrice();
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void selectEndDate() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);
                    endDate = selectedDate.getTime();
                    btnSelectEndDate.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(endDate));

                    updateTotalPrice();
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void updateTotalPrice() {
        if (startDate != null && endDate != null) {
            if (endDate.before(startDate)) {
                totalPriceTextView.setText("End date must be after start date.");
            } else {
                int totalDays = calculateDays(startDate, endDate);
                totalCost = totalDays * pricePerDay;

                // Format to 2 decimal places for consistency
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                totalCost = Double.parseDouble(decimalFormat.format(totalCost));
                Log.d("BookingActivity", "Price per day: " + pricePerDay);
                Log.d("BookingActivity", "Total days: " + calculateDays(startDate, endDate));
                Log.d("BookingActivity", "Total cost (calculated): " + totalCost);

                totalPriceTextView.setText("Total: " + totalCost + " VND");
            }
        }
    }


    private boolean validateInput() {
        if (startDate == null || endDate == null) {
            Toast.makeText(this, "Please select both start and end dates.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (endDate.before(startDate)) {
            Toast.makeText(this, "End date must be after start date.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (userPhoneEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter your phone number.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private int calculateDays(Date startDate, Date endDate) {
        long diffInMillis = endDate.getTime() - startDate.getTime();
        return (int) TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) + 1;
    }

    private void performBooking() {
        // Format dates to ISO 8601 UTC
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String bookingDateStr = isoFormat.format(new Date());
        String checkInStr = isoFormat.format(startDate);
        String checkOutStr = isoFormat.format(endDate);

        // Round totalCost to two decimal places
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double roundedTotalCost = Double.parseDouble(decimalFormat.format(totalCost));

        // Create booking request
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setBookingDate(bookingDateStr);
        bookingRequest.setCheckIn(checkInStr);
        bookingRequest.setCheckOut(checkOutStr);
        bookingRequest.setTotalPrice((float) roundedTotalCost); // Explicitly cast to float
        bookingRequest.setBookingStatus(1); // Replace with actual status
        bookingRequest.setUserId(userId);
        bookingRequest.setRoomId(roomId);

        // Log request
        Gson gson = new Gson();
        String requestBody = gson.toJson(bookingRequest);
        Log.d("BookingRequestSent", "Request Body: " + requestBody);

        // Get auth token
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token == null) {
            showErrorDialog("Authentication token not found. Please log in again.");
            return;
        }

        String authToken = "Bearer " + token;

        // Call booking API
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<BookingResponse> call = apiService.createBooking(authToken, bookingRequest);

        call.enqueue(new Callback<BookingResponse>() {
            @Override
            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSucceed()) {
                    Log.d("BookingResponse", "Response: " + new Gson().toJson(response.body()));

                    // Get the payment link from the data object
                    String paymentLink = response.body().getPaymentLink();
                    Log.d("BookingActivity", "Received Payment Link: " + paymentLink);

                    if (paymentLink != null && !paymentLink.isEmpty()) {
                        // Start PaymentActivity and pass the payment link to it
                        Intent intent = new Intent(BookingActivity.this, PaymentActivity.class);
                        intent.putExtra("PAYMENT_LINK", paymentLink);
                        Log.d("BookingActivity", "Starting PaymentActivity");
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e("BookingActivity", "Payment link is empty or null.");
                        showErrorDialog("Payment link not found.");
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("BookingError", "Error: " + errorBody);
                        showErrorDialog("Booking failed: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                        showErrorDialog("Booking failed. Please try again.");
                    }
                }
            }

            @Override
            public void onFailure(Call<BookingResponse> call, Throwable t) {
                showErrorDialog("Error connecting to server. Please try again.");
                t.printStackTrace();
            }
        });
    }



    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null) // OK button to dismiss the dialog
                .show();
    }
    private void updateRoom(String authToken, int roomId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.updateRoom(authToken, roomId); // UpdateRoom API call

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(BookingActivity.this, "Room status updated successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("UpdateRoomError", "Error: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("UpdateRoomFailure", "Error: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void redirectToPayment(String paymentUrl) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl));
        startActivity(browserIntent);
    }

    private void showQRCode(String paymentLink) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(paymentLink, BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(bitMatrix);

            // Set the generated QR code to an ImageView in your layout
            ImageView qrCodeImageView = findViewById(R.id.qrCodeImageView); // Ensure you have an ImageView in your layout
            qrCodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

}