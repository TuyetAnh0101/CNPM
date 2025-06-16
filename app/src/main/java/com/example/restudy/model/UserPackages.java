package com.example.restudy.model;

public class UserPackages {
    private int id;
    private int userId;
    private int packageId;
    private int remainingPosts;
    private String expiryDate; // định dạng yyyy-MM-dd

    public UserPackages() {}

    public UserPackages(int id, int userId, int packageId, int remainingPosts, String expiryDate) {
        this.id = id;
        this.userId = userId;
        this.packageId = packageId;
        this.remainingPosts = remainingPosts;
        this.expiryDate = expiryDate;
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getPackageId() { return packageId; }
    public void setPackageId(int packageId) { this.packageId = packageId; }

    public int getRemainingPosts() { return remainingPosts; }
    public void setRemainingPosts(int remainingPosts) { this.remainingPosts = remainingPosts; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
}

