package com.weathervoyage.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WeatherHistoryService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/WeatherDB";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Akash@#2003";

    static {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Create table on startup
            createWeatherHistoryTable();
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        }
    }

    private static void createWeatherHistoryTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS weather_history (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "location VARCHAR(255) NOT NULL," +
            "search_type VARCHAR(50) NOT NULL," +
            "result TEXT NOT NULL," +
            "timestamp DATETIME NOT NULL" +
            ")";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Weather history table created successfully");
        } catch (SQLException e) {
            System.err.println("Error creating weather history table: " + e.getMessage());
        }
    }

    public void saveWeatherSearch(String location, String searchType, String result) {
        String query = "INSERT INTO weather_history (location, search_type, result, timestamp) VALUES (?, ?, ?, NOW())";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, location);
            stmt.setString(2, searchType);
            stmt.setString(3, result);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving weather search: " + e.getMessage());
        }
    }

    public List<WeatherHistoryEntry> getRecentSearches(int limit) {
        List<WeatherHistoryEntry> history = new ArrayList<>();
        String query = "SELECT * FROM weather_history ORDER BY timestamp DESC LIMIT ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                history.add(new WeatherHistoryEntry(
                    rs.getInt("id"),
                    rs.getString("location"),
                    rs.getString("search_type"),
                    rs.getString("result"),
                    rs.getTimestamp("timestamp")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching weather history: " + e.getMessage());
        }
        return history;
    }

    public void clearHistory() {
        String query = "DELETE FROM weather_history";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error clearing weather history: " + e.getMessage());
        }
    }

    private static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            throw e;
        }
    }

    public static class WeatherHistoryEntry {
        private final int id;
        private final String location;
        private final String searchType;
        private final String result;
        private final Timestamp timestamp;

        public WeatherHistoryEntry(int id, String location, String searchType, String result, Timestamp timestamp) {
            this.id = id;
            this.location = location;
            this.searchType = searchType;
            this.result = result;
            this.timestamp = timestamp;
        }

        public int getId() { return id; }
        public String getLocation() { return location; }
        public String getSearchType() { return searchType; }
        public String getResult() { return result; }
        public Timestamp getTimestamp() { return timestamp; }
    }
} 