package com.example.yuanping.freemusic.utils;

import android.util.TypedValue;

import com.example.yuanping.freemusic.App;

/**
 * Created by yuanping on 5/2/18.
 * 数值转换工具类
 */

public class ValueConverse {
    public static int dpTopx(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, App.sContext.getResources().getDisplayMetrics());
    }
}
