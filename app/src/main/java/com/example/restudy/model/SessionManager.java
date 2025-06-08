package com.example.restudy.model;
public class SessionManager {
    private static boolean loggedIn = false;
    private static boolean isAdmin = false;

    public static boolean isLoggedIn() {
        return loggedIn;
    }

    public static boolean isAdmin() {
        return isAdmin;
    }

    public static void login(boolean admin) {
        loggedIn = true;
        isAdmin = admin;
    }

    public static void logout() {
        loggedIn = false;
        isAdmin = false;
    }
}
