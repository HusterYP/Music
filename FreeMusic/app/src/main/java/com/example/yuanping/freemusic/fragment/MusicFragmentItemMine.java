package com.example.yuanping.freemusic.fragment;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yuanping.freemusic.R;
import com.example.yuanping.freemusic.activity.LocalMusic;
import com.example.yuanping.freemusic.utils.MusicUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by yuanping on 5/7/18.
 * 主界面中 "我的"
 */

public class MusicFragmentItemMine extends Fragment implements SwipeRefreshLayout
        .OnRefreshListener {

    @BindView(R.id.mine_swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.mine_top_framelayout)
    FrameLayout mTopLayout;
    @BindView(R.id.music_mine_local_count)
    TextView mLocalCount;
    @BindView(R.id.music_mine_cur_count)
    TextView mCurCount;
    @BindView(R.id.music_mine_down_load_count)
    TextView mDownloadCount;

    private Unbinder mUnbinder;

    public MusicFragmentItemMine() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_music_mine, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        initSwipeRefresh();
        initCount();
    }

    //设置数量
    private void initCount() {
        int localCount = MusicUtils.getLocalMusicCount(getContext());
        mLocalCount.setText(getResources().getString(R.string.count_set, localCount));
    }

    //设置下拉刷新
    private void initSwipeRefresh() {
        if (getActivity() != null) {
            TypedArray a = getActivity().getTheme().obtainStyledAttributes(new int[]{R.attr
                    .swipe_refresh_background, R.attr.swipe_refresh_pre});
            int preColor = a.getColor(1, 0xCD5555);
            int background = a.getColor(0, 0x444444);
            a.recycle();
            mRefreshLayout.setColorSchemeColors(preColor);
            mRefreshLayout.setProgressBackgroundColorSchemeColor(background);
        }
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(false);
    }

    // "本地音乐"
    @OnClick(R.id.music_mine_local_item)
    public void localItemClick(View view) {
        Intent intent = new Intent(getActivity(), LocalMusic.class);
        startActivity(intent);
    }

    // "最近播放"
    @OnClick(R.id.music_mine_cur_item)
    public void curPlayClick(View view) {

    }

    // "下载管理"
    @OnClick(R.id.music_mine_download_item)
    public void downloadClick(View view) {

    }
}
