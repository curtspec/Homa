package com.curtspec2018.homa.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.curtspec2018.homa.MFFragment;
import com.curtspec2018.homa.MSFragment;
import com.curtspec2018.homa.MTFragment;

public class MPagerAdapter extends FragmentPagerAdapter {

    String[] titles = new String[]{"미처리", "입금현황", "입주통계"};

    public MPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment frag = null;
        switch (i){
            case 0:
                frag = new MSFragment();
                break;
            case 1:
                frag = new MFFragment();
                break;
            case 2:
                frag = new MTFragment();
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
