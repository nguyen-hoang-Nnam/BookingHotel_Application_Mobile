package com.fpt.hotelbooking.network;

import com.fpt.hotelbooking.model.Hotel;
import com.fpt.hotelbooking.model.request.BookingRequest;
import com.fpt.hotelbooking.model.response.BookingResponse;
import com.fpt.hotelbooking.model.response.CountryResponse;
import com.fpt.hotelbooking.model.Room;
import com.fpt.hotelbooking.model.response.HotelResponse;
import com.fpt.hotelbooking.model.response.LoginRequest;
import com.fpt.hotelbooking.model.response.LoginResponse;
import com.fpt.hotelbooking.model.response.MyBookingResponse;
import com.fpt.hotelbooking.model.response.RegisterRequest;
import com.fpt.hotelbooking.model.response.RegisterResponse;
import com.fpt.hotelbooking.model.response.RoomResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("Country/GetAllCountries")
    Call<CountryResponse> getAllCountries();

    @GET("Hotel/GetByCountryId/{countryId}")
    Call<List<Hotel>> getHotelsByCountry(@Path("countryId") int countryId);

    @GET("Hotel/GetPaginatedHotels")
    Call<HotelResponse> getPaginatedHotels(@Query("Page") int page, @Query("Limit") int limit);

    @POST("/api/Auth/Login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("/api/Auth/Register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @GET("/api/Room/GetRoomByHotelTypeId/{hotelId}")
    Call<RoomResponse> getRoomsByHotelId(@Path("hotelId") int hotelId);

    @POST("/api/Booking/CreateBooking")
    Call<BookingResponse> createBooking(
            @Header("Authorization") String token,
            @Body BookingRequest bookingRequest
    );
    @GET("/api/Booking/GetBookingByUserId/{id}")
    Call<MyBookingResponse> getBookingsByUserId(@Header("Authorization") String token, @Path("id") String userId);
    @PUT("/api/Room/UpdateRoom/{id}")
    Call<Void> updateRoom(
            @Header("Authorization") String authToken,
            @Path("id") int roomId
    );



}
