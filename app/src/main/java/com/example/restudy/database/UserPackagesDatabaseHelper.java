package com.example.restudy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.restudy.model.UserPackages;

public class UserPackagesDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "user_packages.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "UserPackages";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER_ID = "userId";
    public static final String COLUMN_PACKAGE_ID = "packageId";
    public static final String COLUMN_REMAINING_POSTS = "remainingPosts";
    public static final String COLUMN_EXPIRY_DATE = "expiryDate";

    public UserPackagesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID + " INTEGER NOT NULL, " +
                COLUMN_PACKAGE_ID + " INTEGER NOT NULL, " +
                COLUMN_REMAINING_POSTS + " INTEGER, " +
                COLUMN_EXPIRY_DATE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // ======================= CRUD Methods =======================

    public void insertUserPackage(UserPackages userPackage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userPackage.getUserId());
        values.put(COLUMN_PACKAGE_ID, userPackage.getPackageId());
        values.put(COLUMN_REMAINING_POSTS, userPackage.getRemainingPosts());
        values.put(COLUMN_EXPIRY_DATE, userPackage.getExpiryDate());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateUserPackage(UserPackages userPackage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PACKAGE_ID, userPackage.getPackageId());
        values.put(COLUMN_REMAINING_POSTS, userPackage.getRemainingPosts());
        values.put(COLUMN_EXPIRY_DATE, userPackage.getExpiryDate());

        db.update(TABLE_NAME, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userPackage.getUserId())});
        db.close();
    }

    public UserPackages getUserPackageByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null, null, null
        );

        UserPackages userPackage = null;
        if (cursor != null && cursor.moveToFirst()) {
            userPackage = new UserPackages(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PACKAGE_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REMAINING_POSTS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPIRY_DATE))
            );
            cursor.close();
        }
        db.close();
        return userPackage;
    }

    public void deleteUserPackage(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
    }
}
