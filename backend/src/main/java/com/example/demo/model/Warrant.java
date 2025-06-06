package com.example.demo.model;

import java.time.LocalDateTime;

public class Warrant {
    private String id;
    private String type;
    private String description;
    private LocalDateTime issueDate;
    private String status;
    private String issuingAuthority;

    public Warrant(String id, String type, String description, LocalDateTime issueDate, String status,
            String issuingAuthority) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.issueDate = issueDate;
        this.status = status;
        this.issuingAuthority = issuingAuthority;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }
}