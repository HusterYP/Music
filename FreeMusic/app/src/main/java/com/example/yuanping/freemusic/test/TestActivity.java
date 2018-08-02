package com.example.yuanping.freemusic.test;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.example.yuanping.freemusic.R;
import com.example.yuanping.freemusic.utils.MusicUtils;
import com.example.yuanping.freemusic.view.PlayProgressBarRoundRect;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 测试Activity
 */

public class TestActivity extends AppCompatActivity {
    @BindView(R.id.img)
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Bitmap bitmap = MusicUtils.getAlbumArt(this, 438);
        mImageView.setImageBitmap(bitmap);
    }
}
