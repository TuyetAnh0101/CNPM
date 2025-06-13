package com.example.restudy.model;

public class Transactions {
    private int id;
    private int userId;        // FK -> Users.id
    private int packageId;     // FK -> Packages.id
    private int amount;
    private String status;     // "success" or "fail"
    private String createdAt;  // lưu dưới dạng chuỗi

    public Transactions() {}

    public Transactions(int id, int userId, int packageId, int amount, String status, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.packageId = packageId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getPackageId() { return packageId; }
    public void setPackageId(int packageId) { this.packageId = packageId; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
