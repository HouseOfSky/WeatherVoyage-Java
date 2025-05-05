package com.weathervoyage.ui.components;

import com.weathervoyage.ui.theme.ThemeManager;
import javax.swing.*;
import java.awt.*;

public class AdminPage extends JPanel {
    private ThemeManager themeManager;
    private JTable userTable;
    private JButton refreshButton;
    private JButton deleteButton;

    public AdminPage(ThemeManager themeManager) {
        this.themeManager = themeManager;
        setLayout(new BorderLayout(10, 10));
        initializeComponents();
        setupLayout();
        applyTheme();
    }

    private void initializeComponents() {
        // Initialize table with user data
        String[] columnNames = {"Username", "Email", "Role", "Last Login"};
        Object[][] data = {
            {"admin", "admin@example.com", "Administrator", "2024-03-15"},
            {"user1", "user1@example.com", "User", "2024-03-14"}
        };
        userTable = new JTable(data, columnNames);
        
        refreshButton = new JButton("Refresh");
        deleteButton = new JButton("Delete User");
    }

    private void setupLayout() {
        // Add table with scroll pane
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        // Add button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void applyTheme() {
        if (themeManager instanceof com.weathervoyage.ui.theme.DarkTheme) {
            Color darkBg = new Color(30, 30, 30);
            Color darkText = new Color(220, 220, 220);
            
            setBackground(darkBg);
            userTable.setBackground(darkBg);
            userTable.setForeground(darkText);
            userTable.getTableHeader().setBackground(new Color(50, 50, 50));
            userTable.getTableHeader().setForeground(darkText);
            
            refreshButton.setBackground(new Color(70, 130, 180));
            refreshButton.setForeground(darkText);
            deleteButton.setBackground(new Color(220, 53, 69));
            deleteButton.setForeground(darkText);
        }
    }
} 