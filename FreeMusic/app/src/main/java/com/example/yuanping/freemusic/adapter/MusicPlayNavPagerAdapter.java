package com.example.yuanping.freemusic.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yuanping.freemusic.R;
import com.example.yuanping.freemusic.activity.MusicPlay;

/**
 * Created by yuanping on 4/28/18.
 * 主界面底部播放导航栏Adapter
 */

public class MusicPlayNavPagerAdapter extends PagerAdapter {
    private Context context;
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (context != null) {
                Intent intent = new Intent(context, MusicPlay.class);
                context.startActivity(intent);
            }
        }
    };

    public MusicPlayNavPagerAdapter(Context context) {
        this.context = context;
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
        View view = View.inflate(context, R.layout.common_bottom_music_pager_item,
                null);
        view.setOnClickListener(listener);
        ImageView avatar = view.findViewById(R.id.music_play_nav_avatar);
        TextView title = view.findViewById(R.id.music_play_nav_title);
        TextView name = view.findViewById(R.id.music_play_nav_name);
        title.setText("This is Title");
        name.setText("This is Name");
        //记住添加到container,否则显示不出来
        container.addView(view);
        return view;
    }
}
