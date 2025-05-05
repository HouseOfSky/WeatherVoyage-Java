package com.weathervoyage.service;

import com.weathervoyage.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    // Add own database password!
    private static final String DB_URL = "jdbc:mysql://localhost:3306/WeatherDB";
    private static final String DB_USER = "";
    private static final String DB_PASS = "";
    

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public boolean authenticate(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerUser(String name, String username, String password, String email) {
        String insert = "INSERT INTO users (name, username, password, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(insert)) {
            stmt.setString(1, name);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, email);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Registration Error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUser(User user) {
        String update = "UPDATE users SET name = ?, email = ?, username = ? WHERE username = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(update)) {
            stmt.setString(1, user.getfirstName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getUsername());
            stmt.setString(4, user.getUsername()); // Original username for WHERE clause
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Update Error: " + e.getMessage());
            e.printStackTrace(); // Add this for debugging
            return false;
        }
    }

    public boolean changePassword(String username, String currentPassword, String newPassword) throws SQLException {
        // First verify the current password
        if (!authenticate(username, currentPassword)) {
            return false;
        }

        // Update to new password
        String update = "UPDATE users SET password = ? WHERE username = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(update)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Password Change Error: " + e.getMessage());
            e.printStackTrace(); // Add this for debugging
            throw e;
        }
    }

    public User getUserByUsername(String username) {
        String query = "SELECT id, name, username, password, email FROM users WHERE username = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User(
                    rs.getString("name"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email")
                );
                user.setId(rs.getInt("id"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT name, username, password, email FROM users";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                users.add(new User(
                    rs.getString("name"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email")
                ));
            }
        }
        return users;
    }

    public boolean deleteUser(String username) throws SQLException {
        String delete = "DELETE FROM users WHERE username = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(delete)) {
            stmt.setString(1, username);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean updateUserField(String username, String fieldName, String newValue) {
        String update = "UPDATE users SET " + fieldName + " = ? WHERE username = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(update)) {
            stmt.setString(1, newValue);
            stmt.setString(2, username);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Update Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUsername(String oldUsername, String newUsername) {
        String update = "UPDATE users SET username = ? WHERE username = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(update)) {
            stmt.setString(1, newUsername);
            stmt.setString(2, oldUsername);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Username Update Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
