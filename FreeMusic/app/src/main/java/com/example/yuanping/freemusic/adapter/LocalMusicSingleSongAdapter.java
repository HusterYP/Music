package com.example.yuanping.freemusic.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuanping.freemusic.App;
import com.example.yuanping.freemusic.R;
import com.example.yuanping.freemusic.activity.MusicPlay;
import com.example.yuanping.freemusic.bean.MusicBean;
import com.example.yuanping.freemusic.utils.MusicPlayInf;
import com.example.yuanping.freemusic.utils.MusicPlayUtils;
import com.example.yuanping.freemusic.utils.MusicUtils;

import java.util.List;

/**
 * Created by yuanping on 5/14/18.
 * 本地音乐-单曲
 * TODO: 如何根据当前播放歌曲,设置不同的Item的状态: MusicPlayUtils中持有的应该是当前播放Music的ID
 * TODO: 每次根据ID来判断和设置,所以MusicPlayUtils中的getCurPlayState()方法设计也有问题
 */
public class LocalMusicSingleSongAdapter extends RecyclerView.Adapter {

    private static final int TOP_ITEM = 0; // 最顶部的Item
    private static final int NORMAL_ITEM = 1; // 普通Item

    private Context mContext;
    private List<MusicBean> mMusicBeans;
    private OnItemClickListener mOnItemClickListener;

    public LocalMusicSingleSongAdapter(Context context) {
        mContext = context;
        mMusicBeans = MusicUtils.getLocalMusicList(context);
    }

    private MusicPlayInf mMusicPlayUtils = MusicPlayUtils.getInstance();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TOP_ITEM: {
                View view = LayoutInflater.from(mContext).inflate(R.layout
                        .item_local_music_single_song_top, parent, false);
                return new TopItemViewHolder(view);
            }
            case NORMAL_ITEM: {
                View view = LayoutInflater.from(mContext).inflate(R.layout
                        .item_local_music_single_song_normal, parent, false);
                return new NormalItemViewHolder(view);
            }
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TOP_ITEM: {
                setTopItem(holder, position);
                break;
            }
            case NORMAL_ITEM: {
                setNormalItem(holder, position);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TOP_ITEM : NORMAL_ITEM;
    }

    @Override
    public int getItemCount() {
        return mMusicBeans.size();
    }

    // 设置TopItem
    private void setTopItem(RecyclerView.ViewHolder holder, int positiom) {
        TopItemViewHolder topHolder = null;
        if (holder instanceof TopItemViewHolder) {
            topHolder = (TopItemViewHolder) holder;
        }
        if (topHolder != null) {
            topHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMusicPlayUtils.play();
                    Intent intent = new Intent(mContext, MusicPlay.class);
                    mContext.startActivity(intent);
                }
            });
            topHolder.mSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(App.sContext, String.valueOf("多选"), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // 设置NormalItem
    private void setNormalItem(final RecyclerView.ViewHolder holder, int position) {
        NormalItemViewHolder normalHolder = null;
        if (holder instanceof NormalItemViewHolder) {
            normalHolder = (NormalItemViewHolder) holder;
        }
        if (normalHolder != null) {
            MusicBean musicBean = mMusicBeans.get(position - 1);
            normalHolder.mSingerName.setText(musicBean.getSinger());
            normalHolder.mMusicName.setText(musicBean.getMusicName());
            normalHolder.mMusicSet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, String.valueOf("单曲设置"), Toast.LENGTH_SHORT).show();
                }
            });
            normalHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMusicPlayUtils.getCurMusicState() == MusicPlayUtils.STATE_PLAYING) {
                        Intent intent = new Intent(mContext, MusicPlay.class);
                        mContext.startActivity(intent);
                    } else if (mMusicPlayUtils.getCurMusicState() == MusicPlayUtils.STATE_STOP) {
                        mMusicPlayUtils.play();
                        ((NormalItemViewHolder) holder).mTrumpet.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    // 设置Item点击事件
    public interface OnItemClickListener {
        void onNormalItemClick();

        void onTopItemClick();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    class TopItemViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mSelect;

        public TopItemViewHolder(View itemView) {
            super(itemView);
            mSelect = itemView.findViewById(R.id.single_song_select);
        }
    }

    class NormalItemViewHolder extends RecyclerView.ViewHolder {

        ImageView mTrumpet;
        ImageView mMusicSet;
        TextView mMusicName;
        TextView mSingerName;

        public NormalItemViewHolder(View itemView) {
            super(itemView);
            mTrumpet = itemView.findViewById(R.id.single_song_trumpet);
            mMusicSet = itemView.findViewById(R.id.single_song_set);
            mMusicName = itemView.findViewById(R.id.single_song_song_name);
            mSingerName = itemView.findViewById(R.id.single_song_singer_name);
        }
    }
}
