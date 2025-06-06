package com.example.demo.model;

import java.util.List;

public class LookupResult {
    private String identifier;
    private String type; // "DRIVER" or "VEHICLE"
    private List<Notice> notices;
    private List<Warrant> warrants;

    public LookupResult(String identifier, String type, List<Notice> notices, List<Warrant> warrants) {
        this.identifier = identifier;
        this.type = type;
        this.notices = notices;
        this.warrants = warrants;
    }

    // Getters and setters
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Notice> getNotices() {
        return notices;
    }

    public void setNotices(List<Notice> notices) {
        this.notices = notices;
    }

    public List<Warrant> getWarrants() {
        return warrants;
    }

    public void setWarrants(List<Warrant> warrants) {
        this.warrants = warrants;
    }
}