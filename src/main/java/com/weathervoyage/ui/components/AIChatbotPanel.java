package com.weathervoyage.ui.components;

import com.weathervoyage.service.AIService;
import com.weathervoyage.ui.theme.ThemeManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AIChatbotPanel extends JDialog {
    private final JTextPane chatHistory;
    private final JTextField messageField;
    private final JButton sendButton;
    private final JButton returnButton;
    private final ThemeManager themeManager;
    private final AIService aiService;
    private static final int PANEL_WIDTH = 500;
    private static final int PANEL_HEIGHT = 600;
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private boolean isProcessing = false;
    private final String destination;
    private final List<ChatMessage> messageHistory;

    private static class ChatMessage {
        final String sender;
        final String message;
        final LocalDateTime timestamp;
        final boolean isUser;

        ChatMessage(String sender, String message, LocalDateTime timestamp, boolean isUser) {
            this.sender = sender;
            this.message = message;
            this.timestamp = timestamp;
            this.isUser = isUser;
        }
    }

    public AIChatbotPanel(JFrame owner, ThemeManager themeManager, String destination) {
        super(owner, "AI Travel Assistant", false);
        this.themeManager = themeManager;
        this.aiService = new AIService();
        this.destination = destination;
        this.messageHistory = new ArrayList<>();
        
        // Set destination in AIService
        if (destination != null && !destination.isEmpty()) {
            aiService.setDestination(destination);
        }
        
        // Initialize components
        chatHistory = createChatHistory();
        messageField = createMessageField();
        sendButton = createSendButton();
        returnButton = createReturnButton();
        
        setupUI();
        applyTheme();
    }

    private JTextPane createChatHistory() {
        JTextPane chat = new JTextPane();
        chat.setEditable(false);
        chat.setContentType("text/html");
        chat.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        chat.setFont(themeManager.getDefaultFont());
        return chat;
    }

    private JTextField createMessageField() {
        JTextField field = new JTextField();
        field.putClientProperty("JTextField.placeholderText", "Ask me anything about travel...");
        field.setFont(themeManager.getDefaultFont());
        field.addActionListener(e -> sendMessage());
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setCaretPosition(field.getText().length());
            }
        });
        
        return field;
    }

    private JButton createSendButton() {
        JButton button = new JButton("Send");
        button.setFont(themeManager.getDefaultFont());
        button.setForeground(Color.BLACK);
        button.setBackground(themeManager.getAccentColor());
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.addActionListener(e -> sendMessage());
        return button;
    }

    private JButton createReturnButton() {
        JButton button = new JButton("Return to Dashboard");
        button.setFont(themeManager.getDefaultFont());
        button.setForeground(Color.BLACK);
        button.setBackground(themeManager.getAccentColor());
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.addActionListener(e -> dispose());
        return button;
    }

    private void setupUI() {
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setLocationRelativeTo(getOwner());
        setResizable(true);
        
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Add return button at the top
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(themeManager.getBackgroundColor());
        topPanel.add(returnButton);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(chatHistory);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        
        // Add welcome message with destination context if available
        String welcomeMessage = "Hello! I'm your travel assistant.";
        if (destination != null && !destination.isEmpty()) {
            welcomeMessage += " I see you're interested in " + destination + ". How can I help you with your travel plans?";
        } else {
            welcomeMessage += " How can I help you today?";
        }
        appendMessage("AI Assistant", welcomeMessage, false);
        
        // Set focus to message field
        messageField.requestFocusInWindow();
    }

    private void sendMessage() {
        if (isProcessing) {
            return;
        }

        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            isProcessing = true;
            messageField.setEnabled(false);
            sendButton.setEnabled(false);
            
            appendMessage("You", message, true);
            messageField.setText("");
            
            // Show typing indicator and get AI response
            appendMessage("AI Assistant", "typing...", false);
            new Thread(() -> {
                try {
                    String response = aiService.getAIResponse(message);
                    SwingUtilities.invokeLater(() -> {
                        removeLastMessage();
                        appendMessage("AI Assistant", response, false);
                        messageField.setEnabled(true);
                        sendButton.setEnabled(true);
                        messageField.requestFocusInWindow();
                        isProcessing = false;
                    });
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> {
                        removeLastMessage();
                        appendMessage("AI Assistant", "I apologize, but I encountered an error. Please try again.", false);
                        messageField.setEnabled(true);
                        sendButton.setEnabled(true);
                        messageField.requestFocusInWindow();
                        isProcessing = false;
                    });
                }
            }).start();
        }
    }

    private void appendMessage(String sender, String message, boolean isUser) {
        LocalDateTime timestamp = LocalDateTime.now();
        messageHistory.add(new ChatMessage(sender, message, timestamp, isUser));
        updateChatDisplay();
    }

    private void updateChatDisplay() {
        StringBuilder chatContent = new StringBuilder();
        for (ChatMessage msg : messageHistory) {
            String bgColor = msg.isUser ? "#E3F2FD" : "#F0F0F0";
            String alignment = msg.isUser ? "right" : "left";
            String margin = msg.isUser ? "margin-left: 20%;" : "margin-right: 20%;";
            
            chatContent.append(String.format(
                "<div style='margin: 5px; padding: 8px; background-color: %s; border-radius: 8px; text-align: %s; %s'>" +
                "<b>%s</b> (%s)<br>%s</div>",
                bgColor, alignment, margin, msg.sender, msg.timestamp.format(TIME_FORMAT), 
                msg.message.replace("\n", "<br>")
            ));
        }
        
        chatHistory.setText(chatContent.toString());
        chatHistory.setCaretPosition(chatHistory.getDocument().getLength());
    }

    private void removeLastMessage() {
        if (!messageHistory.isEmpty()) {
            messageHistory.remove(messageHistory.size() - 1);
            updateChatDisplay();
        }
    }

    private void applyTheme() {
        Color bgColor = themeManager.getBackgroundColor();
        Color textColor = themeManager.getForegroundColor();
        Color inputBg = themeManager.getComponentBackgroundColor();
        Color accentColor = themeManager.getAccentColor();

        chatHistory.setBackground(bgColor);
        chatHistory.setForeground(textColor);
        messageField.setBackground(inputBg);
        messageField.setForeground(textColor);
        messageField.setCaretColor(textColor);
        sendButton.setBackground(accentColor);
        sendButton.setForeground(Color.BLACK);
        returnButton.setBackground(accentColor);
        returnButton.setForeground(Color.BLACK);
        getContentPane().setBackground(bgColor);
    }
} 