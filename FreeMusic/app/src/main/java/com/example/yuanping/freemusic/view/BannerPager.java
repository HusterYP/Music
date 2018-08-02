package com.example.yuanping.freemusic.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yuanping on 4/22/18.
 * 轮播图实现
 */

public class BannerPager extends ViewPager {

    private static final int CHANE_PERIOD = 3000;
    private Handler handler = new MyHandler(this);
    private boolean isTouch = false;
    private Timer timer = null;

    public BannerPager(@NonNull Context context) {
        super(context);
    }

    public BannerPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void onStopPlay() {
        isTouch = true;
    }

    public void onStartPlay() {
        isTouch = false;
    }

    public void startTimer() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new MyTask(), 0, CHANE_PERIOD);
        }
    }

    public void clear() {
        if (timer != null) {
            timer.cancel();
        }
    }

    class MyTask extends TimerTask {
        @Override
        public void run() {
            if (!isTouch) {
                handler.sendEmptyMessage(0);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                onStopPlay();
                break;
            }
            case MotionEvent.ACTION_UP: {
                onStartPlay();
                break;
            }
        }
        return super.onTouchEvent(ev);
    }

    static class MyHandler extends Handler {
        private ViewPager viewPager;

        public MyHandler(ViewPager viewPager) {
            this.viewPager = viewPager;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }
}
