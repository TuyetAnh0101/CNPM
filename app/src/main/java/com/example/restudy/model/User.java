package com.example.restudy.model;

public class User {
    private int id;
    private String name;
    private String password;
    private String email;
    private String sex;
    private String phone;
    private String role;
    private boolean active;

    // Constructor không tham số
    public User() {
    }

    // Constructor với tất cả các trường
    public User(int id, String name, String password, String email, String sex, String phone, String role, boolean active) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.sex = sex;
        this.phone = phone;
        this.role = role;
        this.active = active;
    }

    // Getter và Setter
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // Override toString() để dễ dàng debug, log thông tin
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", active=" + active +
                '}';
    }
}
