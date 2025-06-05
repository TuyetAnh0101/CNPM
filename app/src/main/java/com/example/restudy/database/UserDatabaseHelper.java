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

    // Bảng Users
    private static final String TABLE_USERS = "users";
    private static final String USER_ID = "id";
    private static final String USER_USERNAME = "username";
    private static final String USER_PASSWORD = "password";
    private static final String USER_EMAIL = "email";
    private static final String USER_PHONE = "phone";
    private static final String USER_SEX = "sex";

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng users với username là UNIQUE
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Cột ID tự tăng
                USER_USERNAME + " TEXT UNIQUE, " +
                USER_PASSWORD + " TEXT, " +
                USER_EMAIL + " TEXT, " +
                USER_PHONE + " TEXT, " +
                USER_SEX + " TEXT)";
        db.execSQL(createUsersTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ nếu có rồi tạo lại bảng mới
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Thêm người dùng mới vào database
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_USERNAME, user.getName());
        values.put(USER_PASSWORD, user.getPassword());
        values.put(USER_EMAIL, user.getEmail());
        values.put(USER_PHONE, user.getPhone());
        values.put(USER_SEX, user.getSex());

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    // Cập nhật thông tin người dùng
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_PASSWORD, user.getPassword());
        values.put(USER_EMAIL, user.getEmail());
        values.put(USER_PHONE, user.getPhone());
        values.put(USER_SEX, user.getSex());

        db.update(TABLE_USERS, values, USER_USERNAME + "=?", new String[]{user.getName()});
        db.close();
    }

    // Xóa người dùng
    public void deleteUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, USER_USERNAME + "=?", new String[]{username});
        db.close();
    }

    // Lấy thông tin người dùng từ username
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
            cursor.close();
            return new User(id, name, password, email, phone, sex);
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

                userList.add(new User(id, name, password, email, phone, sex));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return userList;
    }

    // Kiểm tra xem người dùng có tồn tại trong cơ sở dữ liệu hay không
    public boolean isUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{USER_USERNAME},
                USER_USERNAME + "=?", new String[]{username}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    // Kiểm tra đăng nhập
    public boolean loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{USER_USERNAME, USER_PASSWORD},
                USER_USERNAME + "=? AND " + USER_PASSWORD + "=?", new String[]{username, password}, null, null, null);
        boolean isLoggedIn = (cursor.getCount() > 0);
        cursor.close();
        return isLoggedIn;
    }
}
