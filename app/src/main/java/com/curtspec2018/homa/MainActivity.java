package com.curtspec2018.homa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.curtspec2018.homa.vo.Schedule;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    ActivityMainBinding b;
    ActionBarDrawerToggle toggle;

    MPagerAdapter adapter;
    TextView tvName, tvAddress;

    private final int REQUEST_PERNISSION = 22;

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

        b.navigation.bringToFront();
        View headerView = LayoutInflater.from(this).inflate(R.layout.header_main_navi, null);

        MapFragment map = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);

        ImageView iv = headerView.findViewById(R.id.btn_swap);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HouseActivity.class));
            }
        });
        tvName = headerView.findViewById(R.id.tv_name);
        tvAddress = headerView.findViewById(R.id.tv_address);

        View building = headerView.findViewById(R.id.menu_house);
        View tenant = headerView.findViewById(R.id.menu_tenant);
        View account = headerView.findViewById(R.id.menu_account);
        View sms = headerView.findViewById(R.id.menu_sms);
        View memo = headerView.findViewById(R.id.menu_memo);

        building.setOnClickListener(this);
        tenant.setOnClickListener(this);
        account.setOnClickListener(this);
        sms.setOnClickListener(this);
        memo.setOnClickListener(this);

        b.navigation.addHeaderView(headerView);

        //=================================================== 동적 퍼미션 ========================================================
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERNISSION);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_PERNISSION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "일부 기능의 사용에 지장이 있을 수 있습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MFFragment mf = (MFFragment) adapter.getItem(1);
        mf.refreshView();
        MTFragment mt = (MTFragment) adapter.getItem(2);
        mt.refreshView();
        Building currentBuilding = G.getCurrentBuilding();
        if (currentBuilding!= null){
            tvName.setText(currentBuilding.getName());
            tvAddress.setText(currentBuilding.getAddress());
        }
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        naverMap.setMapType(NaverMap.MapType.Basic);
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true);
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, false);
        naverMap.setBuildingHeight(0.6f);

        Building currentBuilding = G.getCurrentBuilding();

        double lati = 37.566303, logi = 126.977934;
        String name = "";
        if (currentBuilding != null){
            String address = currentBuilding.getAddress();
            name = currentBuilding.getName();
            Geocoder coder = new Geocoder(this, Locale.KOREA);
            try {
                List<Address> result = coder.getFromLocationName(address, 1);
                lati = result.get(0).getLatitude();
                logi = result.get(0).getLongitude();
            } catch (IOException e) {
                Toast.makeText(this, "주소검색 결과 없음", Toast.LENGTH_SHORT).show();
            }
        }

        LatLng pos = new LatLng(lati, logi);
        naverMap.setCameraPosition(new CameraPosition(pos, 15, 30, 0));

        Marker marker = new Marker(pos);
        marker.setCaptionText(name);
        marker.setMap(naverMap);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
    }
}
