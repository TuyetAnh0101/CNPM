package com.example.restudy.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.restudy.Utils;
import com.example.restudy.database.ProductDatabaseHelper;
import com.example.restudy.model.Product;

import java.util.ArrayList;

public class ProductDataQuery {

    public static long insert(Context context, Product product) {
        ProductDatabaseHelper dbHelper = new ProductDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Utils.PRODUCT_NAME, product.getName());
        values.put(Utils.PRODUCT_PRICE, product.getPrice());
        values.put(Utils.PRODUCT_DESCRIPTION, product.getDescription());
        values.put(Utils.PRODUCT_IMAGE, product.getImage());
        values.put(Utils.PRODUCT_CATEGORY_ID, product.getCategoryId());
        values.put(Utils.PRODUCT_STOCK, product.getStock());
        values.put(Utils.PRODUCT_STATUS, product.isStatus() ? 1 : 0);
        values.put(Utils.PRODUCT_CREATED_AT, product.getCreatedAt());
        values.put(Utils.PRODUCT_UPDATED_AT, product.getUpdatedAt());

        long result = db.insert(Utils.TABLE_PRODUCTS, null, values);
        db.close();
        return result;
    }

    public static ArrayList<Product> getAll(Context context) {
        ArrayList<Product> products = new ArrayList<>();
        ProductDatabaseHelper dbHelper = new ProductDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cs = db.rawQuery("SELECT * FROM " + Utils.TABLE_PRODUCTS, null);

        if (cs.moveToFirst()) {
            do {
                int id = cs.getInt(cs.getColumnIndexOrThrow(Utils.PRODUCT_ID));
                String name = cs.getString(cs.getColumnIndexOrThrow(Utils.PRODUCT_NAME));
                double price = cs.getDouble(cs.getColumnIndexOrThrow(Utils.PRODUCT_PRICE));
                String description = cs.getString(cs.getColumnIndexOrThrow(Utils.PRODUCT_DESCRIPTION));
                String image = cs.getString(cs.getColumnIndexOrThrow(Utils.PRODUCT_IMAGE));
                int categoryId = cs.getInt(cs.getColumnIndexOrThrow(Utils.PRODUCT_CATEGORY_ID));
                int stock = cs.getInt(cs.getColumnIndexOrThrow(Utils.PRODUCT_STOCK));
                boolean status = cs.getInt(cs.getColumnIndexOrThrow(Utils.PRODUCT_STATUS)) == 1;
                String createdAt = cs.getString(cs.getColumnIndexOrThrow(Utils.PRODUCT_CREATED_AT));
                String updatedAt = cs.getString(cs.getColumnIndexOrThrow(Utils.PRODUCT_UPDATED_AT));

                products.add(new Product(id, name, price, description, image, categoryId, stock, status, createdAt, updatedAt));
            } while (cs.moveToNext());
        }
        cs.close();
        db.close();
        return products;
    }

    public static boolean delete(Context context, int id) {
        ProductDatabaseHelper dbHelper = new ProductDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete(Utils.TABLE_PRODUCTS, Utils.PRODUCT_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    public static int update(Context context, Product product) {
        ProductDatabaseHelper dbHelper = new ProductDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Utils.PRODUCT_NAME, product.getName());
        values.put(Utils.PRODUCT_PRICE, product.getPrice());
        values.put(Utils.PRODUCT_DESCRIPTION, product.getDescription());
        values.put(Utils.PRODUCT_IMAGE, product.getImage());
        values.put(Utils.PRODUCT_CATEGORY_ID, product.getCategoryId());
        values.put(Utils.PRODUCT_STOCK, product.getStock());
        values.put(Utils.PRODUCT_STATUS, product.isStatus() ? 1 : 0);
        values.put(Utils.PRODUCT_CREATED_AT, product.getCreatedAt());
        values.put(Utils.PRODUCT_UPDATED_AT, product.getUpdatedAt());

        int result = db.update(Utils.TABLE_PRODUCTS, values, Utils.PRODUCT_ID + "=?", new String[]{String.valueOf(product.getId())});
        db.close();
        return result;
    }
}
