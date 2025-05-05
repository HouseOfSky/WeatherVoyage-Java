package com.weathervoyage.model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String Name;
    private String username;
    private String password;
    private String email;
    private String preferredLocation;

    public User(String firstName, String username, String password, String email) {
        validateInput(firstName, "Name");
        validateInput(username, "Username");
        validateInput(password, "Password");
        validateInput(email, "Email");

        this.Name = firstName.trim();
        this.username = username.trim();
        this.password = password;
        this.email = email.trim();
    }

    private void validateInput(String input, String fieldName) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
    }
    

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getfirstName() { return Name; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getPreferredLocation() { return preferredLocation; }

    public void setPassword(String password) {
        validateInput(password, "Password");
        this.password = password;
    }

    public void setEmail(String email) {
        validateInput(email, "Email");
        this.email = email.trim();
    }

    public void setPreferredLocation(String location) {
        this.preferredLocation = location != null ? location.trim() : null;
    }

    public void setUsername(String username) {
        validateInput(username, "Username");
        this.username = username.trim();
    }

    public void setName(String name) {
        validateInput(name, "Name");
        this.Name = name.trim();
    }
} 