package com.weathervoyage.model;

import java.util.ArrayList;
import java.util.List;

public class UserPreferences {
    private String temperatureUnit = "°C";
    private String windSpeedUnit = "km/h";
    private List<String> favoriteLocations;
    private boolean receiveAlerts;
    private String theme = "Light";
    private String username;

    public UserPreferences() {
        this.favoriteLocations = new ArrayList<>();
        this.receiveAlerts = true;
    }

    // Getters and setters
    public String getTemperatureUnit() { return temperatureUnit; }
    public void setTemperatureUnit(String unit) { this.temperatureUnit = unit; }
    public String getWindSpeedUnit() { return windSpeedUnit; }
    public void setWindSpeedUnit(String unit) { this.windSpeedUnit = unit; }
    public List<String> getFavoriteLocations() { return new ArrayList<>(favoriteLocations); }
    public void addFavoriteLocation(String location) { favoriteLocations.add(location); }
    public void removeFavoriteLocation(String location) { favoriteLocations.remove(location); }
    public boolean isReceiveAlerts() { return receiveAlerts; }
    public void setReceiveAlerts(boolean receive) { this.receiveAlerts = receive; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
} 