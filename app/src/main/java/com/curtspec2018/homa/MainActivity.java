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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.curtspec2018.homa.SMS.SMSActivity;
import com.curtspec2018.homa.account.AccountActivity;
import com.curtspec2018.homa.adapter.MPagerAdapter;
import com.curtspec2018.homa.databinding.ActivityMainBinding;
import com.curtspec2018.homa.house.HouseActivity;
import com.curtspec2018.homa.memo.MemoActivity;
import com.curtspec2018.homa.tenant.TenantActivity;
import com.curtspec2018.homa.vo.Building;

public class MainActivity extends AppCompatActivity{

    ActivityMainBinding b;
    ActionBarDrawerToggle toggle;

    MPagerAdapter adapter;
    TextView tvName, tvAddress;

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

        ImageView iv = headerView.findViewById(R.id.btn_swap);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HouseActivity.class));
            }
        });
        tvName = headerView.findViewById(R.id.tv_name);
        tvAddress = headerView.findViewById(R.id.tv_address);

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
                    case R.id.menu_account:
                        startActivity(new Intent(MainActivity.this, AccountActivity.class));
                        break;
                    case R.id.menu_sms:
                        startActivity(new Intent(MainActivity.this, SMSActivity.class));
                        break;
                    case R.id.menu_memo:
                        startActivity(new Intent(MainActivity.this, MemoActivity.class));
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MFFragment mf = (MFFragment) adapter.getItem(1);
        mf.refreshView();
        Building currentBuilding = G.getCurrentBuilding();
        if (currentBuilding!= null){
            tvName.setText(currentBuilding.getName());
            tvAddress.setText(currentBuilding.getAddress());
        }
    }
}
