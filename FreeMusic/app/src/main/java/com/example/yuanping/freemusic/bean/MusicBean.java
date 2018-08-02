package com.example.yuanping.freemusic.bean;

/**
 * Created by yuanping on 5/19/18.
 * 歌曲信息
 */

public class MusicBean {
    private int musicId; //歌曲ID
    private String musicName; //歌曲名
    private int albumId; //专辑ID
    private String albumName; //专辑名
    private String singer; //歌手
    private String url; //歌曲文件全路径
    private int duration; //总时长

    public MusicBean() {
    }

    public MusicBean(int musicId, String musicName, int albumId, String albumName, String
            singer, String url, int duration) {
        this.musicId = musicId;
        this.musicName = musicName;
        this.albumId = albumId;
        this.albumName = albumName;
        this.singer = singer;
        this.url = url;
        this.duration = duration;
    }

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
