package com.example.yuanping.freemusic.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.yuanping.freemusic.R;
import com.example.yuanping.freemusic.adapter.MainViewPagerAdapter;
import com.example.yuanping.freemusic.adapter.MusicPlayNavPagerAdapter;
import com.example.yuanping.freemusic.event.MusicStartPlay;
import com.example.yuanping.freemusic.event.MusicStopPlay;
import com.example.yuanping.freemusic.fragment.MusicFragment;
import com.example.yuanping.freemusic.service.MusicPlayService;
import com.example.yuanping.freemusic.utils.BitmapManager;
import com.example.yuanping.freemusic.utils.ChangeTheme;
import com.example.yuanping.freemusic.utils.MusicPlayInf;
import com.example.yuanping.freemusic.utils.MusicPlayUtils;
import com.example.yuanping.freemusic.utils.MusicUtils;
import com.example.yuanping.freemusic.view.CircleImage;
import com.example.yuanping.freemusic.view.PlayProgressBarCircle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity implements NavigationView
        .OnNavigationItemSelectedListener, Toolbar.OnMenuItemClickListener, View.OnClickListener {

    public static final String MUSIC_ID = "Music_ID"; // 传递音乐ID
    private static final int DEFAULT_AVATAR_WIDTH = 200; //头像默认宽高
    private static final int DEFAULT_AVATAR_HEIGHT = 200;
    private static final int SELECT_FROM_PIC = 0;
    private static final int SELECT_FROM_CAP = 1;
    private static final String AVATAR = "avatar.png";
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.action_bar_music)
    ImageView menuMusic;
    @BindView(R.id.action_bar_video)
    ImageView menuVideo;
    @BindView(R.id.action_bar_person)
    ImageView menuPerson;
    @BindView(R.id.main_view_pager)
    ViewPager mainViewPager;
    @BindView(R.id.common_music_nav_play)
    PlayProgressBarCircle musicPlayNavProgressBar;
    @BindView(R.id.common_music_nav_pager)
    ViewPager musicPlayNavPager;
    @BindView(R.id.common_music_nav_root)
    RelativeLayout musicPlayNavRoot;

    CircleImage circleImageView;
    Button selectFromPic;
    Button selectFromCap;
    ActionBar actionbar;
    List<ImageView> menuList = new ArrayList<>();
    Handler handler = null;
    MusicPlayInf mMusicPlayInf = MusicPlayUtils.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        setBarMenuClick();
        setTab();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionbar.setTitle("");
        }
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.setOnMenuItemClickListener(this);
        View view = navigationView.getHeaderView(0);
        circleImageView = view.findViewById(R.id.avatar);
        circleImageView.setOnClickListener(this);
        Bitmap bitmap = BitmapManager.getBitmap(getFilesDir() + AVATAR);
        if (bitmap != null) {
            circleImageView.setImageBitmap(bitmap);
        }
        musicPlayNavRoot.setOnClickListener(this);
        onPlayProgress();
        onMusicPlayNav();
    }

    //底部播放进度条
    private void onPlayProgress() {
        if (handler == null) {
            handler = new ProgressHandler(musicPlayNavProgressBar);
        }
        musicPlayNavProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayNavProgressBar.changeState();
                handler.sendEmptyMessage(0);
                //TODO bug : 多次点击时会崩溃
               /* if (musicPlayNavProgressBar.isPlaying()) {
                    EventBus.getDefault().post(new MusicStartPlay());
                } else {
                    EventBus.getDefault().post(new MusicStopPlay());
                }*/
            }
        });
    }

    //底部播放导航处的滑动ViewPager
    //TODO  addPagerChangeListener
    private void onMusicPlayNav() {
        PagerAdapter adapter = new MusicPlayNavPagerAdapter(this);
        musicPlayNavPager.setAdapter(adapter);
        musicPlayNavPager.setCurrentItem(Integer.MAX_VALUE / 2);
    }

    //设置ActionBar上的三个Menu菜单点击事件
    private void setBarMenuClick() {
        menuList.add(menuMusic);
        menuList.add(menuVideo);
        menuList.add(menuPerson);
        menuMusic.setSelected(true);
        menuMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barMenuSelectUtil(0);
                mainViewPager.setCurrentItem(0);
            }
        });
        menuVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barMenuSelectUtil(1);
                mainViewPager.setCurrentItem(1);
            }
        });
        menuPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barMenuSelectUtil(2);
                mainViewPager.setCurrentItem(2);
            }
        });
    }

    private void barMenuSelectUtil(int position) {
        for (int i = 0; i < menuList.size(); i++) {
            if (position == i) {
                menuList.get(position).setSelected(true);
            } else {
                menuList.get(i).setSelected(false);
            }
        }
    }

    private void setTab() {
        MusicFragment music = new MusicFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(music);
        fragments.add(new MusicFragment());
        fragments.add(new MusicFragment());
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager(),
                fragments, null);
        mainViewPager.setAdapter(adapter);
        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                barMenuSelectUtil(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    //设置左上角Menu导航按钮点击时弹出菜单
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //监听单击返回事件,若侧边菜单可见,则关闭
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && drawerLayout
                .isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //Toolbar上的菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        return true;
    }

    //侧滑菜单项点击
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

        }
        return false;
    }

    //Toolbar上menu的点击事件
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_search: {
                Toast.makeText(getApplication(), String.valueOf("搜索.."), Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    //夜间模式和日间模式的转换
    @OnClick(R.id.bt_change_state)
    public void changeState(View view) {
        String text = ((Button) view).getText().toString();
        if (text.equals(getResources().getString(R.string.day_state))) {
            ChangeTheme.setState(ChangeTheme.DAY_THEME, this);
        } else if (text.equals(getResources().getString(R.string.night_state))) {
            ChangeTheme.setState(ChangeTheme.NIGHT_THEME, this);
        }
        recreate();
    }

    //底部设置按钮点击事件
    @OnClick(R.id.setting)
    public void setting(View view) {
        Intent intent = new Intent(this, Setting.class);
        startActivity(intent);
    }

    /**
     * 头像的点击事件
     * 从相机拍照
     * 从相册选择头像
     * 底部播放导航
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avatar: {
                setAvatar();
                return;
            }
            case R.id.select_from_cap: {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, SELECT_FROM_CAP);
                return;
            }
            case R.id.select_from_pic: {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_FROM_PIC);
                return;
            }
            case R.id.common_music_nav_root: {
                Intent intent = new Intent(this, MusicPlay.class);
                startActivity(intent);
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, MusicPlayService.class);
        stopService(intent);
        EventBus.getDefault().unregister(this);
    }

    //设置头像
    private void setAvatar() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.dialog_select_pic);
        dialog.show();
        selectFromCap = dialog.findViewById(R.id.select_from_cap);
        selectFromPic = dialog.findViewById(R.id.select_from_pic);
        selectFromCap.setOnClickListener(this);
        selectFromPic.setOnClickListener(this);
    }

    // TODO Music ID
    @Subscribe
    public void onMusicStartPlay(MusicStartPlay startPlay) {
        Intent intent = new Intent(MainActivity.this, MusicPlayService.class);
        intent.putExtra(MUSIC_ID, MusicUtils.getLastMusicID());
        startService(intent);
    }

    @Subscribe
    public void onMusicStopPlay(MusicStopPlay stopPlay) {
        mMusicPlayInf.stop();
    }

    //接受设置头像界面的返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_FROM_CAP: {
                if (resultCode == RESULT_OK) {
                    try {
                        Bundle bundle = data.getExtras();
                        Bitmap b = (Bitmap) bundle.get("data");
                        Bitmap bitmap = BitmapManager.decodeSampledBitmapFromBitmap(b,
                                DEFAULT_AVATAR_WIDTH, DEFAULT_AVATAR_HEIGHT);
                        circleImageView.setImageBitmap(bitmap);
//                        saveAvatar(bitmap);
                        BitmapManager.saveBitmap(getFilesDir() + AVATAR, bitmap, 0.2f);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
            case SELECT_FROM_PIC: {
                if (resultCode == RESULT_OK) {
                    try {
                        Uri uri = data.getData();
                        Bitmap b = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        Bitmap bitmap = BitmapManager.decodeSampledBitmapFromBitmap(b,
                                DEFAULT_AVATAR_WIDTH, DEFAULT_AVATAR_HEIGHT);
                        circleImageView.setImageBitmap(bitmap);
//                        saveAvatar(bitmap);
                        BitmapManager.saveBitmap(getFilesDir() + AVATAR, bitmap, 0.2f);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
        }
    }

    @Subscribe
    public void onStartPlayMusic() {
        handler.sendEmptyMessage(0);
    }

    private static class ProgressHandler extends Handler {
        private PlayProgressBarCircle progressBar;

        ProgressHandler(PlayProgressBarCircle progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        public void handleMessage(Message msg) {
            if (progressBar.isPlaying()) {
                progressBar.setProgress(progressBar.getProgress() + 1);
                if (progressBar.getProgress() <= 100) {
                    sendEmptyMessageDelayed(0, 100);
                }
            }
            super.handleMessage(msg);
        }
    }
}
