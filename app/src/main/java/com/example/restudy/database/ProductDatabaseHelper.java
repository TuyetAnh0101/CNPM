package com.example.restudy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "product.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PRODUCTS = "products";
    public static final String PRODUCT_ID = "id";
    public static final String PRODUCT_NAME = "name";
    public static final String PRODUCT_PRICE = "price";
    public static final String PRODUCT_DESCRIPTION = "description";
    public static final String PRODUCT_IMAGE = "image";
    public static final String PRODUCT_CATEGORY_ID = "categoryId";
    public static final String PRODUCT_STOCK = "stock";
    public static final String PRODUCT_STATUS = "status";
    public static final String PRODUCT_CREATED_AT = "created_at";
    public static final String PRODUCT_UPDATED_AT = "updated_at";

    public ProductDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PRODUCT_NAME + " TEXT, " +
                PRODUCT_PRICE + " REAL, " +
                PRODUCT_DESCRIPTION + " TEXT, " +
                PRODUCT_IMAGE + " TEXT, " +
                PRODUCT_CATEGORY_ID + " INTEGER, " +
                PRODUCT_STOCK + " INTEGER, " +
                PRODUCT_STATUS + " INTEGER, " + // BOOLEAN: dùng INTEGER 0/1
                PRODUCT_CREATED_AT + " TEXT, " +
                PRODUCT_UPDATED_AT + " TEXT" +
                ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }
}
