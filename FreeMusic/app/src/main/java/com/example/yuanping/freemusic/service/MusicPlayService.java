package com.example.yuanping.freemusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.example.yuanping.freemusic.activity.MainActivity;
import com.example.yuanping.freemusic.bean.MusicBean;
import com.example.yuanping.freemusic.utils.MusicPlayInf;
import com.example.yuanping.freemusic.utils.MusicPlayUtils;
import com.example.yuanping.freemusic.utils.MusicUtils;

/**
 * 音乐播放服务
 */

public class MusicPlayService extends Service{
    private final MusicPlayInf mMusicPlayInf = MusicPlayUtils.getInstance();
    private int musicId = -1;
    private MusicBean curMusicBean = null;

    public MusicPlayService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        musicId = intent.getIntExtra(MainActivity.MUSIC_ID, -1);
        if (musicId != -1) {
            curMusicBean = MusicUtils.getMusicByID(musicId);
            mMusicPlayInf.loadMusic(curMusicBean);
            mMusicPlayInf.prepare();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMusicPlayInf.release();
    }
}
