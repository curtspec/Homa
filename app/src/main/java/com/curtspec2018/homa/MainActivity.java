package com.curtspec2018.homa;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.curtspec2018.homa.adapter.MPagerAdapter;
import com.curtspec2018.homa.databinding.ActivityMainBinding;
import com.curtspec2018.homa.house.HouseActivity;
import com.curtspec2018.homa.tenant.TenantActivity;

public class MainActivity extends AppCompatActivity{

    ActivityMainBinding b;
    ActionBarDrawerToggle toggle;

    MPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b = DataBindingUtil.setContentView(this, R.layout.activity_main);
        b.setActivity(this);

        setSupportActionBar(b.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toggle = new ActionBarDrawerToggle(this, b.drawer, b.toolbar, R.string.app_name, R.string.app_name);
        toggle.syncState();
        b.drawer.addDrawerListener(toggle);

        adapter = new MPagerAdapter(getSupportFragmentManager());
        b.viewPager.setAdapter(adapter);
        b.tab.setupWithViewPager(b.viewPager);
        b.viewPager.setCurrentItem(1);

        LayoutInflater inflater = getLayoutInflater();
        View headerView = inflater.inflate(R.layout.header_main_navi, b.navigation, false);
        b.navigation.addHeaderView(headerView);

        b.navigation.bringToFront();
        b.navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_house:
                        startActivity(new Intent(MainActivity.this, HouseActivity.class));
                        break;
                    case R.id.menu_tenant :
                        startActivity(new Intent(MainActivity.this, TenantActivity.class));
                        break;
                }
                return false;
            }
        });
    }



}
