package com.weathervoyage.ui.weather;

import com.weathervoyage.model.WeatherData;
import com.weathervoyage.ui.theme.ThemeManager;

import javax.swing.*;
import java.awt.*;

public class WeatherDisplayPanel extends JPanel {
    private JPanel mainContainer;
    private ThemeManager themeManager;

    public WeatherDisplayPanel(ThemeManager themeManager) {
        this.themeManager = themeManager;
        setLayout(new BorderLayout());
        initializeComponents();
    }

    private void initializeComponents() {
        mainContainer = new JPanel(new BorderLayout(10, 10));
        mainContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(mainContainer, BorderLayout.CENTER);
    }

    public void displayWeatherData(WeatherData data) {
        mainContainer.removeAll();
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel(data);
        mainContainer.add(headerPanel, BorderLayout.NORTH);
        
        // Center Panel
        JPanel centerPanel = createCenterPanel(data);
        mainContainer.add(centerPanel, BorderLayout.CENTER);
        
        // Footer Panel
        JPanel footerPanel = createFooterPanel();
        mainContainer.add(footerPanel, BorderLayout.SOUTH);
        
        mainContainer.revalidate();
        mainContainer.repaint();
    }

    private JPanel createHeaderPanel(WeatherData data) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel locationLabel = new JLabel(data.getLocation());
        locationLabel.setFont(themeManager.getTitleFont());
        
        JLabel dateLabel = new JLabel(data.getDate());
        dateLabel.setFont(themeManager.getDefaultFont());
        
        headerPanel.add(locationLabel, BorderLayout.WEST);
        headerPanel.add(dateLabel, BorderLayout.EAST);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        return headerPanel;
    }

    private JPanel createCenterPanel(WeatherData data) {
        JPanel centerPanel = new JPanel(new BorderLayout(15, 0));
        
        // Temperature Panel
        JPanel tempPanel = createTemperaturePanel(data);
        centerPanel.add(tempPanel, BorderLayout.CENTER);
        
        // Details Panel
        JPanel detailsPanel = createDetailsPanel(data);
        centerPanel.add(detailsPanel, BorderLayout.EAST);
        
        return centerPanel;
    }

    private JPanel createTemperaturePanel(WeatherData data) {
        JPanel tempPanel = new JPanel(new BorderLayout());
        tempPanel.setBackground(themeManager.getComponentBackgroundColor());
        tempPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(themeManager.getAccentColor(), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel tempLabel = new JLabel(String.format("%.1f°C", data.getTemperature()));
        tempLabel.setFont(themeManager.getTitleFont());
        tempPanel.add(tempLabel, BorderLayout.NORTH);
        
        JPanel conditionPanel = new JPanel(new BorderLayout(5, 0));
        conditionPanel.setOpaque(false);
        
        JLabel conditionLabel = new JLabel(data.getCondition());
        conditionLabel.setFont(themeManager.getHeaderFont());
        
        JLabel descriptionLabel = new JLabel(data.getDescription());
        descriptionLabel.setFont(themeManager.getDefaultFont());
        
        JLabel minMaxLabel = new JLabel(String.format("Min: %.1f°C / Max: %.1f°C", 
            data.getTempMin(), data.getTempMax()));
        minMaxLabel.setFont(themeManager.getDefaultFont());
        
        conditionPanel.add(conditionLabel, BorderLayout.NORTH);
        conditionPanel.add(descriptionLabel, BorderLayout.CENTER);
        conditionPanel.add(minMaxLabel, BorderLayout.SOUTH);
        
        tempPanel.add(conditionPanel, BorderLayout.CENTER);
        
        JLabel feelsLikeLabel = new JLabel(String.format("Feels like: %.1f°C", data.getFeelsLike()));
        feelsLikeLabel.setFont(themeManager.getDefaultFont());
        tempPanel.add(feelsLikeLabel, BorderLayout.SOUTH);
        
        return tempPanel;
    }

    private JPanel createDetailsPanel(WeatherData data) {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(themeManager.getComponentBackgroundColor());
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(themeManager.getAccentColor(), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        detailsPanel.add(createDetailItem("Humidity", data.getHumidity() + "%", "💧"));
        detailsPanel.add(Box.createVerticalStrut(8));
        detailsPanel.add(createDetailItem("Wind Speed", String.format("%.1f km/h", data.getWindSpeed()), "💨"));
        detailsPanel.add(Box.createVerticalStrut(8));
        detailsPanel.add(createDetailItem("Pressure", String.format("%.0f hPa", data.getPressure()), "🔄"));
        detailsPanel.add(Box.createVerticalStrut(8));
        detailsPanel.add(createDetailItem("Visibility", String.format("%.1f km", data.getVisibility()), "👁️"));
        
        return detailsPanel;
    }

    private JPanel createDetailItem(String label, String value, String icon) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(200, 30));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        panel.add(iconLabel, BorderLayout.WEST);
        
        JLabel nameLabel = new JLabel(label + ":");
        nameLabel.setFont(themeManager.getDefaultFont());
        panel.add(nameLabel, BorderLayout.CENTER);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(themeManager.getDefaultFont());
        panel.add(valueLabel, BorderLayout.EAST);
        
        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JLabel forecastLabel = new JLabel("Weather data provided by OpenWeatherMap");
        forecastLabel.setFont(themeManager.getDefaultFont());
        forecastLabel.setForeground(Color.GRAY);
        footerPanel.add(forecastLabel, BorderLayout.EAST);
        
        return footerPanel;
    }
} 