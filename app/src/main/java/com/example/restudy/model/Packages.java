package com.example.restudy.model;

public class Packages {
    private int id;
    private String name;
    private int price;
    private int duration;
    private int maxPosts;
    private boolean canPin;

    public Packages() {}

    public Packages(int id, String name, int price, int duration, int maxPosts, boolean canPin) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.maxPosts = maxPosts;
        this.canPin = canPin;
    }

    public Packages(String name, int price, int duration, int maxPosts, boolean canPin) {
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.maxPosts = maxPosts;
        this.canPin = canPin;
    }

    // Getters and Setters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getPrice() { return price; }

    public void setPrice(int price) { this.price = price; }

    public int getDuration() { return duration; }

    public void setDuration(int duration) { this.duration = duration; }

    public int getMaxPosts() { return maxPosts; }

    public void setMaxPosts(int maxPosts) { this.maxPosts = maxPosts; }

    public boolean isCanPin() { return canPin; }

    public void setCanPin(boolean canPin) { this.canPin = canPin; }
}
