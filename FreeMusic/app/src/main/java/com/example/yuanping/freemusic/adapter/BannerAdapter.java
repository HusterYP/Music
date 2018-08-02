package com.example.yuanping.freemusic.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.yuanping.freemusic.R;
import com.example.yuanping.freemusic.net.BannerDataAsync;
import com.example.yuanping.freemusic.utils.BannerManager;
import com.example.yuanping.freemusic.utils.BitmapManager;
import com.example.yuanping.freemusic.view.BannerPager;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by yuanping on 4/22/18.
 */

public class BannerAdapter extends PagerAdapter {
    private static final String DURING_DATE = "date";
    private List<Bitmap> data = null;
    private Context context;
    private ViewPager bannerPager;

    public BannerAdapter(Context context, ViewPager bannerPager) {
        this.context = context;
        this.bannerPager = bannerPager;
        boolean isRefresh = false;
        Date date = Calendar.getInstance().getTime();
        SharedPreferences preferences = ((Activity) context).getPreferences(Context.MODE_PRIVATE);
        long lastDate = preferences.getLong(DURING_DATE, 0);
        SharedPreferences.Editor editor = preferences.edit();
        if (lastDate <= 0) {
            editor.putLong(DURING_DATE, date.getTime());
            editor.apply();
        } else {
            long dt = date.getTime() - lastDate;
            long duringDay = TimeUnit.DAYS.convert(dt, TimeUnit.MILLISECONDS);
            if (duringDay >= 1) {
                isRefresh = true;
                editor.putLong(DURING_DATE, date.getTime());
                editor.apply();
            }
        }
        BannerManager.setBannerData(context, isRefresh, this);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public void bindData(List<Bitmap> temp) {
        data = temp;
        notifyDataSetChanged();
        ((BannerPager) bannerPager).onStartPlay();
        ((BannerPager) bannerPager).startTimer();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = View.inflate(context, R.layout.banner, null);
        final ImageView img = view.findViewById(R.id.round_rect_view);
        if (data != null) {
            final int curIndex = position % data.size();
            img.setImageBitmap(data.get(curIndex));
        } else {
            img.setBackgroundColor(Color.GRAY);
        }
        container.addView(view);
        return view;
    }
}
