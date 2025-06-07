package com.example.restudy.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.restudy.database.UserDatabaseHelper;
import com.example.restudy.model.User;

public class UserDataQuery {
    private final UserDatabaseHelper dbHelper;

    public UserDataQuery(Context context) {
        dbHelper = new UserDatabaseHelper(context);
    }

    // ✅ Kiểm tra đăng nhập với email và password
    public User checkLogin(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM User WHERE email = ? AND password = ?",
                new String[]{email, password});

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow("userName")));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
            cursor.close();
            return user;
        }

        return null;
    }

    // ✅ Thêm người dùng mới (nếu có đăng ký)
    public boolean addUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userName", user.getName());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());

        long result = db.insert("User", null, values);
        return result != -1;
    }

    // ✅ Kiểm tra email đã tồn tại chưa (để dùng trong đăng ký)
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM User WHERE email = ?",
                new String[]{email});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}
