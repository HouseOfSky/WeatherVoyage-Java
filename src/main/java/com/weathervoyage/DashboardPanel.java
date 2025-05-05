package com.weathervoyage;

import com.weathervoyage.model.User;
import com.weathervoyage.model.UserPreferences;
import com.weathervoyage.model.WeatherData;
import com.weathervoyage.service.UserService;
import com.weathervoyage.service.WeatherService;
import com.weathervoyage.service.UserHistoryService;
import com.weathervoyage.service.WeatherHistoryService;
import com.weathervoyage.ui.components.*;
import com.weathervoyage.ui.theme.DarkTheme;
import com.weathervoyage.ui.theme.LightTheme;
import com.weathervoyage.ui.theme.ThemeManager;
import com.weathervoyage.ui.weather.WeatherDisplayPanel;
import com.weathervoyage.ui.theme.DefaultThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DashboardPanel extends JPanel {
    private SearchPanel searchPanel;
    private WeatherDisplayPanel weatherDisplayPanel;
    private FavoritesPanel favoritesPanel;
    private AlertsPanel alertsPanel;
    private SideMenuPanel sideMenuPanel;
    private JTabbedPane tabbedPane;
    private WeatherService weatherService;
    private UserHistoryService historyService;
    private UserService userService;
    private User currentUser;
    private UserPreferences preferences;
    private ThemeManager themeManager;
    private JButton historyButton;
    private HistoryPanel historyPanel;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel buttonPanel;
    private String username;

    public DashboardPanel(String username) {
        this.username = username;
        this.userService = new UserService();
        this.weatherService = new WeatherService();
        this.themeManager = DefaultThemeManager.getInstance();
        this.preferences = new UserPreferences();
        
        setLayout(new BorderLayout());
        initializeComponents();
        setupLayout();
        loadUserPreferences();
    }

    private void initializeComponents() {
        mainPanel = new JPanel(new CardLayout());
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // Initialize search panel
        searchPanel = new SearchPanel(themeManager);
        searchPanel.setSearchListener(this::handleSearch);
        
        // Add History button
        historyButton = new JButton("History");
        historyButton.setFont(new Font("Arial", Font.PLAIN, 14));
        historyButton.addActionListener(e -> showHistoryPanel());
        
        // Initialize other components
        sideMenuPanel = new SideMenuPanel(themeManager, this::handleMenuAction);
        tabbedPane = new JTabbedPane();
    }

    private void setupLayout() {
        // Add components to main panel
        mainPanel.add(searchPanel, "search");
        if (historyPanel != null) {
            mainPanel.add(historyPanel, "history");
        }
        
        // Add buttons to button panel
        buttonPanel.add(historyButton);
        
        // Add panels to main layout
        add(sideMenuPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadUserPreferences() {
        currentUser = userService.getUserByUsername(username);
        preferences.setUsername(username);
        preferences.setTemperatureUnit(preferences.getTemperatureUnit());
        preferences.setWindSpeedUnit(preferences.getWindSpeedUnit());
        preferences.setTheme(preferences.getTheme());
        preferences.setReceiveAlerts(preferences.isReceiveAlerts());
        updateTheme();
    }

    private void showHistoryPanel() {
        if (historyPanel == null) {
            historyPanel = new HistoryPanel(themeManager, username);
            mainPanel.add(historyPanel, "history");
        }
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "history");
    }

    private void handleSearch(String query, String searchType) {
        switch (searchType) {
            case "weather":
                searchWeather(query);
                break;
            case "airquality":
                searchAirQuality(query);
                break;
            case "forecast":
                searchForecast(query);
                break;
        }
    }

    private void searchWeather(String query) {
        if (!query.isEmpty()) {
            try {
                WeatherData weatherData = weatherService.getWeatherForecast(query, null);
                if (weatherData != null) {
                    updateWeatherInfo(weatherData);
                    // Save to history
                    WeatherHistoryService historyService = new WeatherHistoryService();
                    historyService.saveWeatherSearch(query, "Weather", "Success");
                    recordSearchHistory(query, "Weather", "Success");
                } else {
                    JOptionPane.showMessageDialog(this, "Location not found", "Error", JOptionPane.ERROR_MESSAGE);
                    recordSearchHistory(query, "Weather", "Location not found");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error fetching weather data", "Error", JOptionPane.ERROR_MESSAGE);
                recordSearchHistory(query, "Weather", "Error: " + e.getMessage());
            }
        }
    }

    private void searchAirQuality(String query) {
        if (!query.isEmpty()) {
            try {
                WeatherData weatherData = weatherService.getWeatherForecast(query, null);
                if (weatherData != null) {
                    updateAirQualityInfo(weatherData);
                    // Save to history
                    WeatherHistoryService historyService = new WeatherHistoryService();
                    historyService.saveWeatherSearch(query, "Air Quality", "Success");
                    recordSearchHistory(query, "Air Quality", "Success");
                } else {
                    JOptionPane.showMessageDialog(this, "Location not found", "Error", JOptionPane.ERROR_MESSAGE);
                    recordSearchHistory(query, "Air Quality", "Location not found");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error fetching air quality data", "Error", JOptionPane.ERROR_MESSAGE);
                recordSearchHistory(query, "Air Quality", "Error: " + e.getMessage());
            }
        }
    }

    private void searchForecast(String query) {
        if (!query.isEmpty()) {
            try {
                WeatherData weatherData = weatherService.getWeatherForecast(query, null);
                if (weatherData != null) {
                    List<WeatherData> forecast = List.of(weatherData); // For now, just use current weather
                    updateForecastInfo(forecast);
                    // Save to history
                    WeatherHistoryService historyService = new WeatherHistoryService();
                    historyService.saveWeatherSearch(query, "Forecast", "Success");
                    recordSearchHistory(query, "Forecast", "Success");
                } else {
                    JOptionPane.showMessageDialog(this, "Location not found", "Error", JOptionPane.ERROR_MESSAGE);
                    recordSearchHistory(query, "Forecast", "Location not found");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error fetching forecast data", "Error", JOptionPane.ERROR_MESSAGE);
                recordSearchHistory(query, "Forecast", "Error: " + e.getMessage());
            }
        }
    }

    private void updateWeatherInfo(WeatherData data) {
        // Update weather display
        searchPanel.updateWeatherDisplay(data);
    }

    private void updateAirQualityInfo(WeatherData data) {
        // Update air quality display
        searchPanel.updateAirQualityDisplay(data);
    }

    private void updateForecastInfo(List<WeatherData> forecast) {
        // Update forecast display
        searchPanel.updateForecastDisplay(forecast);
    }

    private void handleMenuAction(ActionEvent e) {
        String action = ((JButton) e.getSource()).getText();
        switch (action) {
            case "Settings":
                showSettings();
                break;
            case "Logout":
                handleLogout();
                break;
            case "Plan Trip":
                showAIChatbot();
                break;
            case "Profile":
                showProfile();
                break;
            case "History":
                showWeatherHistory();
                break;
            case "Itinerary":
            case "Alerts":
                JOptionPane.showMessageDialog(this,
                    action + " feature coming soon!",
                    "Feature Not Available",
                    JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }

    private void showSettings() {
        JDialog settingsDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), 
            "Settings", true);
        settingsDialog.setLayout(new BorderLayout(10, 10));
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridwidth = 1;

        // Temperature unit
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Temperature Unit:"), gbc);
        JComboBox<String> tempUnit = new JComboBox<>(new String[]{"°C", "°F"});
        tempUnit.setSelectedItem(preferences.getTemperatureUnit());
        tempUnit.setPreferredSize(new Dimension(150, 25));
        gbc.gridx = 1;
        mainPanel.add(tempUnit, gbc);

        // Wind speed unit
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Wind Speed Unit:"), gbc);
        JComboBox<String> windUnit = new JComboBox<>(new String[]{"km/h", "mph"});
        windUnit.setSelectedItem(preferences.getWindSpeedUnit());
        windUnit.setPreferredSize(new Dimension(150, 25));
        gbc.gridx = 1;
        mainPanel.add(windUnit, gbc);

        // Theme
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Theme:"), gbc);
        JComboBox<String> theme = new JComboBox<>(new String[]{"Light", "Dark"});
        theme.setSelectedItem(preferences.getTheme());
        theme.setPreferredSize(new Dimension(150, 25));
        gbc.gridx = 1;
        mainPanel.add(theme, gbc);

        // Alerts
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Receive Alerts:"), gbc);
        JCheckBox alerts = new JCheckBox("", preferences.isReceiveAlerts());
        gbc.gridx = 1;
        mainPanel.add(alerts, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(100, 30));
        saveButton.addActionListener(e -> {
            preferences.setTemperatureUnit((String) tempUnit.getSelectedItem());
            preferences.setWindSpeedUnit((String) windUnit.getSelectedItem());
            preferences.setTheme((String) theme.getSelectedItem());
            preferences.setReceiveAlerts(alerts.isSelected());
            updateTheme();
            settingsDialog.dispose();
        });
        buttonPanel.add(saveButton);

        // Add panels to dialog
        settingsDialog.add(mainPanel, BorderLayout.CENTER);
        settingsDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Set dialog properties
        settingsDialog.setMinimumSize(new Dimension(300, 250));
        settingsDialog.setResizable(false);
        settingsDialog.pack();
        settingsDialog.setLocationRelativeTo(this);
        settingsDialog.setVisible(true);
    }

    private void updateTheme() {
        themeManager = preferences.getTheme().equals("Dark") ? new DarkTheme() : new LightTheme();
        // Update all components with new theme
        weatherDisplayPanel = new WeatherDisplayPanel(themeManager);
        favoritesPanel = new FavoritesPanel(themeManager, currentUser.getUsername());
        alertsPanel = new AlertsPanel(themeManager);
        sideMenuPanel = new SideMenuPanel(themeManager, this::handleMenuAction);
        
        // Update tabbed pane
        tabbedPane.removeAll();
        tabbedPane.addTab("Weather", weatherDisplayPanel);
        tabbedPane.addTab("Favorites", favoritesPanel);
        tabbedPane.addTab("Alerts", alertsPanel);
        
        // Refresh layout
        revalidate();
        repaint();
    }

    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Logout Confirmation",
            JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            Container parent = this.getParent();
            parent.removeAll();
            parent.add(new LoginPanel());
            parent.revalidate();
            parent.repaint();
        }
    }

    private void showAIChatbot() {
        String destination = searchPanel.getDestinationField().getText().trim();
        AIChatbotPanel chatbot = new AIChatbotPanel(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            themeManager,
            destination
        );
        chatbot.setVisible(true);
    }

    private void showProfile() {
        // Get current user from UserService
        UserService userService = new UserService();
        String username = preferences.getUsername(); // Assuming UserPreferences has getUsername()
        User currentUser = userService.getUserByUsername(username);
        
        if (currentUser != null) {
            ProfilePanel profilePanel = new ProfilePanel(themeManager, userService, currentUser);
            JDialog profileDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                "User Profile", true);
            profileDialog.setLayout(new BorderLayout());
            profileDialog.add(profilePanel, BorderLayout.CENTER);
            profileDialog.pack();
            profileDialog.setLocationRelativeTo(this);
            profileDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to load user profile.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showWeatherHistory() {
        JDialog historyDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
            "Weather Search History", true);
        historyDialog.setLayout(new BorderLayout());
        
        WeatherHistoryPanel historyPanel = new WeatherHistoryPanel(themeManager);
        historyDialog.add(historyPanel, BorderLayout.CENTER);
        
        historyDialog.pack();
        historyDialog.setLocationRelativeTo(this);
        historyDialog.setVisible(true);
    }

    // Add this method to record search history
    private void recordSearchHistory(String query, String searchType, String result) {
        if (historyPanel != null) {
            historyPanel.addSearchEntry(query, searchType, result);
        }
    }
} 