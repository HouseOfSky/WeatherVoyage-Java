package com.weathervoyage.model;

import com.fasterxml.jackson.databind.ser.std.StaticListSerializerBase;

public class WeatherData {
    private double temperature;
    private double humidity;
    private String condition;
    private double windSpeed;
    private String location;
    private String date;
    private double feelsLike;
    private double pressure;
    private double visibility;
    private String weatherIcon;
    private String description;
    private double tempMin;
    private double tempMax;
    private double latitude;
    private double longitude;

    public WeatherData(double temperature, double humidity, String condition, double windSpeed, 
                       String location, String date, double feelsLike, double pressure, 
                       double visibility, String weatherIcon, String description, 
                       double tempMin, double tempMax, double latitude, double longitude) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.condition = condition;
        this.windSpeed = windSpeed;
        this.location = location;
        this.date = date;
        this.feelsLike = feelsLike;
        this.pressure = pressure;
        this.visibility = visibility;
        this.weatherIcon = weatherIcon;
        this.description = description;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Backward compatibility constructor for existing code
    public WeatherData(double temperature, double humidity, String condition, double windSpeed, String location, String date) {
        this(temperature, humidity, condition, windSpeed, location, date, 0, 0, 0, "", "", 0, 0, 0, 0);
    }

    // Getters and setters
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    public double getHumidity() { return humidity; }
    public void setHumidity(double humidity) { this.humidity = humidity; }
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public double getFeelsLike() { return feelsLike; }
    public void setFeelsLike(double feelsLike) { this.feelsLike = feelsLike; }
    public double getPressure() { return pressure; }
    public void setPressure(double pressure) { this.pressure = pressure; }
    public double getVisibility() { return visibility; }
    public void setVisibility(double visibility) { this.visibility = visibility; }
    public String getWeatherIcon() { return weatherIcon; }
    public void setWeatherIcon(String weatherIcon) { this.weatherIcon = weatherIcon; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getTempMin() { return tempMin; }
    public void setTempMin(double tempMin) { this.tempMin = tempMin; }
    public double getTempMax() { return tempMax; }
    public void setTempMax(double tempMax) { this.tempMax = tempMax; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
} 

