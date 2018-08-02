package com.example.yuanping.freemusic.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.yuanping.freemusic.R;

/**
 * Created by yuanping on 4/12/18.
 * 用于改变主题,即夜间模式等
 */

public class ChangeTheme {
    public static final int DAY_THEME = 1;
    public static final int NIGHT_THEME = 2;
    private static final String THEME_STATE = "theme_state";

    public static void changeTheme(Activity activity) {
        SharedPreferences sharedPref = activity.getApplication().getSharedPreferences
                (THEME_STATE, Context.MODE_PRIVATE);
        int state = sharedPref.getInt(THEME_STATE, DAY_THEME);
        switch (state) {
            case DAY_THEME: {
                activity.setTheme(R.style.DayTheme);
                return;
            }
            case NIGHT_THEME: {
                activity.setTheme(R.style.NightTheme);
                return;
            }
        }
    }

    //主题设置
    public static void setState(int state, Activity activity) {
        if (state != DAY_THEME && state != NIGHT_THEME) {
            throw new IllegalArgumentException("非法状态参数!");
        }
        SharedPreferences sharedPref = activity.getSharedPreferences(THEME_STATE, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(THEME_STATE, state);
        editor.apply();
    }
}
