package com.example.yuanping.freemusic.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yuanping.freemusic.R;

/**
 * Created by yuanping on 5/1/18.
 * 歌词显示
 */

public class MusicPlayLrcFragment extends Fragment {

    private static final MusicPlayLrcFragment lrcFragment = new MusicPlayLrcFragment();

    public static MusicPlayLrcFragment getInstance() {
        return lrcFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_play_lrc, container, false);
        return view;
    }
}
