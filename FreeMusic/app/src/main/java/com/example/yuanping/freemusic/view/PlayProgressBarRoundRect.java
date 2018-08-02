package com.example.yuanping.freemusic.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.ProgressBar;

import com.example.yuanping.freemusic.R;

/**
 * Created by yuanping on 4/30/18.
 * 长条形的进度条:
 * 考虑扩展的话,可以将默认值设置为自定义属性
 */

public class PlayProgressBarRoundRect extends ProgressBar {
    private int mWidth = 100; // 默认总长度
    private int mHeight = 8; // 默认总高度
    private int mRectRadius = 4; // 默认圆角半径
    private int mCircleRadius = 18; // 默认末尾圆形半径
    private Paint mPaint = new Paint();

    public PlayProgressBarRoundRect(Context context) {
        super(context);
    }

    public PlayProgressBarRoundRect(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayProgressBarRoundRect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PlayProgressBarRoundRect(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mWidth = metrics.widthPixels * 3 / 4; // 默认宽度为总宽度的3/4
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth + mCircleRadius * 2, mCircleRadius * 2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int progress;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                int downX = (int) event.getX();
                progress = (int) (downX * 1.0f / getMeasuredWidth() * getMax());
                setProgress(progress);
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                int moveX = (int) event.getX();
                progress = (int) (moveX * 1.0f / getMeasuredWidth() * getMax());
                setProgress(progress);
                break;
            }
            case MotionEvent.ACTION_UP: {
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 1. 画外层圆角矩形
     * 2. 预加载圆角矩形
     * 3. 根据当前progress画内层圆角矩形
     * 4. 画最后的圆形
     */
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft() + mCircleRadius, getPaddingTop() + mCircleRadius -
                mRectRadius);
        // 1. 画外层圆角矩形
        RectF rectF = new RectF(0, 0, mWidth, mHeight);
        mPaint.setColor(getResources().getColor(R.color.default_progress_bar_outer_color));
        canvas.drawRoundRect(rectF, mRectRadius, mRectRadius, mPaint);

        // 2. 画预加载矩形
        // 本来是打算对外暴露一个接口来设置预加载进度的,但是其实可以利用现成的方法
        int mPreProgress = getSecondaryProgress();
        int preProgressWidth = (int) (mPreProgress * 1.0f / getMax() * mWidth);
        rectF.set(0, 0, preProgressWidth, mHeight);
        mPaint.setColor(getResources().getColor(R.color.default_progress_bar_pre_color));
        canvas.drawRoundRect(rectF, mRectRadius, mRectRadius, mPaint);

        // 3. 根据当前progress画内层圆角矩形
        mPaint.setColor(getResources().getColor(R.color.default_progress_bar_inner_color));
        int progress = getProgress();
        int progressWidth = (int) (progress * 1.0f / getMax() * mWidth);
        rectF.set(0, 0, progressWidth, mHeight);
        canvas.drawRoundRect(rectF, mRectRadius, mRectRadius, mPaint);

        // 4. 画最后的圆形
        // 4.1 画外层圆形
        mPaint.setColor(getResources().getColor(R.color.default_progress_bar_circle_color));
        canvas.drawCircle(progressWidth, mHeight / 2, mCircleRadius, mPaint);
        // 4.2 画内层圆形
        mPaint.setColor(getResources().getColor(R.color.default_progress_bar_inner_color));
        canvas.drawCircle(progressWidth, mHeight / 2, mRectRadius, mPaint);
        canvas.restore();
    }
}
