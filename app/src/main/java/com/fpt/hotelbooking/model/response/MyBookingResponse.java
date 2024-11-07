package com.fpt.hotelbooking.model.response;

import com.fpt.hotelbooking.model.Booking;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyBookingResponse {

    @SerializedName("isSucceed")
    private boolean isSucceed;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<Booking> data;

    public boolean isSucceed() {
        return isSucceed;
    }

    public void setSucceed(boolean succeed) {
        isSucceed = succeed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Booking> getData() {
        return data;
    }

    public void setData(List<Booking> data) {
        this.data = data;
    }

    // Getters and Setters
}
