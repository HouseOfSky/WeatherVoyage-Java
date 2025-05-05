package com.weathervoyage.ui.components;

import com.weathervoyage.model.User;
import com.weathervoyage.service.UserService;
import com.weathervoyage.ui.theme.ThemeManager;
import com.weathervoyage.LoginPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ProfilePanel extends JPanel {
    private ThemeManager themeManager;
    private UserService userService;
    private User currentUser;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField usernameField;
    private JButton saveButton;
    private JButton changePasswordButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton logoutButton;

    public ProfilePanel(ThemeManager themeManager, UserService userService, User currentUser) {
        this.themeManager = themeManager;
        this.userService = userService;
        this.currentUser = currentUser;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        initializeComponents();
        setupLayout();
        loadUserData();
    }

    private void initializeComponents() {
        nameField = new JTextField(30);
        emailField = new JTextField(30);
        usernameField = new JTextField(30);
        
        saveButton = new JButton("Save Changes");
        changePasswordButton = new JButton("Change Password");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete Account");
        logoutButton = new JButton("Logout");

        nameField.setEditable(false);
        emailField.setEditable(false);
        usernameField.setEditable(false);

        saveButton.addActionListener(e -> saveChanges());
        changePasswordButton.addActionListener(e -> showChangePasswordDialog());
        editButton.addActionListener(e -> toggleEditMode());
        deleteButton.addActionListener(e -> deleteCurrentUser());
        logoutButton.addActionListener(e -> logout());

        // Style the logout button
        logoutButton.setForeground(new Color(220, 53, 69)); // Bootstrap danger color
        logoutButton.setFont(new Font(logoutButton.getFont().getName(), Font.BOLD, logoutButton.getFont().getSize()));
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(createProfileSection(), BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createProfileSection() {
        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBorder(BorderFactory.createTitledBorder("Profile Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel headerLabel = new JLabel("Profile Information");
        headerLabel.setFont(themeManager.getTitleFont());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        profilePanel.add(headerLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        profilePanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        profilePanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        profilePanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        profilePanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        profilePanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        profilePanel.add(usernameField, gbc);

        // Create two button panels
        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        actionButtonPanel.add(editButton);
        actionButtonPanel.add(saveButton);
        actionButtonPanel.add(changePasswordButton);

        JPanel dangerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        dangerButtonPanel.add(logoutButton);
        dangerButtonPanel.add(deleteButton);

        // Add button panels to profile panel
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        profilePanel.add(actionButtonPanel, gbc);

        gbc.gridy = 5;
        profilePanel.add(dangerButtonPanel, gbc);

        return profilePanel;
    }

    private void loadUserData() {
        if (currentUser != null) {
            nameField.setText(currentUser.getfirstName());
            emailField.setText(currentUser.getEmail());
            usernameField.setText(currentUser.getUsername());
        }
    }

    private void toggleEditMode() {
        boolean isEditable = !nameField.isEditable();
        nameField.setEditable(isEditable);
        emailField.setEditable(isEditable);
        usernameField.setEditable(isEditable);
        editButton.setText(isEditable ? "Cancel" : "Edit");
        saveButton.setEnabled(isEditable);
    }

    private void saveChanges() {
        try {
            String newName = nameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newUsername = usernameField.getText().trim();
            String originalUsername = currentUser.getUsername();
            boolean anyChanges = false;
            boolean success = true;

            // Validate all fields
            if (newName.isEmpty() || newEmail.isEmpty() || newUsername.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "All fields are required.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if new username is different and already exists
            if (!newUsername.equals(originalUsername)) {
                User existingUser = userService.getUserByUsername(newUsername);
                if (existingUser != null) {
                    JOptionPane.showMessageDialog(this,
                        "Username already exists. Please choose a different username.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Update username first if it's changed
                if (userService.updateUsername(originalUsername, newUsername)) {
                    currentUser.setUsername(newUsername);
                    anyChanges = true;
                } else {
                    success = false;
                }
            }

            // Update name if changed
            if (!newName.equals(currentUser.getfirstName())) {
                if (userService.updateUserField(currentUser.getUsername(), "name", newName)) {
                    currentUser.setName(newName);
                    anyChanges = true;
                } else {
                    success = false;
                }
            }

            // Update email if changed
            if (!newEmail.equals(currentUser.getEmail())) {
                if (userService.updateUserField(currentUser.getUsername(), "email", newEmail)) {
                    currentUser.setEmail(newEmail);
                    anyChanges = true;
                } else {
                    success = false;
                }
            }

            if (anyChanges) {
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Profile updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    toggleEditMode();
                    loadUserData(); // Reload data to ensure UI is in sync
                } else {
                    // Revert username if update failed
                    if (!newUsername.equals(originalUsername)) {
                        currentUser.setUsername(originalUsername);
                    }
                    JOptionPane.showMessageDialog(this,
                        "Some changes could not be saved. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "No changes were made.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
                toggleEditMode();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error updating profile: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Logout Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (choice == JOptionPane.YES_OPTION) {
            Container parent = this.getParent();
            while (parent != null && !(parent instanceof JFrame)) {
                parent = parent.getParent();
            }
            if (parent != null) {
                ((JFrame) parent).dispose();
                new LoginPanel().setVisible(true);
            }
        }
    }

    private void deleteCurrentUser() {
        // First confirmation
        int initialChoice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete your account? This action cannot be undone.",
            "Delete Account - Step 1",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (initialChoice == JOptionPane.YES_OPTION) {
            // Second confirmation with password verification
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);

            JPasswordField passwordField = new JPasswordField(20);
            JLabel messageLabel = new JLabel("Please enter your password to confirm deletion:");

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(messageLabel, gbc);

            gbc.gridy = 1;
            panel.add(passwordField, gbc);

            int passwordChoice = JOptionPane.showConfirmDialog(this,
                panel,
                "Delete Account - Step 2",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);

            if (passwordChoice == JOptionPane.OK_OPTION) {
                String password = new String(passwordField.getPassword());
                
                // Verify password
                if (userService.authenticate(currentUser.getUsername(), password)) {
                    // Final confirmation
                    int finalChoice = JOptionPane.showConfirmDialog(this,
                        "This is your last chance to cancel. Are you absolutely sure you want to delete your account?",
                        "Delete Account - Final Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                    if (finalChoice == JOptionPane.YES_OPTION) {
                        try {
                            if (userService.deleteUser(currentUser.getUsername())) {
                                JOptionPane.showMessageDialog(this,
                                    "Account deleted successfully. You will be logged out.",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                                Container parent = this.getParent();
                                while (parent != null && !(parent instanceof JFrame)) {
                                    parent = parent.getParent();
                                }
                                if (parent != null) {
                                    ((JFrame) parent).dispose();
                                    new LoginPanel().setVisible(true);
                                }
                            } else {
                                JOptionPane.showMessageDialog(this,
                                    "Failed to delete account.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(this,
                                "Error deleting account: " + e.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Incorrect password. Account deletion cancelled.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void showChangePasswordDialog() {
        Window window = SwingUtilities.getWindowAncestor(this);
        JDialog dialog;
        if (window instanceof Frame) {
            dialog = new JDialog((Frame)window, "Change Password", true);
        } else if (window instanceof Dialog) {
            dialog = new JDialog((Dialog)window, "Change Password", true);
        } else {
            dialog = new JDialog((Frame)null, "Change Password", true);
        }
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JPasswordField currentPasswordField = new JPasswordField(20);
        JPasswordField newPasswordField = new JPasswordField(20);
        JPasswordField confirmPasswordField = new JPasswordField(20);

        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Current Password:"), gbc);
        gbc.gridx = 1;
        dialog.add(currentPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1;
        dialog.add(newPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Confirm New Password:"), gbc);
        gbc.gridx = 1;
        dialog.add(confirmPasswordField, gbc);

        JButton confirmButton = new JButton("Change Password");
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(confirmButton, gbc);

        confirmButton.addActionListener(e -> {
            String currentPassword = new String(currentPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "All password fields are required.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(dialog,
                    "New passwords do not match!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (newPassword.length() < 6) {
                JOptionPane.showMessageDialog(dialog,
                    "Password must be at least 6 characters long.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                if (userService.changePassword(currentUser.getUsername(), currentPassword, newPassword)) {
                    currentUser.setPassword(newPassword); // Update the current user object
                    JOptionPane.showMessageDialog(dialog,
                        "Password changed successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog,
                        "Failed to change password. Please check your current password.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace(); // Add this for debugging
                JOptionPane.showMessageDialog(dialog,
                    "Error changing password: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(window);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(themeManager.getBackgroundColor());
    }
}