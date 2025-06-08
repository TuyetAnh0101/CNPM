package com.example.restudy;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

public class Utils {
    public static final String TABLE_PRODUCTS = "products";

    // Các cột trong bảng products
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
    public static final String TABLE_CATEGORY = "OldStuffStore";
    public static final String CATEGORY_ID = "id";
    public static final String CATEGORY_NAME = "name";

    public static Bitmap convertToBitmapFromAssets(Context context, String nameImage) {
        AssetManager assetManager = context.getAssets();
        try (InputStream inputStream = assetManager.open("image/" + nameImage)) {
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String formatPrice(double price) {
        return String.format("%,.0f đ", price);
    }

}
