package com.weathervoyage.model;

import java.time.LocalDateTime;

public class WeatherAlert {
    private String location;
    private String alertType;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String severity;

    public WeatherAlert(String location, String alertType, String description, 
                       LocalDateTime startTime, LocalDateTime endTime, String severity) {
        this.location = location;
        this.alertType = alertType;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.severity = severity;
    }

    // Getters
    public String getLocation() { return location; }
    public String getAlertType() { return alertType; }
    public String getDescription() { return description; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getSeverity() { return severity; }
} 