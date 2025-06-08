package com.example.restudy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.restudy.Utils;
import com.example.restudy.model.Product;

import java.util.ArrayList;

public class ProductDatabaseHelper extends SQLiteOpenHelper {

    public ProductDatabaseHelper(Context context) {
        super(context, "product.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createCategoryTable = "CREATE TABLE " + Utils.TABLE_CATEGORY + " (" +
                Utils.CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Utils.CATEGORY_NAME + " TEXT" +
                ")";
        db.execSQL(createCategoryTable);

        String createProductsTable = "CREATE TABLE " + Utils.TABLE_PRODUCTS + " (" +
                Utils.PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Utils.PRODUCT_NAME + " TEXT, " +
                Utils.PRODUCT_PRICE + " REAL, " +
                Utils.PRODUCT_DESCRIPTION + " TEXT, " +
                Utils.PRODUCT_IMAGE + " TEXT, " +
                Utils.PRODUCT_CATEGORY_ID + " INTEGER, " +
                Utils.PRODUCT_STOCK + " INTEGER, " +
                Utils.PRODUCT_STATUS + " INTEGER, " +  // 0/1
                Utils.PRODUCT_CREATED_AT + " TEXT, " +
                Utils.PRODUCT_UPDATED_AT + " TEXT" +
                ")";
        db.execSQL(createProductsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Utils.TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + Utils.TABLE_CATEGORY);
        onCreate(db);
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Utils.TABLE_PRODUCTS, null);
        if (cursor.moveToFirst()) {
            do {
                Product p = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(Utils.PRODUCT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Utils.PRODUCT_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(Utils.PRODUCT_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Utils.PRODUCT_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Utils.PRODUCT_IMAGE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(Utils.PRODUCT_CATEGORY_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(Utils.PRODUCT_STOCK)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(Utils.PRODUCT_STATUS)) == 1,
                        cursor.getString(cursor.getColumnIndexOrThrow(Utils.PRODUCT_CREATED_AT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Utils.PRODUCT_UPDATED_AT))
                );
                list.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public ArrayList<String> getAllCategories() {
        ArrayList<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + Utils.CATEGORY_NAME + " FROM " + Utils.TABLE_CATEGORY, null);
        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categories;
    }

    public long addProduct(String name, double price, String description, String image,
                           int categoryId, int stock, int status, String createdAt, String updatedAt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Utils.PRODUCT_NAME, name);
        values.put(Utils.PRODUCT_PRICE, price);
        values.put(Utils.PRODUCT_DESCRIPTION, description);
        values.put(Utils.PRODUCT_IMAGE, image);
        values.put(Utils.PRODUCT_CATEGORY_ID, categoryId);
        values.put(Utils.PRODUCT_STOCK, stock);
        values.put(Utils.PRODUCT_STATUS, status);
        values.put(Utils.PRODUCT_CREATED_AT, createdAt);
        values.put(Utils.PRODUCT_UPDATED_AT, updatedAt);

        long id = db.insert(Utils.TABLE_PRODUCTS, null, values);
        db.close();
        return id;
    }

    public int deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(Utils.TABLE_PRODUCTS, Utils.PRODUCT_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result;
    }
}
