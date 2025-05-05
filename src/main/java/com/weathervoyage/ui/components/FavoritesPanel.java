package com.weathervoyage.ui.components;

import com.weathervoyage.model.User;
import com.weathervoyage.service.UserHistoryService;
import com.weathervoyage.service.UserService;
import com.weathervoyage.ui.theme.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FavoritesPanel extends JPanel {
    private JList<String> favoritesList;
    private DefaultListModel<String> favoritesModel;
    private JButton removeButton;
    private JButton refreshButton;
    private ThemeManager themeManager;
    private UserHistoryService historyService;
    private UserService userService;
    private String username;

    public FavoritesPanel(ThemeManager themeManager, String username) {
        this.themeManager = themeManager;
        this.username = username;
        this.historyService = new UserHistoryService();
        this.userService = new UserService();
        setLayout(new BorderLayout());
        initializeComponents();
        setupLayout();
        loadFavorites();
    }

    private void initializeComponents() {
        favoritesModel = new DefaultListModel<>();
        favoritesList = new JList<>(favoritesModel);
        favoritesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        favoritesList.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Set list cell renderer for better appearance
        favoritesList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                        boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return label;
            }
        });

        removeButton = new JButton("Remove");
        refreshButton = new JButton("Refresh");
        
        // Style buttons
        removeButton.setFont(new Font("Arial", Font.PLAIN, 12));
        refreshButton.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Add action listeners
        removeButton.addActionListener(e -> removeFromFavorites());
        refreshButton.addActionListener(e -> loadFavorites());
    }

    private void setupLayout() {
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add title label
        JLabel titleLabel = new JLabel("Favorite Places");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Add scroll pane for the list
        JScrollPane scrollPane = new JScrollPane(favoritesList);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(scrollPane, BorderLayout.CENTER);
        
        // Add buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.add(refreshButton);
        buttonPanel.add(removeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Set minimum size
        setPreferredSize(new Dimension(300, 400));
    }

    private void loadFavorites() {
        favoritesModel.clear();
        User user = userService.getUserByUsername(username);
        if (user != null) {
            System.out.println("Loading favorites for user: " + username + " (ID: " + user.getId() + ")");
            List<UserHistoryService.FavoritePlace> favorites = historyService.getFavoritePlaces(user.getId());
            System.out.println("Found " + favorites.size() + " favorite places");
            for (UserHistoryService.FavoritePlace place : favorites) {
                System.out.println("Adding favorite place: " + place.getPlaceName());
                favoritesModel.addElement(place.getPlaceName());
            }
        } else {
            System.out.println("User not found: " + username);
        }
    }

    public void addToFavorites(String location) {
        if (!location.isEmpty()) {
            User user = userService.getUserByUsername(username);
            if (user != null) {
                System.out.println("Adding favorite place: " + location + " for user: " + username);
                if (historyService.addFavoritePlace(user.getId(), location, "CITY")) {
                    favoritesModel.addElement(location);
                    System.out.println("Successfully added to favorites");
                } else {
                    System.out.println("Failed to add to favorites - duplicate entry");
                    JOptionPane.showMessageDialog(this,
                        "Location is already in favorites",
                        "Duplicate Entry",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                System.out.println("User not found: " + username);
            }
        }
    }

    public void removeFromFavorites() {
        int selectedIndex = favoritesList.getSelectedIndex();
        if (selectedIndex != -1) {
            String location = favoritesModel.getElementAt(selectedIndex);
            User user = userService.getUserByUsername(username);
            if (user != null && historyService.removeFavoritePlace(user.getId(), location)) {
                favoritesModel.remove(selectedIndex);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a location to remove",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    public boolean isFavorite(String location) {
        User user = userService.getUserByUsername(username);
        return user != null && historyService.isFavoritePlace(user.getId(), location);
    }

    public JList<String> getFavoritesList() {
        return favoritesList;
    }

    public JButton getRemoveButton() {
        return removeButton;
    }
} 