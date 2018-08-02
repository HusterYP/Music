package com.example.yuanping.freemusic.utils;

import android.media.MediaPlayer;
import android.os.PowerManager;
import android.util.Log;

import com.example.yuanping.freemusic.App;
import com.example.yuanping.freemusic.bean.MusicBean;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yuanping on 5/14/18.
 * 音乐播放管理
 * 单例类
 */

public class MusicPlayUtils implements MusicPlayInf, MediaPlayer.OnPreparedListener {
    private int playState = STATE_STOP;
    private static MusicPlayUtils instance = null;
    private MediaPlayer mMediaPlayer = null;
    private MusicBean curMusicBean = null;

    private MusicPlayUtils() {
    }

    public static MusicPlayUtils getInstance() {
        if (instance == null) {
            synchronized (MusicPlayUtils.class) {
                if (instance == null) {
                    instance = new MusicPlayUtils();
                }
            }
        }
        return instance;
    }

    private void initMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setWakeMode(App.sContext, PowerManager.PARTIAL_WAKE_LOCK);
        }
    }

    @Override
    public void prepare() {
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.prepareAsync();
    }

    @Override
    public void loadMusic(MusicBean musicBean) {
        initMediaPlayer();
        if (musicBean != null) {
            try {
                mMediaPlayer.setDataSource(musicBean.getUrl());
            } catch (IOException e) {
                e.printStackTrace();
            }
            curMusicBean = musicBean;
        }
    }

    @Override
    public void play() {
        playState = STATE_PLAYING;
    }

    @Override
    public void stop() {
        playState = STATE_STOP;
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    @Override
    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public int getCurMusicState() {
        return playState;
    }

    @Override
    public int getCurProc() {
        return 0;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }
}
