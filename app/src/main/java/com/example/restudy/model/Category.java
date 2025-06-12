package com.example.restudy.model;

public class Category {
    int id;
    String name;
    private double price;

    public Category(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price=price;
    }
    @Override
    public String toString() {
        return name;  // Trả về tên category để hiển thị trong spinner
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}