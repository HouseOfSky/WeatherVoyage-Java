CREATE DATABASE IF NOT EXISTS WeatherDB;
USE WeatherDB;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS search_history;
DROP TABLE IF EXISTS favorite_places;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create search_history table
CREATE TABLE search_history (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    search_query VARCHAR(255) NOT NULL,
    search_type VARCHAR(50) NOT NULL, -- 'WEATHER', 'AIR_QUALITY', 'FORECAST'
    search_result TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);