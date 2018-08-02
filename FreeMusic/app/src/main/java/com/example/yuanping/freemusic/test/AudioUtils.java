package com.example.yuanping.freemusic.test;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Created by yuanping on 4/8/18.
 * 音乐工具类
 */

public class AudioUtils {
    //获取本地音乐
    public static void getLocalMusic(Context context) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.YEAR,
                        MediaStore.Audio.Media.MIME_TYPE,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DATA},
                MediaStore.Audio.Media.MIME_TYPE + "=? or "
                        + MediaStore.Audio.Media.MIME_TYPE + "=?",
                new String[]{"audio/mpeg", "audio/x-ms-wma"}, null);
        if (cursor.moveToFirst()) {
            do {
                Log.d("@HusterYP", String.valueOf("ID: " + cursor.getString(0)
                        + " ; 文件名: " + cursor.getString(1)
                        + " ; 歌曲名: " + cursor.getString(2)
                        + " ; 时长: " + cursor.getString(3)
                        + " ; 歌手名: " + cursor.getString(4)
                        + " ; 专辑名: " + cursor.getString(5)
                        + " ; 年代: " + cursor.getString(6)
                        + " ; 歌曲格式: " + cursor.getString(7)
                        + " ; 文件大小: " + cursor.getString(8)
                        + " ; 文件路径: " + cursor.getString(9)));
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
