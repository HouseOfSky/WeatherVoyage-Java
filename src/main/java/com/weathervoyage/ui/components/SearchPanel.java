package com.weathervoyage.ui.components;

import com.weathervoyage.model.WeatherData;
import com.weathervoyage.ui.theme.ThemeManager;
import com.weathervoyage.ui.weather.WeatherDisplayPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.BiConsumer;

public class SearchPanel extends JPanel {
    private JTextField destinationField;
    private JButton searchButton;
    private JButton addToFavoritesButton;
    private JButton removeFromFavoritesButton;
    private JComboBox<String> searchTypeCombo;
    private WeatherDisplayPanel weatherDisplayPanel;
    private ThemeManager themeManager;
    private BiConsumer<String, String> searchListener;

    public SearchPanel(ThemeManager themeManager) {
        this.themeManager = themeManager;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        destinationField = new JTextField(20);
        destinationField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        searchTypeCombo = new JComboBox<>(new String[]{"Weather", "Air Quality", "Forecast"});
        searchTypeCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        
        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.PLAIN, 14));
        searchButton.addActionListener(e -> performSearch());
        
        addToFavoritesButton = new JButton("Add to Favorites");
        addToFavoritesButton.setFont(new Font("Arial", Font.PLAIN, 14));
        
        removeFromFavoritesButton = new JButton("Remove from Favorites");
        removeFromFavoritesButton.setFont(new Font("Arial", Font.PLAIN, 14));
        
        weatherDisplayPanel = new WeatherDisplayPanel(themeManager);
    }

    private void setupLayout() {
        // Search field and button in one line
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        searchPanel.add(destinationField);
        searchPanel.add(searchTypeCombo);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Weather display panel
        add(weatherDisplayPanel, BorderLayout.CENTER);

        // Favorite buttons
        JPanel favoritePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        favoritePanel.add(addToFavoritesButton);
        favoritePanel.add(removeFromFavoritesButton);
        add(favoritePanel, BorderLayout.SOUTH);
    }

    private void performSearch() {
        String query = destinationField.getText().trim();
        String searchType = searchTypeCombo.getSelectedItem().toString().toLowerCase();
        if (!query.isEmpty() && searchListener != null) {
            searchListener.accept(query, searchType);
        }
    }

    public void setSearchListener(BiConsumer<String, String> listener) {
        this.searchListener = listener;
    }

    public void updateWeatherDisplay(WeatherData data) {
        weatherDisplayPanel.displayWeatherData(data);
    }

    public void updateAirQualityDisplay(WeatherData data) {
        weatherDisplayPanel.displayWeatherData(data);
    }

    public void updateForecastDisplay(List<WeatherData> forecast) {
        if (!forecast.isEmpty()) {
            weatherDisplayPanel.displayWeatherData(forecast.get(0));
        }
    }

    public JTextField getDestinationField() {
        return destinationField;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public JButton getAddToFavoritesButton() {
        return addToFavoritesButton;
    }

    public JButton getRemoveFromFavoritesButton() {
        return removeFromFavoritesButton;
    }
} 