package com.example.restudy.model;

public class Category {
    private int id;
    private String name;
    private String description;
    private String createdAt;

    // Constructor đầy đủ
    public Category(int id, String name, String description, String createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Constructor không có id (dùng khi thêm mới)
    public Category(String name, String description, String createdAt) {
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Getter & Setter
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
