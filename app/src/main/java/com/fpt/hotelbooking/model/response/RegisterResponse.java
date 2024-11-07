package com.fpt.hotelbooking.model.response;


public class RegisterResponse {
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
        private String userName;
        private String email;
        private String phoneNumber;

        public String getUserName() {
            return userName;
        }

        public String getEmail() {
            return email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }
    }
}
