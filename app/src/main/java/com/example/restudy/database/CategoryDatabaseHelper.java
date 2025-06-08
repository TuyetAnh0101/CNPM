package com.example.restudy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.restudy.model.Category;

import java.util.ArrayList;

public class CategoryDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "OldStuffStore.db";
    private static final int DATABASE_VERSION = 2;

    // Tên bảng và các cột
    private static final String TABLE_CATEGORY = "category";
    private static final String CATEGORY_ID = "id";
    private static final String CATEGORY_NAME = "name";

    public CategoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng category chỉ với id và name
        String createTable = "CREATE TABLE " + TABLE_CATEGORY + " (" +
                CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CATEGORY_NAME + " TEXT)";
        db.execSQL(createTable);

        // Thêm danh mục mặc định
        insertDefaultCategories(db);
    }

    private void insertDefaultCategories(SQLiteDatabase db) {
        insertCategory(db, "Điện tử");
        insertCategory(db, "Nội thất");
        insertCategory(db, "Sách");
        insertCategory(db, "Thời trang");
    }

    private void insertCategory(SQLiteDatabase db, String name) {
        ContentValues values = new ContentValues();
        values.put(CATEGORY_NAME, name);
        db.insert(TABLE_CATEGORY, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        onCreate(db);
    }

    // Thêm danh mục mới
    public void addCategory(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CATEGORY_NAME, name);
        db.insert(TABLE_CATEGORY, null, values);
        db.close();
    }

    // Lấy danh sách danh mục
    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORY, null, null, null, null, null, CATEGORY_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(CATEGORY_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY_NAME));
                categories.add(new Category(id, name));
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return categories;
    }

    // Cập nhật danh mục
    public void updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CATEGORY_NAME, category.getName());
        db.update(TABLE_CATEGORY, values, CATEGORY_ID + " = ?", new String[]{String.valueOf(category.getId())});
        db.close();
    }

    // Xóa danh mục
    public void deleteCategory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORY, CATEGORY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
