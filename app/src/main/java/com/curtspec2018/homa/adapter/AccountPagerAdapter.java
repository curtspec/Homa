package com.curtspec2018.homa.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.curtspec2018.homa.account.CurrentFragment;
import com.curtspec2018.homa.account.EntireFragment;
import com.curtspec2018.homa.account.MonthlyFragment;

public class AccountPagerAdapter extends FragmentPagerAdapter {

    Fragment[] frags = new Fragment[3];
    String[] titles = new String[]{"월별", "이번달", "전체기간"};

    public AccountPagerAdapter(FragmentManager fm) {
        super(fm);
        frags[0] = new MonthlyFragment();
        frags[1] = new CurrentFragment();
        frags[2] = new EntireFragment();
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
