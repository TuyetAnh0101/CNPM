package com.example.restudy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.restudy.model.Packages;

import java.util.ArrayList;
import java.util.List;

public class PackagesDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "restudy.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PACKAGES = "tbl_packages";
    public static final String PACKAGE_ID = "package_id";
    public static final String PACKAGE_NAME = "package_name";
    public static final String PACKAGE_PRICE = "package_price";
    public static final String PACKAGE_DURATION = "package_duration";
    public static final String PACKAGE_MAX_POSTS = "package_maxPosts";
    public static final String PACKAGE_CAN_PIN = "package_canPin";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_PACKAGES + " (" +
                    PACKAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PACKAGE_NAME + " TEXT, " +
                    PACKAGE_PRICE + " INTEGER, " +
                    PACKAGE_DURATION + " INTEGER, " +
                    PACKAGE_MAX_POSTS + " INTEGER, " +
                    PACKAGE_CAN_PIN + " INTEGER)";

    public PackagesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PACKAGES);
        onCreate(db);
    }

    // Thêm gói mới
    public long addPackage(String name, int price, int duration, int maxPosts, boolean canPin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PACKAGE_NAME, name);
        values.put(PACKAGE_PRICE, price);
        values.put(PACKAGE_DURATION, duration);
        values.put(PACKAGE_MAX_POSTS, maxPosts);
        values.put(PACKAGE_CAN_PIN, canPin ? 1 : 0);
        return db.insert(TABLE_PACKAGES, null, values);
    }

    // Sửa gói theo ID
    public int updatePackage(int id, String name, int price, int duration, int maxPosts, boolean canPin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PACKAGE_NAME, name);
        values.put(PACKAGE_PRICE, price);
        values.put(PACKAGE_DURATION, duration);
        values.put(PACKAGE_MAX_POSTS, maxPosts);
        values.put(PACKAGE_CAN_PIN, canPin ? 1 : 0);
        return db.update(TABLE_PACKAGES, values, PACKAGE_ID + "=?", new String[]{String.valueOf(id)});
    }

    // Xoá gói theo ID
    public int deletePackage(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PACKAGES, PACKAGE_ID + "=?", new String[]{String.valueOf(id)});
    }

    // Lấy tất cả các gói
    public List<Packages> getAllPackages() {
        List<Packages> packageList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PACKAGES, null);

        if (cursor.moveToFirst()) {
            do {
                Packages pkg = new Packages();
                pkg.setId(cursor.getInt(cursor.getColumnIndexOrThrow(PACKAGE_ID)));
                pkg.setName(cursor.getString(cursor.getColumnIndexOrThrow(PACKAGE_NAME)));
                pkg.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow(PACKAGE_PRICE)));
                pkg.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(PACKAGE_DURATION)));
                pkg.setMaxPosts(cursor.getInt(cursor.getColumnIndexOrThrow(PACKAGE_MAX_POSTS)));
                pkg.setCanPin(cursor.getInt(cursor.getColumnIndexOrThrow(PACKAGE_CAN_PIN)) == 1);
                packageList.add(pkg);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return packageList;
    }

    // Lấy 1 gói theo ID
    public Packages getPackageById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PACKAGES,
                null,
                PACKAGE_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Packages pkg = new Packages();
            pkg.setId(cursor.getInt(cursor.getColumnIndexOrThrow(PACKAGE_ID)));
            pkg.setName(cursor.getString(cursor.getColumnIndexOrThrow(PACKAGE_NAME)));
            pkg.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow(PACKAGE_PRICE)));
            pkg.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(PACKAGE_DURATION)));
            pkg.setMaxPosts(cursor.getInt(cursor.getColumnIndexOrThrow(PACKAGE_MAX_POSTS)));
            pkg.setCanPin(cursor.getInt(cursor.getColumnIndexOrThrow(PACKAGE_CAN_PIN)) == 1);
            cursor.close();
            return pkg;
        }

        if (cursor != null) cursor.close();
        return null;
    }
}
