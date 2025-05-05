package com.weathervoyage;

import com.weathervoyage.service.UserService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private UserService userService;

    public LoginPanel() {
        userService = new UserService();
        setLayout(new GridBagLayout());
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        KeyListener enterListener = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        };
        usernameField.addKeyListener(enterListener);
        passwordField.addKeyListener(enterListener);
    }

    private void setupLayout() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JLabel titleLabel = new JLabel("WeatherVoyage");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(41, 128, 185));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel("Your Travel Forecast Mate");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        gbc.gridy = 1;
        add(subtitleLabel, gbc);

        gbc.gridy = 2; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(passwordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginButton.setPreferredSize(new Dimension(100, 30));
        registerButton.setPreferredSize(new Dimension(100, 30));
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(registerButton);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);
    }

    private void setupListeners() {
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegister());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("All fields are required.");
            return;
        }

        if (userService.authenticate(username, password)) {
            showSuccess("Login successful!");
            showMainDashboard(username);
        } else {
            showError("Invalid username or password.");
        }
    }

    private void handleRegister() {
        JDialog registerDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
            "Register", true);
        registerDialog.setLayout(new GridBagLayout());

        JTextField nameField = new JTextField(20);
        JTextField regUsername = new JTextField(20);
        JPasswordField regPassword = new JPasswordField(20);
        JPasswordField confirmPassword = new JPasswordField(20);
        JTextField emailField = new JTextField(20);
        JButton submitButton = new JButton("Register");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); gbc.anchor = GridBagConstraints.WEST;

        addRegisterField(registerDialog, "Name:", nameField, gbc, 0);
        addRegisterField(registerDialog, "Username:", regUsername, gbc, 1);
        addRegisterField(registerDialog, "Password:", regPassword, gbc, 2);
        addRegisterField(registerDialog, "Confirm Password:", confirmPassword, gbc, 3);
        addRegisterField(registerDialog, "Email:", emailField, gbc, 4);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registerDialog.add(submitButton, gbc);

        submitButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String username = regUsername.getText().trim();
            String password = new String(regPassword.getPassword());
            String confirm = new String(confirmPassword.getPassword());
            String email = emailField.getText().trim();

            try {
                if (name.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty())
                    throw new IllegalArgumentException("All fields are required.");
                if (!password.equals(confirm))
                    throw new IllegalArgumentException("Passwords do not match.");
                if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
                    throw new IllegalArgumentException("Invalid email.");

                if (userService.registerUser(name, username, password, email)) {
                    showSuccess("Registration successful! Please login.");
                    registerDialog.dispose();
                    usernameField.setText(username);
                    passwordField.setText("");
                } else {
                    showError("Registration failed. Username or email may exist.");
                }
            } catch (Exception ex) {
                showError("Error: " + ex.getMessage());
            }
        });

        registerDialog.pack();
        registerDialog.setLocationRelativeTo(this);
        registerDialog.setResizable(false);
        registerDialog.setVisible(true);
    }

    private void addRegisterField(Container c, String label, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        c.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        c.add(field, gbc);
    }

    private void showMainDashboard(String username) {
        Container parent = this.getParent();
        parent.removeAll();
        parent.add(new DashboardPanel(username));
        parent.revalidate();
        parent.repaint();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
