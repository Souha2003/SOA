package com.example.company.dto;

public class ValidationRequest {
    private Long requestId;
    private String response; // "ACCEPTED" ou "REJECTED"

    // Getters et Setters
    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
}