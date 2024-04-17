package com.example.neomusicplayer;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.HashMap;

public class pagerController extends FragmentPagerAdapter {
    int tabcount;

    public pagerController(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                //return new QueueDialog();
                return new songs();
            case 1:
                return new artist();
            case 2:
                return new folder();
            case 3:
                return new playlists();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
