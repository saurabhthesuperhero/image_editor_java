package com.thirstyfish.downloadjavaapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.nio.charset.Charset;
import java.security.MessageDigest;

public class CenterCropPadding extends BitmapTransformation {
    private static final String ID = "com.example.app.CenterCropPadding";
    private static final byte[] ID_BYTES = ID.getBytes(Charset.forName("UTF-8"));

    private int width;
    private int height;
    private Paint paint;

    public CenterCropPadding(Context context) {
        width = context.getResources().getDimensionPixelSize(R.dimen.image_width);
        height = context.getResources().getDimensionPixelSize(R.dimen.image_height);
        paint = new Paint();
        paint.setColor(Color.WHITE);
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int imageWidth = toTransform.getWidth();
        int imageHeight = toTransform.getHeight();
        float scale = Math.max((float) width / imageWidth, (float) height / imageHeight);
        int scaledWidth = (int) (imageWidth * scale);
        int scaledHeight = (int) (imageHeight * scale);
        int left = (width - scaledWidth) / 2;
        int top = (height - scaledHeight) / 2;

        Bitmap bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, left, height, paint);
        canvas.drawRect(width - left, 0, width, height, paint);
        canvas.drawRect(left, 0, width - left, top, paint);
        canvas.drawRect(left, height - top, width - left, height, paint);
        canvas.drawBitmap(toTransform, left, top, null);
        return bitmap;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}
