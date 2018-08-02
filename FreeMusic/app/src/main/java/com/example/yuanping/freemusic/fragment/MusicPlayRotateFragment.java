package com.example.yuanping.freemusic.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yuanping.freemusic.R;
import com.example.yuanping.freemusic.adapter.MusicPlayRotateAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by yuanping on 5/1/18.
 * 音乐播放界面,唱片旋转界面
 */

public class MusicPlayRotateFragment extends Fragment {
    @BindView(R.id.music_play_rotate_pager)
    ViewPager mRotatePager;

    //TODO True of False ??
    // (为了避免因为手势点击,造成多次的创建,也可以不这样做,直接在需要的地方设置为成员变量,然后判null,是否重新创建对象也可)
    private static final MusicPlayRotateFragment rotateFragment = new MusicPlayRotateFragment();
    private Unbinder mUnbinder = null;

    public static MusicPlayRotateFragment getInstance() {
        return rotateFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_play_rotate, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initPager();
        return view;
    }

    private void initPager() {
        MusicPlayRotateAdapter adapter = new MusicPlayRotateAdapter(getContext());
        mRotatePager.setAdapter(adapter);
        mRotatePager.setCurrentItem(Integer.MAX_VALUE / 2);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
