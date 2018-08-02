package com.example.yuanping.freemusic.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.yuanping.freemusic.utils.ChangeTheme;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by yuanping on 4/28/18.
 * 公共Activity:
 * 提取主题改变
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ChangeTheme.changeTheme(this);
        super.onCreate(savedInstanceState);
    }
}
