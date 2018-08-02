package com.example.yuanping.freemusic.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;

import com.example.yuanping.freemusic.adapter.BannerAdapter;
import com.example.yuanping.freemusic.net.BannerDataAsync;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanping on 4/26/18.
 * 轮播图管理工具类
 * 这里将Banner的图片操作相关的方法从BitmapManager中抽取出来了,
 * 因为考虑到Banner的图片操作没有普适性,所以不适合放在BitmapManager中
 */

public class BannerManager {
    private static final String bannerDir = "/banner";

    /**
     * 该类的设计初衷是图片资源管理类,所以BannerDataAsync类没有直接与BannerAdapter发生关系
     *
     * @param isRefresh: 是否从网络请求图片
     */
    public static void setBannerData(Context context, boolean isRefresh, PagerAdapter adapter) {
        if (isRefresh) {
            loadBannerFromNet(context, adapter);
        } else {
            List<Bitmap> data = getBannerBitmapFromCache(context);
            if (data == null) {
                loadBannerFromNet(context, adapter);
            } else {
                ((BannerAdapter) adapter).bindData(data);
            }
        }
    }

    private static List<Bitmap> getBannerBitmapFromCache(Context context) {
        String dir = context.getCacheDir() + bannerDir;
        File file = new File(dir);
        if (!file.exists()) {
            return null;
        }
        List<Bitmap> data = new ArrayList<>();
        File[] files = file.listFiles();
        for (File f : files) {
            data.add(BitmapManager.getBitmap(f));
        }
        return data;
    }

    private static void loadBannerFromNet(final Context context, final PagerAdapter adapter) {
        new BannerDataAsync(new BannerDataAsync.BitmapLoad() {
            @Override
            public void loadComplete(List<Bitmap> bitmaps) {
                bitmaps = BitmapManager.decodeSampledBitmapFromBitmaps(bitmaps, 1020, 360);
                ((BannerAdapter) adapter).bindData(bitmaps);
                cacheBanner(bitmaps, context);
            }
        }).execute();
    }

    private static void cacheBanner(List<Bitmap> bitmaps, Context context) {
        String dir = context.getCacheDir() + bannerDir;
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        String basePath = dir + "/banner";
        for (int i = 0; i < bitmaps.size(); i++) {
            BitmapManager.saveBitmap(basePath + i, bitmaps.get(i), 0.5f);
        }
    }
}
