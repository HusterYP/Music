package com.example.yuanping.freemusic.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.example.yuanping.freemusic.R;
import com.example.yuanping.freemusic.utils.BitmapManager;
import com.example.yuanping.freemusic.utils.ValueConverse;

/**
 * Created by yuanping on 5/2/18.
 * 音乐播放旋转界面的前景圆形
 * 这里直接设置了默认高度,当然,如果需要可拓展的话,可以抽取出来,自定义属性
 */

public class RotatePreCircle extends ImageView {
    private static final int DEFAULT_WIDTH = ValueConverse.dpTopx(270); // 默认宽度
    private static final int DEFAULT_HEIGHT = ValueConverse.dpTopx(270); //默认高度
    private static final int DEFAULT_MARGIN_OF_BIRADIUS_SERADIUS = 10; // 次外层圆和图片圆的默认间距
    private Bitmap mBitmap = null;
    private Paint mPaint = new Paint();
    private LinearGradient mLinearGradient = null;
    int width;
    int height;
    int firstRadius;
    int secondaryRadius;
    int bitmapRadius;

    public RotatePreCircle(Context context) {
        super(context);
    }

    public RotatePreCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RotatePreCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RotatePreCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context) {
        Drawable drawable = getDrawable();
        if (drawable instanceof BitmapDrawable) {
            mBitmap = ((BitmapDrawable) drawable).getBitmap();
        }
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设置默认高度
        setMeasuredDimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        firstRadius = Math.min(width, height) / 2;
        secondaryRadius = firstRadius * 2 / 3;
        bitmapRadius = secondaryRadius - DEFAULT_MARGIN_OF_BIRADIUS_SERADIUS;
    }

    /**
     * 1. 画最外层的大圆: 注意渐变
     * 2. 次外层圆
     * 3. 画圆形图片
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());

        // 1. 画最外层的大圆
        if (mLinearGradient == null) {
            mLinearGradient = new LinearGradient(0, 0, width / 2,
                    height / 2, getResources().getColor(R.color
                    .default_rotate_pre_circle_start_color), getResources().getColor(R.color
                    .default_rotate_pre_circle_end_color), Shader.TileMode.MIRROR);
        }
        mPaint.setShader(mLinearGradient);
        canvas.drawCircle(width / 2, height / 2, firstRadius, mPaint);

        // 2. 画次外层的圆
        mPaint.setShader(null);
        mPaint.setColor(getResources().getColor(R.color.default_rotate_sencondary_circle_color));
        canvas.drawCircle(width / 2, height / 2, secondaryRadius, mPaint);

        // 3. 画圆形图片
        if (mBitmap == null) {
            return;
        }
        Bitmap target = BitmapManager.getCircleBitmap(mBitmap, bitmapRadius);
        canvas.drawBitmap(target, firstRadius - bitmapRadius, firstRadius - bitmapRadius, null);
        canvas.restore();
    }
}
