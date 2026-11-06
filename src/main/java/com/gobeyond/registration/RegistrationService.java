package com.gobeyond.registration;

public class RegistrationService {
    public String register(String name, String email) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (email == null || !email.matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid email address");
        }
        return "Registration successful for " + name;
    }
}