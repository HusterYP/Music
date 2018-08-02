package com.example.yuanping.freemusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yuanping.freemusic.R;
import com.example.yuanping.freemusic.adapter.MainViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by yuanping on 4/16/18.
 */

public class MusicFragment extends Fragment {
    @BindView(R.id.fragment_main_music_pager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    Unbinder mUnbinder = null;
    private List<Fragment> mFragments = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_main_music, container,
                false);
        mUnbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        if (mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.add(new MusicFragmentItemCommand());
            mFragments.add(new MusicFragmentItemMine());
            mFragments.add(new MusicFragmentItemCommand());
        }
        List<String> titles = new ArrayList<>();
        titles.add(getResources().getString(R.string.command));
        titles.add(getResources().getString(R.string.mine));
        titles.add(getResources().getString(R.string.radio_station));
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getChildFragmentManager(),
                mFragments, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
