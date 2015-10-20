package com.mckuai.mcstar.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.NonNull;

/**
 * Created by kyly on 2015/10/19.
 */
public class CircleBitMap2 {
    private static int BORDER_WIDTH = 2;

    static public Bitmap getCircleBitmap(@NonNull Bitmap bitmap){

        int width = bitmap.getWidth();
        int radius = width / 2;
        Bitmap output = Bitmap.createBitmap(width,width,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0,0,width,width);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(radius, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        bitmap.recycle();
        return  output;
    }

}
