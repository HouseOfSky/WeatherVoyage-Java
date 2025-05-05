package com.weathervoyage.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserHistoryService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/WeatherDB";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Akash@#2003";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    // Search History Methods
    public void addSearchHistory(int userId, String query, String searchType, String result) {
        String insert = "INSERT INTO search_history (user_id, search_query, search_type, search_result) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(insert)) {
            stmt.setInt(1, userId);
            stmt.setString(2, query);
            stmt.setString(3, searchType);
            stmt.setString(4, result);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding search history: " + e.getMessage());
        }
    }

    public List<SearchHistoryEntry> getSearchHistory(int userId, int limit) {
        List<SearchHistoryEntry> history = new ArrayList<>();
        String query = "SELECT * FROM search_history WHERE user_id = ? ORDER BY timestamp DESC LIMIT ?";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                history.add(new SearchHistoryEntry(
                    rs.getInt("id"),
                    rs.getString("search_query"),
                    rs.getString("search_type"),
                    rs.getString("search_result"),
                    rs.getTimestamp("timestamp")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching search history: " + e.getMessage());
        }
        return history;
    }

    public void clearSearchHistory(int userId) {
        String delete = "DELETE FROM search_history WHERE user_id = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(delete)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error clearing search history: " + e.getMessage());
        }
    }

    // Favorite Places Methods
    public boolean addFavoritePlace(int userId, String placeName, String placeType) {
        String insert = "INSERT INTO favorite_places (user_id, place_name, place_type) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(insert)) {
            System.out.println("Adding favorite place: " + placeName + " for user ID: " + userId);
            stmt.setInt(1, userId);
            stmt.setString(2, placeName);
            stmt.setString(3, placeType);
            stmt.executeUpdate();
            System.out.println("Successfully added favorite place");
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Duplicate entry error
                System.out.println("Duplicate entry error for favorite place");
                return false;
            }
            System.err.println("Error adding favorite place: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeFavoritePlace(int userId, String placeName) {
        String delete = "DELETE FROM favorite_places WHERE user_id = ? AND place_name = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(delete)) {
            stmt.setInt(1, userId);
            stmt.setString(2, placeName);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error removing favorite place: " + e.getMessage());
            return false;
        }
    }

    public List<FavoritePlace> getFavoritePlaces(int userId) {
        List<FavoritePlace> favorites = new ArrayList<>();
        String query = "SELECT * FROM favorite_places WHERE user_id = ? ORDER BY added_at DESC";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            System.out.println("Executing query: " + query + " for user ID: " + userId);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                FavoritePlace place = new FavoritePlace(
                    rs.getInt("id"),
                    rs.getString("place_name"),
                    rs.getString("place_type"),
                    rs.getTimestamp("added_at")
                );
                System.out.println("Found favorite place: " + place.getPlaceName());
                favorites.add(place);
            }
            System.out.println("Total favorite places found: " + favorites.size());
        } catch (SQLException e) {
            System.err.println("Error fetching favorite places: " + e.getMessage());
            e.printStackTrace();
        }
        return favorites;
    }

    public boolean isFavoritePlace(int userId, String placeName) {
        String query = "SELECT COUNT(*) FROM favorite_places WHERE user_id = ? AND place_name = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, placeName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking favorite place: " + e.getMessage());
        }
        return false;
    }

    // Data classes
    public static class SearchHistoryEntry {
        private final int id;
        private final String query;
        private final String searchType;
        private final String result;
        private final Timestamp timestamp;

        public SearchHistoryEntry(int id, String query, String searchType, String result, Timestamp timestamp) {
            this.id = id;
            this.query = query;
            this.searchType = searchType;
            this.result = result;
            this.timestamp = timestamp;
        }

        // Getters
        public int getId() { return id; }
        public String getQuery() { return query; }
        public String getSearchType() { return searchType; }
        public String getResult() { return result; }
        public Timestamp getTimestamp() { return timestamp; }
    }

    public static class FavoritePlace {
        private final int id;
        private final String placeName;
        private final String placeType;
        private final Timestamp addedAt;

        public FavoritePlace(int id, String placeName, String placeType, Timestamp addedAt) {
            this.id = id;
            this.placeName = placeName;
            this.placeType = placeType;
            this.addedAt = addedAt;
        }

        // Getters
        public int getId() { return id; }
        public String getPlaceName() { return placeName; }
        public String getPlaceType() { return placeType; }
        public Timestamp getAddedAt() { return addedAt; }
    }
} 