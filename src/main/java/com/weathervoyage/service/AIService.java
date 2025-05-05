package com.weathervoyage.service;

import com.weathervoyage.utils.GeminiApiClient;
import java.io.IOException;

public class AIService {
    private final GeminiApiClient geminiClient;
    private String destination;

    public AIService() {
        this.geminiClient = new GeminiApiClient();
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getAIResponse(String userMessage) {
        try {
            StringBuilder prompt = new StringBuilder("You are a helpful travel assistant.");
            if (destination != null && !destination.isEmpty()) {
                prompt.append(" The user is interested in traveling to ").append(destination).append(".");
            }
            prompt.append(" Please provide a clear and concise response to: ").append(userMessage);
            
            String response = geminiClient.generateContent(prompt.toString());
            return response != null ? response : "I apologize, but I couldn't generate a response at this time.";
        } catch (IOException e) {
            System.err.println("Error getting AI response: " + e.getMessage());
            return "I apologize, but I encountered an error while processing your request. Please try again later.";
        }
    }
} 