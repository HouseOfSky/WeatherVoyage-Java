package com.weathervoyage.ui.theme;

import javax.swing.*;
import java.awt.*;

public class LightTheme implements ThemeManager {
    private static final Color BACKGROUND = new Color(255, 255, 255);
    private static final Color FOREGROUND = new Color(20, 20, 20);
    private static final Color COMPONENT_BACKGROUND = new Color(240, 240, 240);
    private static final Color ACCENT = new Color(70, 130, 180);

    @Override
    public void applyTheme(JComponent component) {
        if (component instanceof JPanel) {
            component.setBackground(BACKGROUND);
        } else if (component instanceof JTextField || component instanceof JButton || component instanceof JComboBox) {
            component.setBackground(COMPONENT_BACKGROUND);
            component.setForeground(FOREGROUND);
        } else if (component instanceof JLabel) {
            component.setForeground(FOREGROUND);
        }
    }

    @Override
    public Color getBackgroundColor() {
        return BACKGROUND;
    }

    @Override
    public Color getForegroundColor() {
        return FOREGROUND;
    }

    @Override
    public Color getComponentBackgroundColor() {
        return COMPONENT_BACKGROUND;
    }

    @Override
    public Color getAccentColor() {
        return ACCENT;
    }

    @Override
    public Font getDefaultFont() {
        return new Font("Arial", Font.PLAIN, 14);
    }

    @Override
    public Font getHeaderFont() {
        return new Font("Arial", Font.BOLD, 18);
    }

    @Override
    public Font getTitleFont() {
        return new Font("Arial", Font.BOLD, 24);
    }
} 