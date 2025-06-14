package com.example.restudy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import com.example.restudy.model.Transactions;

import java.util.ArrayList;
import java.util.List;

public class TransactionsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "transaction.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "Transactions";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER_ID = "user_id";       // FK -> Users.id
    public static final String COLUMN_PACKAGE_ID = "package_id"; // FK -> Packages.id
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_CREATED_AT = "created_at";

    public TransactionsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Tạo bảng
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID + " INTEGER, "
                + COLUMN_PACKAGE_ID + " INTEGER, "
                + COLUMN_AMOUNT + " INTEGER, "
                + COLUMN_STATUS + " TEXT, "
                + COLUMN_CREATED_AT + " TEXT)";
        db.execSQL(CREATE_TABLE);
        Cursor cursor = db.rawQuery("SELECT * FROM Transactions ORDER BY datetime(created_at) DESC", null);

    }

    // Nâng cấp
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Thêm giao dịch
    public long insertTransaction(Transactions transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, transaction.getUserId());
        values.put(COLUMN_PACKAGE_ID, transaction.getPackageId());
        values.put(COLUMN_AMOUNT, transaction.getAmount());
        values.put(COLUMN_STATUS, transaction.getStatus());
        values.put(COLUMN_CREATED_AT, transaction.getCreatedAt());
        return db.insert(TABLE_NAME, null, values);
    }

    // Sửa
    public int updateTransaction(Transactions transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, transaction.getUserId());
        values.put(COLUMN_PACKAGE_ID, transaction.getPackageId());
        values.put(COLUMN_AMOUNT, transaction.getAmount());
        values.put(COLUMN_STATUS, transaction.getStatus());
        values.put(COLUMN_CREATED_AT, transaction.getCreatedAt());
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(transaction.getId())});
    }

    // Xóa
    public int deleteTransaction(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Lấy giao dịch theo UserId
    public ArrayList<Transactions> getTransactionsByUserId(int userId) {
        ArrayList<Transactions> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)}, null, null, COLUMN_CREATED_AT + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Transactions transaction = new Transactions();
                transaction.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                transaction.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
                transaction.setPackageId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PACKAGE_ID)));
                transaction.setAmount(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)));
                transaction.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
                transaction.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)));
                list.add(transaction);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return list;
    }
    public List<Transactions> getAllTransactions() {
        List<Transactions> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Sắp xếp theo ngày mới nhất
        Cursor cursor = db.rawQuery("SELECT * FROM Transactions ORDER BY datetime(" + COLUMN_CREATED_AT + ") DESC", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
                int packageId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PACKAGE_ID));
                int amount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS));
                String createdAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT));

                Transactions t = new Transactions(id, userId, packageId, amount, status, createdAt);
                list.add(t);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }


}
