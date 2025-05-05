package com.weathervoyage.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.weathervoyage.model.WeatherData;
import org.json.JSONObject;
import org.json.JSONArray;

public class WeatherService {
    // Replace this with your actual OpenWeatherMap API key
    private static final String API_KEY = "Add Open Weather API Key";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast";

    public WeatherData getWeatherForecast(String location, String date) {
        try {
            // Build the API URL
            String urlString = String.format("%s?q=%s&appid=%s&units=metric", BASE_URL, location, API_KEY);
            URL url = new URL(urlString);

            // Make the API request
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            // Check for error response
            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Weather API returned error code: " + responseCode);
            }

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse the JSON response
            JSONObject json = new JSONObject(response.toString());
            
            // Get the first forecast entry (current weather)
            JSONObject firstForecast = json.getJSONArray("list").getJSONObject(0);
            JSONObject main = firstForecast.getJSONObject("main");
            JSONObject weather = firstForecast.getJSONArray("weather").getJSONObject(0);
            JSONObject city = json.getJSONObject("city");
            JSONObject coord = city.getJSONObject("coord");

            // Extract weather data
            double temperature = main.getDouble("temp");
            double feelsLike = main.getDouble("feels_like");
            double tempMin = main.getDouble("temp_min");
            double tempMax = main.getDouble("temp_max");
            int humidity = main.getInt("humidity");
            double pressure = main.getDouble("pressure");
            String condition = weather.getString("main");
            String description = weather.getString("description");
            double windSpeed = firstForecast.getJSONObject("wind").getDouble("speed");
            double visibility = firstForecast.getDouble("visibility") / 1000.0; // Convert to kilometers
            double latitude = coord.getDouble("lat");
            double longitude = coord.getDouble("lon");

            // Format the date
            String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));

            return new WeatherData(
                temperature, humidity, condition, windSpeed,
                location, formattedDate, feelsLike, pressure,
                visibility, "", description, tempMin, tempMax,
                latitude, longitude
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch weather data: " + e.getMessage());
        }
    }
} 