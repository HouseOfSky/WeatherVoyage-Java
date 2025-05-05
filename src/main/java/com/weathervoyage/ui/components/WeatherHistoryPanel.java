package com.weathervoyage.ui.components;

import com.weathervoyage.service.WeatherHistoryService;
import com.weathervoyage.ui.theme.ThemeManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class WeatherHistoryPanel extends JPanel {
    private final JTable historyTable;
    private final DefaultTableModel tableModel;
    private final WeatherHistoryService historyService;
    private final ThemeManager themeManager;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public WeatherHistoryPanel(ThemeManager themeManager) {
        this.themeManager = themeManager;
        this.historyService = new WeatherHistoryService();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create table model
        String[] columns = {"Location", "Search Type", "Result", "Timestamp"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        historyTable = new JTable(tableModel);
        historyTable.setFont(themeManager.getDefaultFont());
        historyTable.setRowHeight(25);
        historyTable.getTableHeader().setFont(themeManager.getDefaultFont());
        
        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Refresh");
        JButton clearButton = new JButton("Clear History");
        
        refreshButton.addActionListener(e -> refreshHistory());
        clearButton.addActionListener(e -> clearHistory());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(clearButton);
        
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Load initial data
        refreshHistory();
        applyTheme();
    }

    public void refreshHistory() {
        tableModel.setRowCount(0);
        List<WeatherHistoryService.WeatherHistoryEntry> history = historyService.getRecentSearches(50);
        
        for (WeatherHistoryService.WeatherHistoryEntry entry : history) {
            tableModel.addRow(new Object[]{
                entry.getLocation(),
                entry.getSearchType(),
                entry.getResult(),
                DATE_FORMAT.format(entry.getTimestamp())
            });
        }
    }

    private void clearHistory() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to clear the weather search history?",
            "Clear History",
            JOptionPane.YES_NO_OPTION
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            historyService.clearHistory();
            refreshHistory();
        }
    }

    private void applyTheme() {
        Color bgColor = themeManager.getBackgroundColor();
        Color textColor = themeManager.getForegroundColor();
        Color headerBg = themeManager.getComponentBackgroundColor();
        
        setBackground(bgColor);
        historyTable.setBackground(bgColor);
        historyTable.setForeground(textColor);
        historyTable.getTableHeader().setBackground(headerBg);
        historyTable.getTableHeader().setForeground(textColor);
        historyTable.setGridColor(themeManager.getAccentColor());
    }
} 