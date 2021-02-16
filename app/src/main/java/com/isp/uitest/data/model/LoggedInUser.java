package com.isp.uitest.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId = "Student ID";
    private String displayName = "Student/Staff Name";
    private String role = "None";

    public LoggedInUser(){

    }

    public LoggedInUser(String userId, String displayName, String role) {
        this.userId = userId;
        this.displayName = displayName;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getRole() { return role; }
}