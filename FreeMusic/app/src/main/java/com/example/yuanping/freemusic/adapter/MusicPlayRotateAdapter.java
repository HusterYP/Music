package com.example.yuanping.freemusic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.yuanping.freemusic.R;
import com.example.yuanping.freemusic.activity.MusicPlay;

/**
 * Created by yuanping on 5/3/18.
 * 音乐播放旋转界面Adapter
 */

public class MusicPlayRotateAdapter extends PagerAdapter {
    private Context mContext;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((MusicPlay) mContext).changeFragmentInterFace();
        }
    };

    public MusicPlayRotateAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
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
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = View.inflate(mContext, R.layout.pager_music_play_rotate, null);
        view.setOnClickListener(mOnClickListener);
        container.addView(view);
        return view;
    }
}
