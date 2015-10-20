package com.mckuai.mcstar.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.NonNull;

/**
 * Created by kyly on 2015/10/20.
 */
public class CircleBitmap {
    private static int mBorderThickness = 0;
    private static int circleRadius;
    private static int radius;

    public static Bitmap getCircleBitmap(@NonNull Bitmap bitmap,int width){
        circleRadius = width / 2;
        radius = circleRadius - 2 * mBorderThickness;
//        Drawable drawable = context.getResources().getDrawable(android.support.v7.appcompat.R.drawable.abc_ic_clear_mtrl_alpha);
        Bitmap tagBitmap = Bitmap.createBitmap(width,width,Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(tagBitmap);
        drawCircleBorder(canvas, 0xFF00FF);
        Bitmap circleBitmap = getCroppedRoundBitmap(bitmap,radius);
        canvas.drawBitmap(circleBitmap, circleRadius - radius, circleRadius - radius, null);
        canvas.save();
//        bitmap.recycle();
//        bitmap = null;
        return tagBitmap;
    }

    private static Bitmap getCroppedRoundBitmap(@NonNull Bitmap bitmap,int radius){
        int diameter = radius * 2;
        Bitmap tagBitmap = Bitmap.createBitmap(diameter,diameter,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tagBitmap);
        Rect srcRect = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        Rect dstrect = new Rect(0,0,diameter,diameter);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(radius, radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, srcRect, dstrect, paint);
        canvas.save();
        //bitmap.recycle();
        //bitmap = null;
        return tagBitmap;
    }

    private static void drawCircleBorder(@NonNull Canvas canvas,int color){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
//        paint.setFilterBitmap(true);
//        paint.setDither(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mBorderThickness);
        canvas.drawCircle(circleRadius, circleRadius, circleRadius, paint);
    }
}
