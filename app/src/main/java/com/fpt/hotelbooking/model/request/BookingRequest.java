package com.fpt.hotelbooking.model.request;

import com.google.gson.annotations.SerializedName;

public class BookingRequest {

    @SerializedName("bookingDate")
    private String bookingDate;

    @SerializedName("checkIn")
    private String checkIn;

    @SerializedName("checkOut")
    private String checkOut;

    @SerializedName("totalPrice")
    private double totalPrice;

    @SerializedName("bookingStatus")
    private int bookingStatus;

    @SerializedName("userId")
    private String userId;

    @SerializedName("roomId")
    private int roomId;

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(int bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
