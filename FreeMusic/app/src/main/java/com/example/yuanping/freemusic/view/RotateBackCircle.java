package com.example.yuanping.freemusic.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.yuanping.freemusic.R;

/**
 * Created by yuanping on 5/1/18.
 * 音乐播放界面旋转部分的半透明背景圆
 */

public class RotateBackCircle extends ImageView {
    private int mRadius;
    private Paint mPaint = new Paint();

    public RotateBackCircle(Context context) {
        super(context);
    }

    public RotateBackCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(R.color.default_rotate_back_circle_color));
    }

    public RotateBackCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RotateBackCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mRadius = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 2;
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, mRadius, mPaint);
    }
}
