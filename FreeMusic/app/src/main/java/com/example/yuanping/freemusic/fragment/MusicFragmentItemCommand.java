package com.example.yuanping.freemusic.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yuanping.freemusic.R;
import com.example.yuanping.freemusic.adapter.BannerAdapter;
import com.example.yuanping.freemusic.view.BannerPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by yuanping on 4/17/18.
 * "推荐"界面
 */

public class MusicFragmentItemCommand extends Fragment {
    @BindView(R.id.banner_pager)
    BannerPager bannerPager;
    private PagerAdapter adapter;

    private Unbinder mUnbinder = null;

    public MusicFragmentItemCommand() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_main_music_command,
                container,
                false);
        mUnbinder = ButterKnife.bind(this, view);
        initBanner();
        return view;
    }

    //初始化和开启轮播图
    private void initBanner() {
        adapter = new BannerAdapter(getContext(), bannerPager);
        bannerPager.setAdapter(adapter);
        bannerPager.setCurrentItem(Integer.MAX_VALUE / 2);
    }

    @Override
    public void onPause() {
        super.onPause();
        //暂停轮播
        bannerPager.onStopPlay();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //轮播图资源回收
        bannerPager.clear();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    /*
    //下拉刷新
    private void initSwipeRefresh() {
        if (getActivity() != null) {
            TypedArray a = getActivity().getTheme().obtainStyledAttributes(new int[]{R.attr
                    .swipe_refresh_background, R.attr.swipe_refresh_pre});
            int preColor = a.getColor(1, 0xCD5555);
            int background = a.getColor(0, 0x444444);
            a.recycle();
            refreshLayout.setColorSchemeColors(preColor);
            refreshLayout.setProgressBackgroundColorSchemeColor(background);
        }
        refreshLayout.setOnRefreshListener(this);
    }
    */

    /*private List<Bitmap> addView() {
        List<Bitmap> music = new ArrayList<>();
        Drawable d = getResources().getDrawable(R.mipmap.sun);
        music.add(((BitmapDrawable)d).getBitmap());
        d = getResources().getDrawable(R.mipmap.test_one);
        music.add(((BitmapDrawable)d).getBitmap());
        d = getResources().getDrawable(R.mipmap.test_two);
        music.add(((BitmapDrawable)d).getBitmap());
        d = getResources().getDrawable(R.mipmap.test_three);
        music.add(((BitmapDrawable)d).getBitmap());
        return music;
    }*/
}
