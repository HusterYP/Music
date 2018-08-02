package com.example.yuanping.freemusic.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanping on 4/19/18.
 * 轮播图
 * 实现思路为: 将五个View水平排列,然后屏幕上能够看到的是处于中间那个View,
 * 当手指或者计时器时间到,需要水平切换时,在手指Up或者水平滑动到位置的时候,
 * 同时调用RequestLayout(),重新恢复原来View序列的排列,同时重新设置
 * 显示的图片,因为最中间显示的那个图片前后没有改变,所以视觉效果上没有差别
 */

public class BannerViewTemp extends ViewGroup {
    private static final int DEFAULT_COUNT = 5; //默认显示的图片数量
    private Scroller scroller;
    private int itemMargain; //每个Item之间的间隔
    private int screenWidth; //屏幕宽度
    private int itemWidth; //每个Item宽度
    private int itemHeight; //每个Item高度
    private RoundRectImageView imageViews[] = new RoundRectImageView[DEFAULT_COUNT];
    private List<Bitmap> bitmapList = new ArrayList<>(); //存储轮播的图片
    private int currentIndex; //当前可见图片序列

    public BannerViewTemp(Context context) {
        super(context);
    }

    public BannerViewTemp(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BannerViewTemp(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BannerViewTemp(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        itemWidth = (int) (screenWidth * 3.0f / 4);
        itemHeight = (int) (itemWidth * 3.0f / 7);
        itemMargain = (int) (itemWidth * 1.0f / 6);
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i] = new RoundRectImageView(context);
            addView(imageViews[i]);
        }
    }

    public void setBitmapList(List<Bitmap> bitmapList) {
        if (bitmapList == null) {
            return;
        }
        this.bitmapList = bitmapList;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i].setSize(itemWidth, itemHeight);
        }
        setMeasuredDimension(itemWidth * 3 + itemMargain * 2, itemHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        MarginLayoutParams layoutParams = (MarginLayoutParams) getLayoutParams();
        layoutParams.width = itemWidth * imageViews.length + itemMargain * (imageViews.length - 1);
        layoutParams.height = itemHeight;
        setLayoutParams(layoutParams);
        //开始Layout的坐标
//        int startX = (int) ((screenWidth - itemWidth) / 2.0f - itemWidth * 2 - itemMargain * 2);
        int startX = l;
        Log.d("@HusterYP", String.valueOf(l + " ; " + r + " ; " + layoutParams.width + " ; " +
                layoutParams.height + " ; " + screenWidth));
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i].layout(startX, 0, startX + itemWidth, itemHeight);
            startX = startX + itemWidth + itemMargain;
        }
        setWillNotDraw(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int startX = 0;
        int dx = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                startX = (int) event.getX();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                dx = (int) (startX - event.getX());
                scrollBy(dx,0);
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
        }
        startX = (int) event.getX();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmapList.size() > 0) {
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[i].setImageBitmap(bitmapList.get(i));
            }
        }
    }
}
