package com.example.yuanping.freemusic.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.yuanping.freemusic.R;
import com.example.yuanping.freemusic.adapter.MainViewPagerAdapter;
import com.example.yuanping.freemusic.fragment.LocalMusicFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocalMusic extends BaseActivity {
    @BindView(R.id.tool_bar)
    Toolbar mToolbar;
    @BindView(R.id.local_music_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initToolbar();
        initPager();
    }

    // 初始化Toolbar
    private void initToolbar() {
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setTitle(getResources().getString(R.string.music_mine_local));
            mActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        }
        mToolbar.setOnCreateContextMenuListener(this);
    }

    // 初始化ViewPager
    private void initPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new LocalMusicFragment());
        fragments.add(new LocalMusicFragment());
        fragments.add(new LocalMusicFragment());
        fragments.add(new LocalMusicFragment());
        List<String> titles = new ArrayList<>();
        titles.add(getResources().getString(R.string.local_music_single_song));
        titles.add(getResources().getString(R.string.local_music_singer));
        titles.add(getResources().getString(R.string.local_music_album));
        titles.add(getResources().getString(R.string.local_music_folder));
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager(),
                fragments, titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    //Toolbar上的菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.local_music_toolbar_menu, menu);
        return true;
    }
}
