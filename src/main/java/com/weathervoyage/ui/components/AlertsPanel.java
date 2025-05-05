package com.weathervoyage.ui.components;

import com.weathervoyage.ui.theme.ThemeManager;

import javax.swing.*;
import java.awt.*;

public class AlertsPanel extends JPanel {
    private JTextArea alertsArea;
    private ThemeManager themeManager;

    public AlertsPanel(ThemeManager themeManager) {
        this.themeManager = themeManager;
        setLayout(new BorderLayout());
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        alertsArea = new JTextArea();
        alertsArea.setEditable(false);
        alertsArea.setLineWrap(true);
        alertsArea.setWrapStyleWord(true);
        alertsArea.setFont(themeManager.getDefaultFont());
    }

    private void setupLayout() {
        add(new JScrollPane(alertsArea), BorderLayout.CENTER);
    }

    public void addAlert(String alert) {
        alertsArea.append(alert + "\n");
        alertsArea.setCaretPosition(alertsArea.getDocument().getLength());
    }

    public void clearAlerts() {
        alertsArea.setText("");
    }

    public JTextArea getAlertsArea() {
        return alertsArea;
    }
} 