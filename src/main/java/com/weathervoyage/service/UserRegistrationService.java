package com.weathervoyage.service;

import com.weathervoyage.model.User;
import java.util.regex.Pattern;

public class UserRegistrationService {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    
    private final UserService userService;

    public UserRegistrationService() {
        this.userService = new UserService();
    }

    public boolean registerUser(String name, String username, String password, String email) {
        if (!isValidRegistration(name, username, email, password)) {
            return false;
        }

        return userService.registerUser(name, username, password, email);
    }

    private boolean isValidRegistration(String name, String username, String email, String password) {
        if (name == null || username == null || email == null || password == null) {
            return false;
        }

        // Check name
        if (name.length() < 2 || name.length() > 100) {
            return false;
        }

        // Check username
        if (username.length() < 3 || username.length() > 50) {
            return false;
        }

        // Check email format
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return false;
        }

        // Check password strength
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return false;
        }

        // Check if username or email already exists
        try {
            User existingUser = userService.getUserByUsername(username);
            if (existingUser != null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }
} 