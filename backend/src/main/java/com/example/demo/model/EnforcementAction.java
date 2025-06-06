package com.example.demo.model;

import java.time.LocalDateTime;

public class EnforcementAction {
    private String id;
    private String type; // "WARRANT" or "FINE"
    private String identifier; // Driver ID or Vehicle Registration
    private String userId;
    private LocalDateTime timestamp;
    private String status;
    private String notes;

    public EnforcementAction(String id, String type, String identifier, String userId, LocalDateTime timestamp,
            String status, String notes) {
        this.id = id;
        this.type = type;
        this.identifier = identifier;
        this.userId = userId;
        this.timestamp = timestamp;
        this.status = status;
        this.notes = notes;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}