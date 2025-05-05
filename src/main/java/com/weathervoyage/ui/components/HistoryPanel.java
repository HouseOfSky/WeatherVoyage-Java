package com.weathervoyage.ui.components;

import com.weathervoyage.model.User;
import com.weathervoyage.service.UserHistoryService;
import com.weathervoyage.service.UserService;
import com.weathervoyage.ui.theme.ThemeManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class HistoryPanel extends JPanel {
    private JTable historyTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton clearButton;
    private ThemeManager themeManager;
    private UserHistoryService historyService;
    private UserService userService;
    private String username;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public HistoryPanel(ThemeManager themeManager, String username) {
        this.themeManager = themeManager;
        this.username = username;
        this.historyService = new UserHistoryService();
        this.userService = new UserService();
        setLayout(new BorderLayout());
        initializeComponents();
        setupLayout();
        loadHistory();
    }

    private void initializeComponents() {
        // Create table model with columns
        String[] columns = {"Date", "Search Type", "Query", "Result"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        historyTable = new JTable(tableModel);
        historyTable.setFont(new Font("Arial", Font.PLAIN, 12));
        historyTable.setRowHeight(25);
        historyTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // Set column widths
        historyTable.getColumnModel().getColumn(0).setPreferredWidth(150); // Date
        historyTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Search Type
        historyTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Query
        historyTable.getColumnModel().getColumn(3).setPreferredWidth(300); // Result

        refreshButton = new JButton("Refresh");
        clearButton = new JButton("Clear History");
        
        // Style buttons
        refreshButton.setFont(new Font("Arial", Font.PLAIN, 12));
        clearButton.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Add action listeners
        refreshButton.addActionListener(e -> loadHistory());
        clearButton.addActionListener(e -> clearHistory());
    }

    private void setupLayout() {
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add title label
        JLabel titleLabel = new JLabel("Search History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Add scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(scrollPane, BorderLayout.CENTER);
        
        // Add buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.add(refreshButton);
        buttonPanel.add(clearButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Set minimum size
        setPreferredSize(new Dimension(800, 400));
    }

    private void loadHistory() {
        tableModel.setRowCount(0); // Clear existing rows
        User user = userService.getUserByUsername(username);
        if (user != null) {
            List<UserHistoryService.SearchHistoryEntry> history = historyService.getSearchHistory(user.getId(), 50);
            for (UserHistoryService.SearchHistoryEntry entry : history) {
                tableModel.addRow(new Object[]{
                    dateFormat.format(entry.getTimestamp()),
                    entry.getSearchType(),
                    entry.getQuery(),
                    entry.getResult()
                });
            }
        }
    }

    private void clearHistory() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to clear your search history?",
            "Confirm Clear History",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            User user = userService.getUserByUsername(username);
            if (user != null) {
                historyService.clearSearchHistory(user.getId());
                loadHistory();
            }
        }
    }

    public void addSearchEntry(String query, String searchType, String result) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            historyService.addSearchHistory(user.getId(), query, searchType, result);
            loadHistory();
        }
    }
} 