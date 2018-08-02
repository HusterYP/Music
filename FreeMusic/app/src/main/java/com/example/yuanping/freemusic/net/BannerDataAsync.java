package com.example.yuanping.freemusic.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import com.example.yuanping.freemusic.adapter.BannerAdapter;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanping on 4/25/18.
 * Banner图片下载
 */

public class BannerDataAsync extends AsyncTask<Void, Void, List<Bitmap>> {
    private List<String> uris = new ArrayList<>();
    private static final int ITEM_COUNT = 5;
    private static final String baseUri = "https://picsum.photos/450/180/?image=";
    private BitmapLoad bitmapLoad;

    public BannerDataAsync(BitmapLoad bitmapLoad) {
        this.bitmapLoad = bitmapLoad;
        for (int i = 0; i < ITEM_COUNT; i++) {
            uris.add(baseUri + (int) (Math.random() * 1000));
        }
    }

    @Override
    protected List<Bitmap> doInBackground(Void... voids) {
        if (uris == null) {
            return null;
        }
        List<Bitmap> data = new ArrayList<>();
        for (int i = 0; i < uris.size(); i++) {
            Bitmap temp = loadBitmap(uris.get(i));
            if (temp != null) {
                data.add(temp);
            }
        }
        return data;
    }

    private Bitmap loadBitmap(String uri) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream in = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(List<Bitmap> bitmaps) {
        super.onPostExecute(bitmaps);
        bitmapLoad.loadComplete(bitmaps);
    }

    public interface BitmapLoad {
        void loadComplete(List<Bitmap> bitmaps);
    }
}
