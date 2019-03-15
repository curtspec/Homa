package com.curtspec2018.homa.house;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.curtspec2018.homa.G;
import com.curtspec2018.homa.R;
import com.curtspec2018.homa.adapter.HouseAdapter;
import com.curtspec2018.homa.databinding.ActivityHouseBinding;
import com.curtspec2018.homa.vo.Building;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

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
                    items = G.getBuildings();
                    adapter = new HouseAdapter(items, this);
                    b.listview.setAdapter(adapter);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Gson gson = new Gson();
        String crtBuildingJson = gson.toJson(currentBuilding);
        JsonArray buildingsJson = new JsonArray();
        for (Building b : items) buildingsJson.add(gson.toJson(b));

        String url = G.SERVER_URL + "saveBuildings.php";
        SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("error")) Toast.makeText(HouseActivity.this, "서버연결에 문제발생", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HouseActivity.this, "서버연결에 문제발생", Toast.LENGTH_SHORT).show();
            }
        });
        request.addStringParam("id", G.getId());
        request.addStringParam("current", crtBuildingJson);
        request.addStringParam("buildings", buildingsJson.toString());

        Volley.newRequestQueue(this).add(request);
    }
}
