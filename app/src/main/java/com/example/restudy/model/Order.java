package com.example.restudy.model;

public class Order {
    private int id;
    private int userId;
    private double total;
    private String date;
    private String status;

    public Order(int id, int userId, double total, String date, String status) {
        this.id = id;
        this.userId = userId;
        this.total = total;
        this.date = date;
        this.status = status;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
