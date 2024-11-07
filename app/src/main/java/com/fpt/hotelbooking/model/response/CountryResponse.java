package com.fpt.hotelbooking.model.response;

import com.fpt.hotelbooking.model.Country;

import java.util.List;

public class CountryResponse {
    private boolean isSucceed;
    private String message;
    private List<Country> data;

    public boolean isSucceed() {
        return isSucceed;
    }

    public String getMessage() {
        return message;
    }

    public List<Country> getData() {
        return data;
    }
}
