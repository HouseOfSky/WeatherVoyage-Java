package com.weathervoyage.ui.components;

import com.weathervoyage.ui.theme.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SideMenuPanel extends JPanel {
    private ThemeManager themeManager;
    private ActionListener menuActionListener;

    public SideMenuPanel(ThemeManager themeManager, ActionListener menuActionListener) {
        this.themeManager = themeManager;
        this.menuActionListener = menuActionListener;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initializeComponents();
    }

    private void initializeComponents() {
        String[] menuItems = {"Profile", "Itinerary", "Plan Trip", "Alerts", "Settings"};
        String[] icons = {"/icons/profile.png", "/icons/itinerary.png", 
                         "/icons/chatbot.png", "/icons/alert.png", "/icons/settings.png"};

        for (int i = 0; i < menuItems.length; i++) {
            JButton button = createMenuButton(menuItems[i], icons[i]);
            add(button);
            add(Box.createVerticalStrut(5));
        }
    }

    private JButton createMenuButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(150, 30));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            // Continue without icon
        }
        
        button.addActionListener(menuActionListener);
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(themeManager.getComponentBackgroundColor());
    }
} 