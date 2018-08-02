package com.example.yuanping.freemusic.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.yuanping.freemusic.utils.BitmapManager;

/**
 * Created by yuanping on 4/15/18.
 */

public class CircleImage extends ImageView {

    private int radius;

    public CircleImage(Context context) {
        super(context);
    }

    public CircleImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircleImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        radius = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 2;
        Bitmap bitmap = getBitmap();
        if (bitmap == null)
            return;
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    private Bitmap getBitmap() {
        Drawable drawable = getDrawable();
        if (drawable == null)
            return null;
        //如果设置的是图片
        if (drawable instanceof BitmapDrawable) {
            Bitmap origin = ((BitmapDrawable) drawable).getBitmap();
            return BitmapManager.getCircleBitmap(origin, radius);
        }
        //如果设置的是颜色
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        Bitmap bitmap = Bitmap.createBitmap(radius, radius, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, radius, radius);
        drawable.draw(canvas);
        canvas.drawCircle(radius / 2, radius / 2, radius, paint);
        return bitmap;
    }
}
