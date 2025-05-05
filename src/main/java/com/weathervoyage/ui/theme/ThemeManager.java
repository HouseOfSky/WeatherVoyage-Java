package com.weathervoyage.ui.theme;

import javax.swing.*;
import java.awt.*;

public interface ThemeManager {
    void applyTheme(JComponent component);
    Color getBackgroundColor();
    Color getForegroundColor();
    Color getComponentBackgroundColor();
    Color getAccentColor();
    Font getDefaultFont();
    Font getHeaderFont();
    Font getTitleFont();
} 