package com.example.restudy.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.restudy.model.User;

import java.util.ArrayList;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String USER_ID = "id";
    private static final String USER_USERNAME = "username";
    private static final String USER_PASSWORD = "password";
    private static final String USER_EMAIL = "email";
    private static final String USER_PHONE = "phone";
    private static final String USER_SEX = "sex";
    private static final String USER_ROLE = "role";
    private static final String USER_ACTIVE = "active";

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_USERNAME + " TEXT UNIQUE, " +
                USER_PASSWORD + " TEXT, " +
                USER_EMAIL + " TEXT, " +
                USER_PHONE + " TEXT, " +
                USER_SEX + " TEXT, " +
                USER_ROLE + " TEXT, " +
                USER_ACTIVE + " INTEGER)";
        db.execSQL(createUsersTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Thêm người dùng
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_USERNAME, user.getName());
        values.put(USER_PASSWORD, user.getPassword());
        values.put(USER_EMAIL, user.getEmail());
        values.put(USER_PHONE, user.getPhone());
        values.put(USER_SEX, user.getSex());
        values.put(USER_ROLE, user.getRole());
        values.put(USER_ACTIVE, user.isActive() ? 1 : 0);

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    // Cập nhật người dùng
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_PASSWORD, user.getPassword());
        values.put(USER_EMAIL, user.getEmail());
        values.put(USER_PHONE, user.getPhone());
        values.put(USER_SEX, user.getSex());
        values.put(USER_ROLE, user.getRole());
        values.put(USER_ACTIVE, user.isActive() ? 1 : 0);

        db.update(TABLE_USERS, values, USER_USERNAME + "=?", new String[]{user.getName()});
        db.close();
    }

    // Xóa người dùng
    public void deleteUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, USER_USERNAME + "=?", new String[]{username});
        db.close();
    }

    // Lấy người dùng theo username
    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, USER_USERNAME + "=?", new String[]{username}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(USER_ID));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(USER_USERNAME));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(USER_PASSWORD));
            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(USER_EMAIL));
            @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(USER_PHONE));
            @SuppressLint("Range") String sex = cursor.getString(cursor.getColumnIndex(USER_SEX));
            @SuppressLint("Range") String role = cursor.getString(cursor.getColumnIndex(USER_ROLE));
            @SuppressLint("Range") boolean active = cursor.getInt(cursor.getColumnIndex(USER_ACTIVE)) == 1;

            cursor.close();
            return new User(id, name, password, email, sex, phone, role, active);
        }
        return null;
    }

    // Lấy tất cả người dùng
    public ArrayList<User> getAllUsers() {
        ArrayList<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(USER_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(USER_USERNAME));
                @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(USER_PASSWORD));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(USER_EMAIL));
                @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(USER_PHONE));
                @SuppressLint("Range") String sex = cursor.getString(cursor.getColumnIndex(USER_SEX));
                @SuppressLint("Range") String role = cursor.getString(cursor.getColumnIndex(USER_ROLE));
                @SuppressLint("Range") boolean active = cursor.getInt(cursor.getColumnIndex(USER_ACTIVE)) == 1;

                userList.add(new User(id, name, password, email, sex, phone, role, active));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return userList;
    }

    // Kiểm tra username tồn tại
    public boolean isUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{USER_USERNAME},
                USER_USERNAME + "=?", new String[]{username}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    // Đăng nhập: kiểm tra username, password và active = 1
    public boolean loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{USER_USERNAME},
                USER_USERNAME + "=? AND " + USER_PASSWORD + "=? AND " + USER_ACTIVE + "=1",
                new String[]{username, password}, null, null, null);
        boolean isLoggedIn = (cursor.getCount() > 0);
        cursor.close();
        return isLoggedIn;
    }
}
