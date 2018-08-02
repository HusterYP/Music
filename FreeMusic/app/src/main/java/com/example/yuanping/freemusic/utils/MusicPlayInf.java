package com.example.yuanping.freemusic.utils;

import com.example.yuanping.freemusic.bean.MusicBean;

/**
 * Created by yuanping on 5/24/18.
 * 音乐播放接口
 */

public interface MusicPlayInf {
    int STATE_PLAYING = 0; // 正在播放
    int STATE_STOP = 1; // 暂停播放

    // 准备
    void prepare();

    // 加载
    void loadMusic(MusicBean musicBean);

    // 播放音乐
    void play();

    // 停止音乐
    void stop();

    void release();

    // 获取当前播放状态
    int getCurMusicState();

    // 获取当前播放进度
    int getCurProc();
}
