package com.curtspec2018.homa.house;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

        items.addAll(G.getBuildings());

        adapter = new HouseAdapter(items, this);
        b.listview.setAdapter(adapter);
        b.listview.setEmptyView(b.emptyView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        currentBuilding = G.getCurrentBuilding();
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

    public void exchangeCurrent(int position){
        Building tmp = currentBuilding;
        currentBuilding = items.get(position);
        b.tvCurrentName.setText(currentBuilding.getName());
        b.tvCurrentAdd.setText(currentBuilding.getAddress());
        Glide.with(this).load(currentBuilding.getProfileUrl()).into(b.circleIv);

        items.remove(position);
        items.add(position, tmp);
        adapter.notifyDataSetChanged();

        G.setCurrentBuilding(currentBuilding);
        G.setBuildings(items);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case HOUSE_EDIT:
                if (resultCode == RESULT_OK){
                    currentBuilding = G.getCurrentBuilding();
                    if (currentBuilding != null){

                        b.tvCurrentName.setText(currentBuilding.getName());
                        b.tvCurrentAdd.setText(currentBuilding.getAddress());
                        Glide.with(this).load(currentBuilding.getProfileUrl()).into(b.circleIv);
                    }else {
                        b.tvCurrentName.setText("건물 없음");
                        b.tvCurrentAdd.setText("등록된 건물이 없습니다");
                        Glide.with(this).load(R.drawable.ic_request_image).into(b.circleIv);
                    }
                    Log.i("ErrorTrace", "house에서 받고 G. = "+ G.getBuildings().size()+"");
                    items = G.getBuildings();
                    adapter = new HouseAdapter(items, this);
                    b.listview.setAdapter(adapter);

                    Log.i("ErrorTrace", "house에서 받고 G. = "+ G.getBuildings().size()+"");
                    Log.i("ErrorTrace", "house에서 받고 items = "+ items.size()+"");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
