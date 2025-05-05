# WeatherVoyage Application Documentation

## Overview

WeatherVoyage is a Java Swing application that provides weather forecasts for travelers. The application allows users to log in, view weather forecasts for different locations, manage favorite locations, and customize their user experience.

## Project Structure

The project follows a standard Maven structure with the following organization:

```
/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── weathervoyage/
│                   ├── WeatherVoyageApp.java
│                   ├── LoginPanel.java
│                   ├── DashboardPanel.java
│                   ├── service/
│                   │   ├── UserService.java
│                   │   └── WeatherService.java
│                   ├── model/
│                   │   ├── User.java
│                   │   ├── WeatherData.java
│                   │   ├── UserPreferences.java
│                   │   └── WeatherAlert.java
│                   └── util/
│                       └── IconGenerator.java
├── pom.xml
```

## Core Files

### WeatherVoyageApp.java

The main application entry point that initializes the Swing UI.

#### Functions:

- `main(String[] args)`: Entry point of the application. Sets the UI look and feel, then calls `createAndShowGUI()`.
- `createAndShowGUI()`: Creates the main application window and initializes it with a login panel.

### LoginPanel.java

Provides the user login interface and authentication functionality.

### DashboardPanel.java

The main interface shown after successful login, displaying weather information and allowing user interaction.

## Services

### UserService.java

Manages user authentication and registration.

#### Functions:

- `authenticate(String username, String password)`: Validates a user's login credentials.
- `registerUser(String username, String password, String email)`: Creates a new user account with the provided details.

### WeatherService.java

Handles API communication to fetch weather data.

#### Functions:

- `getWeatherForecast(String location, String date)`: Fetches weather data for a specific location and date from the OpenWeather API.
  - Makes an HTTP request to the API
  - Parses the JSON response
  - Creates a WeatherData object containing all relevant weather information

## Models

### User.java

Represents a user of the application.

#### Properties:

- `username`: The user's login name
- `password`: The user's password
- `email`: The user's email address
- `preferredLocation`: User's preferred location for weather forecasts

#### Functions:

- `User(String username, String password, String email)`: Constructor that creates a new user.
- `validateInput(String input, String fieldName)`: Validates that input strings are not null or empty.
- Getters and setters for all properties.

### WeatherData.java

Contains weather information for a specific location and date.

#### Properties:

- `temperature`: The temperature in Celsius
- `humidity`: The humidity percentage
- `condition`: Text description of weather condition (e.g., "Clear", "Rain")
- `windSpeed`: Wind speed in km/h
- `location`: The location name
- `date`: The forecast date
- `feelsLike`: The "feels like" temperature
- `pressure`: Atmospheric pressure
- `visibility`: Visibility distance in km
- `weatherIcon`: Icon code for the weather condition
- `description`: Detailed weather description
- `tempMin`: Minimum temperature
- `tempMax`: Maximum temperature

#### Functions:

- `WeatherData(double temperature, double humidity, String condition, double windSpeed, String location, String date, double feelsLike, double pressure, double visibility, String weatherIcon, String description, double tempMin, double tempMax)`: Full constructor with all parameters.
- `WeatherData(double temperature, double humidity, String condition, double windSpeed, String location, String date)`: Backward compatibility constructor.
- Getters and setters for all properties.

### UserPreferences.java

Stores user customization settings.

#### Properties:

- `temperatureUnit`: User's preferred unit for temperature (default: "°C")
- `windSpeedUnit`: User's preferred unit for wind speed (default: "km/h")
- `favoriteLocations`: List of user's favorite locations
- `receiveAlerts`: Whether user wants to receive weather alerts
- `theme`: UI theme preference (default: "Light")

#### Functions:

- `UserPreferences()`: Default constructor that initializes properties.
- Getters and setters for all properties.
- `addFavoriteLocation(String location)`: Adds a location to favorites.
- `removeFavoriteLocation(String location)`: Removes a location from favorites.

### WeatherAlert.java

Contains information about weather alerts.

#### Properties:

- `location`: The affected location
- `alertType`: Type of alert (e.g., "Storm", "Flood")
- `description`: Detailed alert description
- `startTime`: Alert start date/time
- `endTime`: Alert end date/time
- `severity`: Alert severity level

#### Functions:

- `WeatherAlert(String location, String alertType, String description, LocalDateTime startTime, LocalDateTime endTime, String severity)`: Constructor with all parameters.
- Getters for all properties.

## Utilities

### IconGenerator.java

Generates placeholder icons for the application UI.

#### Functions:

- `generatePlaceholderIcons()`: Creates basic placeholder icons for UI elements and saves them to resources directory.

## Configuration

### pom.xml

Maven project configuration file.

#### Key Dependencies:

- JUnit (4.13.2): For unit testing
- Apache HttpClient (4.5.13): For API requests
- Jackson Databind (2.13.0): For JSON processing
- JFreeChart (1.5.3): For graphing weather data
- JCalendar (1.4): For date selection UI components