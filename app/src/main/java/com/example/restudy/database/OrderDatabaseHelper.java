package com.example.restudy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.restudy.model.Order;

import java.util.ArrayList;

public class OrderDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Order.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ORDERS = "orders";
    private static final String ORDER_ID = "id";
    private static final String ORDER_USER_ID = "userId";
    private static final String ORDER_TOTAL = "total";
    private static final String ORDER_DATE = "date";
    private static final String ORDER_STATUS = "status";

    public OrderDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_ORDERS + " (" +
                ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ORDER_USER_ID + " INTEGER, " +
                ORDER_TOTAL + " REAL, " +
                ORDER_DATE + " DATETIME, " +
                ORDER_STATUS + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    // Thêm đơn hàng
    public void addOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ORDER_USER_ID, order.getUserId());
        values.put(ORDER_TOTAL, order.getTotal());
        values.put(ORDER_DATE, order.getDate());
        values.put(ORDER_STATUS, order.getStatus());

        db.insert(TABLE_ORDERS, null, values);
        db.close();
    }

    // Lấy tất cả đơn hàng
    public ArrayList<Order> getAllOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDERS, null, null, null, null, null, ORDER_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_ID));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_USER_ID));
                double total = cursor.getDouble(cursor.getColumnIndexOrThrow(ORDER_TOTAL));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(ORDER_DATE));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(ORDER_STATUS));

                orders.add(new Order(id, userId, total, date, status));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return orders;
    }

    // Cập nhật đơn hàng
    public void updateOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ORDER_USER_ID, order.getUserId());
        values.put(ORDER_TOTAL, order.getTotal());
        values.put(ORDER_DATE, order.getDate());
        values.put(ORDER_STATUS, order.getStatus());

        db.update(TABLE_ORDERS, values, ORDER_ID + " = ?", new String[]{String.valueOf(order.getId())});
        db.close();
    }

    // Xóa đơn hàng
    public void deleteOrder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDERS, ORDER_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
