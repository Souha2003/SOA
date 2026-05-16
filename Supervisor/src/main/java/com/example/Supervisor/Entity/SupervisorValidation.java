package com.example.Supervisor.Entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class SupervisorValidation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long requestId; // L'ID de la StudentRequest provenant du service Student
    private String supervisorName;
    private String decision; // APPROUVÉ ou REJETÉ
    private String comments;
    private LocalDateTime validationDate;

    public SupervisorValidation() {
        this.validationDate = LocalDateTime.now();
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }
    public String getSupervisorName() { return supervisorName; }
    public void setSupervisorName(String supervisorName) { this.supervisorName = supervisorName; }
    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public LocalDateTime getValidationDate() { return validationDate; }
}