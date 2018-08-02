package com.example.yuanping.freemusic.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.yuanping.freemusic.R;

/**
 * Created by yuanping on 4/19/18.
 * 圆角图片
 */

public class RoundRectImageView extends ImageView {
    private final int DEFAULT_RADIUS_X = 40;
    private final int DEFAULT_RADIUS_Y = 30;
    private Bitmap bitmap;
    private int width;
    private int height;
    private int radiusX; //圆角矩形x向半径
    private int radiusY; //圆角矩形y向半径
    private boolean isChange = false; //是否通过代码设置了显示的尺寸

    public RoundRectImageView(Context context) {
        super(context);
    }

    public RoundRectImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundRectImageView);
        radiusX = a.getInteger(R.styleable.RoundRectImageView_radius_x, DEFAULT_RADIUS_X);
        radiusY = a.getInteger(R.styleable.RoundRectImageView_radius_y, DEFAULT_RADIUS_Y);
        a.recycle();
    }

    public RoundRectImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RoundRectImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                              int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setSize(int width, int height) {
        if (width > 0) {
            this.width = width;
            isChange = true;
        }
        if (height > 0) {
            this.height = height;
            isChange = true;
        }
        if (isChange) {
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (!isChange) {
            width = getMeasuredWidth();
            height = getMeasuredHeight();
        }
        if (drawable == null) {
            return;
        }
        if (width <= 0) {
            return;
        }
        bitmap = getRoundRectBitmap(drawable);
        if (bitmap == null) {
            return;
        }
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    private Bitmap getRoundRectBitmap(Drawable drawable) {
        Bitmap source = null;
        Bitmap temp;

        if (drawable instanceof ColorDrawable) {
            int color = ((ColorDrawable) drawable).getColor();
            source = Bitmap.createBitmap(new int[]{color}, width, height, Bitmap.Config.ARGB_8888);
        }
        if (drawable instanceof BitmapDrawable) {
            source = Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(), width,
                    height, false);
        }
        if (source == null) {
            return null;
        }
        temp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(temp);
        Paint paint = new Paint();
        RectF rectF = new RectF(0, 0, width, height);
        canvas.drawRoundRect(rectF, radiusX, radiusY, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(source, 0, 0, paint);
        return temp;
    }
}
