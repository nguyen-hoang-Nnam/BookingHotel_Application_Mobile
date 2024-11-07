package com.fpt.hotelbooking.model.response;

import com.fpt.hotelbooking.model.Hotel;
import java.util.List;

public class HotelResponse {
    private boolean isSucceed;
    private String message;
    private HotelData data;

    public boolean isSucceed() {
        return isSucceed;
    }

    public String getMessage() {
        return message;
    }

    public HotelData getData() {
        return data;
    }

    public static class HotelData {
        private List<Hotel> hotels;

        public List<Hotel> getHotels() {
            return hotels;
        }
    }
}
