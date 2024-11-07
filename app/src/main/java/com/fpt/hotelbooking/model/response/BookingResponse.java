package com.fpt.hotelbooking.model.response;

import com.fpt.hotelbooking.model.Booking;

public class BookingResponse {
    private boolean isSucceed;
    private String message;
    private Booking data;
    private String paymentLink;




    public boolean isSucceed() {
        return isSucceed;
    }

    public void setSucceed(boolean isSucceed) {
        this.isSucceed = isSucceed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Booking getData() { // Updated to return BookingData
        return data;
    }

    public void setData(Booking data) {
        this.data = data;
    }
    public String getPaymentLink() { // Payment link from data object
        return data != null ? data.getPaymentLink() : null;
    }

    public void setPaymentLink(String paymentLink) {
        this.paymentLink = paymentLink;
    }
}
