package com.curtspec2018.homa.account;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.curtspec2018.homa.R;
import com.curtspec2018.homa.adapter.AccountPagerAdapter;
import com.curtspec2018.homa.databinding.ActivityAccountBinding;

public class AccountActivity extends AppCompatActivity {

    ActivityAccountBinding b;
    AccountPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        b = DataBindingUtil.setContentView(this, R.layout.activity_account);
        setSupportActionBar(b.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.account_activity_title);

        adapter = new AccountPagerAdapter(getSupportFragmentManager());
        b.viewPager.setAdapter(adapter);
        b.tab.setupWithViewPager(b.viewPager);
        b.viewPager.setCurrentItem(1);
    }
}
