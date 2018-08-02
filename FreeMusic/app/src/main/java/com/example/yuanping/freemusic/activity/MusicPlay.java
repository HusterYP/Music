package com.example.yuanping.freemusic.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.yuanping.freemusic.R;
import com.example.yuanping.freemusic.fragment.MusicPlayLrcFragment;
import com.example.yuanping.freemusic.fragment.MusicPlayRotateFragment;
import com.example.yuanping.freemusic.utils.BitmapManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 音乐播放界面
 */

public class MusicPlay extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar mToolbar;
    @BindView(R.id.music_play_back_ground)
    ImageView mBackground;

    ActionBar mActionBar;
    private boolean isShowLrc = false; // 是否显示歌词界面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setTitle("");
            mActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        }
        getSupportFragmentManager().beginTransaction().add(R.id.music_play_frame_layout,
                MusicPlayRotateFragment.getInstance()).commit();
        setDefaultBackGround();
    }

    //TODO : 替换
    //设置默认的背景色,经过模糊处理图片
    private void setDefaultBackGround() {
        int scaleRatio = 5;
        int blurRatio = 10;
        Bitmap origin = BitmapFactory.decodeResource(getResources(), R.mipmap.music);
        Bitmap blurBitmap = BitmapManager.blurBitmap(origin, scaleRatio, blurRatio);
        mBackground.setImageBitmap(blurBitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @OnClick(R.id.music_play_frame_layout)
    public void changeFragment() {
        changeFragmentInterFace();
    }

    // 对ViewPager的每一个Item设置点击切换接口
    public void changeFragmentInterFace() {
        isShowLrc = !isShowLrc;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (isShowLrc) {
            transaction.replace(R.id.music_play_frame_layout,
                    MusicPlayLrcFragment.getInstance());
        } else {
            transaction.replace(R.id.music_play_frame_layout,
                    MusicPlayRotateFragment.getInstance());
        }
        transaction.commit();
    }
}
