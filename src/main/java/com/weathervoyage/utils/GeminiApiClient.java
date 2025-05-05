package com.weathervoyage.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GeminiApiClient {
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    private static final String API_KEY = "AIzaSyDt8b1cKc7G1T0UTd9-MHGr2bSE36Bx4Ws";

    public String generateContent(String prompt) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = createRequest(prompt);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity());
                
                if (statusCode != 200) {
                    System.err.println("API Error Response: " + responseBody);
                    throw new IOException("API request failed with status code: " + statusCode);
                }
                
                return extractTextFromResponse(responseBody);
            }
        }
    }

    private HttpPost createRequest(String prompt) throws IOException {
        HttpPost request = new HttpPost(GEMINI_API_URL + "?key=" + API_KEY);
        request.setHeader("Content-Type", "application/json");
        
        JSONObject requestBody = new JSONObject();
        JSONArray contents = new JSONArray();
        JSONObject content = new JSONObject();
        JSONArray parts = new JSONArray();
        JSONObject part = new JSONObject();
        part.put("text", prompt);
        parts.put(part);
        content.put("parts", parts);
        contents.put(content);
        requestBody.put("contents", contents);
        
        request.setEntity(new StringEntity(requestBody.toString(), StandardCharsets.UTF_8));
        return request;
    }

    private String extractTextFromResponse(String jsonResponse) {
        try {
            JSONObject response = new JSONObject(jsonResponse);
            
            if (!response.has("candidates") || response.getJSONArray("candidates").isEmpty()) {
                return "I apologize, but I couldn't generate a response at this time.";
            }
            
            JSONObject candidate = response.getJSONArray("candidates").getJSONObject(0);
            if (!candidate.has("content") || !candidate.getJSONObject("content").has("parts")) {
                return "I apologize, but I couldn't process the response properly.";
            }
            
            JSONArray parts = candidate.getJSONObject("content").getJSONArray("parts");
            if (parts.isEmpty()) {
                return "I apologize, but I couldn't process the response properly.";
            }
            
            String text = parts.getJSONObject(0).getString("text");
            return text != null && !text.trim().isEmpty() ? text : 
                   "I apologize, but I couldn't generate a meaningful response.";
        } catch (Exception e) {
            System.err.println("Error parsing response: " + e.getMessage());
            return "I apologize, but I encountered an error while processing the response.";
        }
    }
} 