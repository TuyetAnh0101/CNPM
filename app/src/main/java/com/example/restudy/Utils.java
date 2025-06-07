package com.example.restudy;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

public class Utils {
    public static Bitmap convertToBitmapFromAssets(Context context, String nameImage) {
        AssetManager assetManager = context.getAssets();
        try {
            // Sửa lại đường dẫn để đảm bảo có dấu gạch chéo trước tên ảnh
            InputStream inputStream = assetManager.open("images/" + nameImage);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
