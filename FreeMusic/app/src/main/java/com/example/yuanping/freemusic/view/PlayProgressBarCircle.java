package com.example.yuanping.freemusic.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.example.yuanping.freemusic.R;
import com.example.yuanping.freemusic.utils.MusicPlayInf;
import com.example.yuanping.freemusic.utils.MusicPlayUtils;
import com.example.yuanping.freemusic.utils.ValueConverse;

/**
 * Created by yuanping on 4/27/18.
 * 主界面底部圆形播放按钮
 * 需要实现功能:
 * 1. 模仿网易云,需要实现进度变更
 * 2. 需要实现不同模式下的颜色改变(夜间模式和日间模式)
 */

public class PlayProgressBarCircle extends ProgressBar {
    //默认取值
    private static final int DEFAULT_STOP_CIRCLE_COLOR = 0xFF515151; //暂停播放时,默认外圆颜色
    private static final int DEFAULT_START_CIRCLE_COLOR = 0xFFE6E6E6; //正在播放时,默认外圆颜色
    private static final int DEFAULT_PROGRESS_COLOR = 0xFFCD5555; //进度颜色
    private static final int DEFAULT_STOP_INNER_COLOR = 0xFF515151;//暂停播放时,圆内部图案颜色
    private static final int DEFAULT_START_INNER_COLOR = 0xFFCD5555;//正在播放时,圆内部图案颜色
    private static final int DEFAULT_RADIUS = 10; //外圆半径;dp
    private static final int DEFAULT_CIRCLE_WIDTH = 5; //默认画笔宽度;px
    //变量
    private Paint mPaint = new Paint();
    private int mRadius;
    private int stopCircleColor;
    private int startCircleColor;
    private int progressColor;
    private int stopInnerColor;
    private int startInnerColor;
    private boolean isPlaying = false; //是否正在播放
    private int circleWidth;
    private int startInnerHeight; //正在播放时,内部图案高度
    private int startInnerMargin;//正在播放时,内部图案默认横向间隔;px

    public PlayProgressBarCircle(Context context) {
        this(context, null);
    }

    public PlayProgressBarCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayProgressBarCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PlayProgressBarCircle(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable
                .PlayProgressBarCircle);
        mRadius = (int) a.getDimension(R.styleable.PlayProgressBarCircle_radius, ValueConverse
                .dpTopx(DEFAULT_RADIUS));
        stopCircleColor = a.getColor(R.styleable.PlayProgressBarCircle_stop_circle_color,
                DEFAULT_STOP_CIRCLE_COLOR);
        startCircleColor = a.getColor(R.styleable.PlayProgressBarCircle_start_circle_color,
                DEFAULT_START_CIRCLE_COLOR);
        progressColor = a.getColor(R.styleable.PlayProgressBarCircle_progress_color,
                DEFAULT_PROGRESS_COLOR);
        stopInnerColor = a.getColor(R.styleable.PlayProgressBarCircle_stop_inner_color,
                DEFAULT_STOP_INNER_COLOR);
        startInnerColor = a.getColor(R.styleable.PlayProgressBarCircle_start_inner_color,
                DEFAULT_START_INNER_COLOR);
        circleWidth = a.getInt(R.styleable.PlayProgressBarCircle_circle_width,
                DEFAULT_CIRCLE_WIDTH);
        startInnerHeight = mRadius / 2;
        startInnerMargin = startInnerHeight;
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        a.recycle();
    }


    /**
     * 绘制过程:
     * 1. 绘制外圆
     * 2. 绘制进度
     * 3. 绘制圆内部图案
     *
     * @param canvas
     */
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft() + circleWidth, getPaddingTop() + circleWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        //绘制外圆
        mPaint.setStrokeWidth(circleWidth);
        if (isPlaying) {
            mPaint.setColor(startCircleColor);
        } else {
            mPaint.setColor(stopCircleColor);
        }
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        //绘制进度条
        mPaint.setColor(progressColor);
        float sweepAngle = getProgress() * 1.0f / getMax() * 360;
        canvas.drawArc(new RectF(0, 0, mRadius * 2, mRadius * 2),
                -90, sweepAngle, false, mPaint);
        //绘制内部图案
        if (isPlaying) {
            //两条竖线
            mPaint.setColor(startInnerColor);
            canvas.drawLine(mRadius - startInnerMargin / 2,
                    mRadius - startInnerHeight / 2,
                    mRadius - startInnerMargin / 2,
                    mRadius + startInnerHeight / 2, mPaint);
            canvas.drawLine(mRadius + startInnerMargin / 2,
                    mRadius - startInnerHeight / 2,
                    mRadius + startInnerMargin / 2,
                    mRadius + startInnerHeight / 2, mPaint);
        } else {
            //三角形
            mPaint.setColor(stopInnerColor);
            mPaint.setStyle(Paint.Style.FILL);
            Path path = new Path();
            path.moveTo(mRadius - startInnerMargin / 2, mRadius - startInnerHeight * 2 / 3);
            path.lineTo(mRadius - startInnerMargin / 2, mRadius + startInnerHeight * 2 / 3);
            path.lineTo(mRadius + startInnerMargin * 2 / 3, mRadius);
            path.close();
            canvas.drawPath(path, mPaint);
        }
        canvas.restore();
    }

    public void changeState() {
        isPlaying = !isPlaying;
        invalidate();
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
