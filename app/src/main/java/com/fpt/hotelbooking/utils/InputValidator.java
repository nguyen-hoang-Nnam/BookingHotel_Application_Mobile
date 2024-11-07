package com.fpt.hotelbooking.utils;

import android.util.Patterns;
public class InputValidator {
    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && Patterns.PHONE.matcher(phone).matches();
    }
}
