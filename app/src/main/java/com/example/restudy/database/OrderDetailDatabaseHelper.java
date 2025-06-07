package com.example.restudy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.restudy.model.OrderDetail;

import java.util.ArrayList;

public class OrderDetailDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "OrderDetail.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ORDER_DETAILS = "order_details";
    private static final String DETAIL_ID = "id";
    private static final String DETAIL_ORDER_ID = "orderId";
    private static final String DETAIL_PRODUCT_ID = "productId";
    private static final String DETAIL_QUANTITY = "quantity";
    private static final String DETAIL_PRICE = "price";

    public OrderDetailDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_ORDER_DETAILS + " (" +
                DETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DETAIL_ORDER_ID + " INTEGER, " +
                DETAIL_PRODUCT_ID + " INTEGER, " +
                DETAIL_QUANTITY + " INTEGER, " +
                DETAIL_PRICE + " REAL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_DETAILS);
        onCreate(db);
    }

    // Thêm chi tiết đơn hàng
    public void addOrderDetail(OrderDetail detail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DETAIL_ORDER_ID, detail.getOrderId());
        values.put(DETAIL_PRODUCT_ID, detail.getProductId());
        values.put(DETAIL_QUANTITY, detail.getQuantity());
        values.put(DETAIL_PRICE, detail.getPrice());

        db.insert(TABLE_ORDER_DETAILS, null, values);
        db.close();
    }

    // Lấy chi tiết theo orderId
    public ArrayList<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        ArrayList<OrderDetail> details = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDER_DETAILS, null, DETAIL_ORDER_ID + " = ?", new String[]{String.valueOf(orderId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DETAIL_ID));
                int oId = cursor.getInt(cursor.getColumnIndexOrThrow(DETAIL_ORDER_ID));
                int productId = cursor.getInt(cursor.getColumnIndexOrThrow(DETAIL_PRODUCT_ID));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DETAIL_QUANTITY));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DETAIL_PRICE));

                details.add(new OrderDetail(id, oId, productId, quantity, price));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return details;
    }

    // Cập nhật chi tiết đơn hàng
    public void updateOrderDetail(OrderDetail detail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DETAIL_ORDER_ID, detail.getOrderId());
        values.put(DETAIL_PRODUCT_ID, detail.getProductId());
        values.put(DETAIL_QUANTITY, detail.getQuantity());
        values.put(DETAIL_PRICE, detail.getPrice());

        db.update(TABLE_ORDER_DETAILS, values, DETAIL_ID + " = ?", new String[]{String.valueOf(detail.getId())});
        db.close();
    }

    // Xóa chi tiết đơn hàng
    public void deleteOrderDetail(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDER_DETAILS, DETAIL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
