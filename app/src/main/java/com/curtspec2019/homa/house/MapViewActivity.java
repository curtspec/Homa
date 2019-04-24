package com.curtspec2019.homa.house;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.curtspec2019.homa.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    Intent intent;
    MapFragment map;

    double lat, lng;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        map = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view);
        map.getMapAsync(this);

        intent = getIntent();
        lat = intent.getDoubleExtra("lati", 0);
        lng = intent.getDoubleExtra("logi", 0);
        address = intent.getStringExtra("address");

        TextView tvAddress = findViewById(R.id.tv_address);
        tvAddress.setText(address);
    }

    public void clickBack(View view) {
        finish();
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        naverMap.setMapType(NaverMap.MapType.Basic);
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true);
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, false);
        naverMap.setBuildingHeight(0.6f);

        LatLng pos = new LatLng(lat, lng);
        naverMap.setCameraPosition(new CameraPosition(pos, 15, 30, 0));

        Marker marker = new Marker(pos);
        marker.setMap(naverMap);
    }
}
