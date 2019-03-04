package com.curtspec2018.homa.house;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.curtspec2018.homa.G;
import com.curtspec2018.homa.R;
import com.curtspec2018.homa.adapter.HouseAdapter;
import com.curtspec2018.homa.databinding.ActivityHouseBinding;
import com.curtspec2018.homa.vo.Building;
import com.curtspec2018.homa.vo.HouseListItem;

import java.util.ArrayList;

public class HouseActivity extends AppCompatActivity {

    ActivityHouseBinding b;
    HouseAdapter adapter;

    ArrayList<Building> items = new ArrayList<>();
    Building currentBuilding;
    final int HOUSE_EDIT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);
        b = DataBindingUtil.setContentView(this, R.layout.activity_house);
        b.setActivity(this);
        setSupportActionBar(b.toolbar);
        getSupportActionBar().setTitle(R.string.house_activity_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        items = G.getBuildings();
        currentBuilding = G.getCurrentBuilding();

        adapter = new HouseAdapter(items, this);
        b.listview.setAdapter(adapter);
        b.listview.setEmptyView(b.emptyView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentBuilding != null){
            b.tvCurrentName.setText(currentBuilding.getName());
            b.tvCurrentAdd.setText(currentBuilding.getAddress());
            Glide.with(this).load(currentBuilding.getProfileUrl()).into(b.circleIv);
        }else {
            b.tvCurrentName.setText("건물 없음");
            b.tvCurrentAdd.setText("등록된 건물이 없습니다");
            Glide.with(this).load(R.drawable.ic_request_image).into(b.circleIv);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (currentBuilding != null) G.setCurrentBuilding(currentBuilding);
        G.setBuildings(items);
    }

    public void clickEnter(View v){
        if (currentBuilding == null) return;
        Intent intent = new Intent(this, HouseEditActivity.class);
        intent.putExtra("type", "edit");
        intent.putExtra("index", -1);
        startActivityForResult(intent, HOUSE_EDIT);
    }

    public void clickFab(View view) {
        Intent intent = new Intent(this, HouseEditActivity.class);
        intent.putExtra("type", "new");
        startActivityForResult(intent, HOUSE_EDIT);
    }

    public void editBuilding(int position){
        Intent intent = new Intent(this, HouseEditActivity.class);
        intent.putExtra("type", "edit");
        intent.putExtra("index", position);
        startActivityForResult(intent, HOUSE_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case HOUSE_EDIT:
                if (resultCode == RESULT_OK){
                    String type = data.getStringExtra("type");
                    int index = data.getIntExtra("index", -2);
                    String name = data.getStringExtra("name");
                    String address = data.getStringExtra("address");
                    int numOfFloor = data.getIntExtra("numOfFloor", 0);
                    boolean isElevator = data.getBooleanExtra("isElevator", false);
                    boolean isParking = data.getBooleanExtra("isParking", false);
                    boolean isUnderGround = data.getBooleanExtra("isUnderGround", false);

                    Building building = null;
                    if (type.equals("new")){
                        building = new Building(name, address, numOfFloor, isElevator, isParking, isUnderGround);
                        if (currentBuilding == null) currentBuilding = building;
                        else items.add(building);
                    }else if (type.equals("edit")){
                        if (index >= 0)   building = items.get(index);
                        else if (index == -1) building = currentBuilding;
                        if (building != null) {
                            building.setName(name);
                            building.setAddress(address);
                            building.setNumOfFloor(numOfFloor);
                            building.setElevator(isElevator);
                            building.setParking(isParking);
                            building.setUnderGround(isUnderGround);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                if (resultCode == HouseEditActivity.RESULT_DELETE){
                    int index = data.getIntExtra("index", -2);
                    if (index >= 0) items.remove(index);
                    if (index == -1) currentBuilding = null;
                    adapter.notifyDataSetChanged();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
