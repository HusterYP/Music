package com.example.yuanping.freemusic.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.yuanping.freemusic.App;
import com.example.yuanping.freemusic.bean.MusicBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanping on 5/7/18.
 * 音乐信息获取工具类
 */

public class MusicUtils {
    public static final String LAST_MUSIC_PATH = "last_music";
    public static final String LAST_MUSIC_NAME = "last_music_name";
    public static final int LAST_MUSIC_DEFAULT = -1;

    public static void getLocalMusic(Context context) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media
                        .EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                // 歌曲ID
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                int albumID = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                getAlbumArt(context, albumID);
                // 歌曲的名称
                String tilte = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio
                        .Media.TITLE));
                // 歌曲的专辑名
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio
                        .Media.ALBUM));
                // 歌曲的歌手名
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio
                        .Media.ARTIST));
                // 歌曲文件的全路径
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media
                        .DATA));
                // 歌曲文件的名称
                String display_name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore
                        .Audio.Media.DISPLAY_NAME));
                // 歌曲文件的发行日期
                String year = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio
                        .Media.YEAR));
                // 歌曲的总播放时长
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media
                        .DURATION));
                // 歌曲文件的大小
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media
                        .SIZE));
                Log.d("@HusterYP", String.valueOf(display_name + " ; " + size / 1024f / 1024f));

                /*Log.d("@HusterYP", String.valueOf(id + " ; " + tilte + " ; " + album + " ; " +
                        artist + " ; " + url + " ; " + display_name + " ; " + year + " ; " + " ; " +
                        "" + duration + " ; " + size));*/
            }
            cursor.close();
        }
    }

    // 获取上一次退出时播放的音乐
    public static int getLastMusicID() {
        SharedPreferences preferences = App.sContext.getSharedPreferences(LAST_MUSIC_PATH, Context
                .MODE_PRIVATE);
        int id = preferences.getInt(LAST_MUSIC_NAME, LAST_MUSIC_DEFAULT);
        if (id == LAST_MUSIC_DEFAULT) {
            List<MusicBean> musicBeans = getLocalMusicList(App.sContext);
            return musicBeans.size() == 0 ? -1 : (musicBeans.get(0)).getMusicId();
        } else {
            return id;
        }
    }

    // 存储上一次退出时播放的音乐ID
    public static void saveLastMusic(MusicBean bean) {
        SharedPreferences preferences = App.sContext.getSharedPreferences(LAST_MUSIC_PATH, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (bean != null) {
            editor.putInt(LAST_MUSIC_NAME, bean.getMusicId());
            editor.apply();
        }
    }

    // 根据歌曲ID获取
    public static MusicBean getMusicByID(int id) {
        Cursor cursor = App.sContext.getContentResolver().query(MediaStore.Audio.Media
                .EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media._ID + "=?", new
                String[]{id + ""}, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor != null && cursor.moveToFirst()) {
            return getMusicBeanByCursor(cursor);
        }
        return null;
    }

    //获取本地大于1M的音频文件数量
    public static int getLocalMusicCount(Context context) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media
                .EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.SIZE + ">?", new
                String[]{1024f * 1024f + ""}, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        int count = 0;
        if (cursor != null) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    // 获取本地大于1M的音频文件
    public static List<MusicBean> getLocalMusicList(Context context) {
        List<MusicBean> musicBeans = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media
                .EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.SIZE + ">?", new
                String[]{1024f * 1024f + ""}, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                musicBeans.add(getMusicBeanByCursor(cursor));
            }
            cursor.close();
        }
        return musicBeans;
    }

    // 根据Cursor构造MusicBean
    private static MusicBean getMusicBeanByCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        // 歌曲ID
        int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
        int albumID = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
        // 歌曲的名称
        String tilte = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio
                .Media.TITLE));
        // 歌曲的专辑名
        String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio
                .Media.ALBUM));
        // 歌曲的歌手名
        String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio
                .Media.ARTIST));
        // 歌曲文件的全路径
        String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media
                .DATA));
        // 歌曲的总播放时长
        int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media
                .DURATION));
        return new MusicBean(id, tilte, albumID, album, artist, url, duration);
    }

    // 获取本地对应音乐专辑图片

    public static Bitmap getAlbumArt(Context context, int album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = context.getContentResolver().query(Uri.parse(mUriAlbums + "/" + Integer
                .toString(album_id)), projection, null, null, null);
        String album_art = null;
        if (cur != null) {
            if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
                cur.moveToNext();
                album_art = cur.getString(0);
            }
            cur.close();
        }
        return BitmapFactory.decodeFile(album_art);
    }

    // 根据歌曲名获取歌词

    // 根据歌词获取歌词
}
