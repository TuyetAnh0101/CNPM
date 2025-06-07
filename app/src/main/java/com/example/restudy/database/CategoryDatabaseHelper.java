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
    private static final int DATABASE_VERSION = 1;

    // Tên bảng và các cột
    private static final String TABLE_CATEGORY = "category";
    private static final String CATEGORY_ID = "id";
    private static final String CATEGORY_NAME = "name";
    private static final String CATEGORY_DESCRIPTION = "description";
    private static final String CATEGORY_CREATED_AT = "created_at";

    public CategoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_CATEGORY + " (" +
                CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CATEGORY_NAME + " TEXT, " +
                CATEGORY_DESCRIPTION + " TEXT, " +
                CATEGORY_CREATED_AT + " DATETIME)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        onCreate(db);
    }

    // Thêm danh mục
    public void addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CATEGORY_NAME, category.getName());
        values.put(CATEGORY_DESCRIPTION, category.getDescription());
        values.put(CATEGORY_CREATED_AT, category.getCreatedAt());

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
                String description = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY_DESCRIPTION));
                String createdAt = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY_CREATED_AT));

                categories.add(new Category(id, name, description, createdAt));
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
        values.put(CATEGORY_DESCRIPTION, category.getDescription());
        values.put(CATEGORY_CREATED_AT, category.getCreatedAt());

        db.update(TABLE_CATEGORY, values, CATEGORY_ID + " = ?", new String[]{String.valueOf(category.getId())});
        db.close();
    }

    // Xoá danh mục
    public void deleteCategory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORY, CATEGORY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
