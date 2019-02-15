package com.curtspec2018.homa.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.curtspec2018.homa.tenant.EmptyFragment;
import com.curtspec2018.homa.tenant.FloorFragment;

public class TenantPagerAdapter extends FragmentPagerAdapter {

    Fragment[] frags = new Fragment[2];
    String[] titles = new String[]{"층/호수별", "공실"};

    public TenantPagerAdapter(FragmentManager fm) {
        super(fm);
        frags[0] = new FloorFragment();
        frags[1] = new EmptyFragment();
    }

    @Override
    public Fragment getItem(int i) {
        return frags[i];
    }

    @Override
    public int getCount() {
        return frags.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
