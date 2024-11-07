package com.fpt.hotelbooking.model.response;


public class LoginResponse {
    private boolean isSucceed;
    private String message;
    private Data data;

    public boolean isSucceed() {
        return isSucceed;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        private String token;
        private String refreshToken;

        public String getToken() {
            return token;
        }

        public String getRefreshToken() {
            return refreshToken;
        }
    }
}
