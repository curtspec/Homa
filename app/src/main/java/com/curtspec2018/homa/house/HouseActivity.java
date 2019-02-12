package com.curtspec2018.homa.house;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.curtspec2018.homa.R;
import com.curtspec2018.homa.adapter.HouseAdapter;
import com.curtspec2018.homa.databinding.ActivityHouseBinding;
import com.curtspec2018.homa.vo.HouseListItem;

import java.util.ArrayList;

public class HouseActivity extends AppCompatActivity {

    ActivityHouseBinding b;
    ArrayList<HouseListItem> items = new ArrayList<>();
    HouseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);
        b = DataBindingUtil.setContentView(this, R.layout.activity_house);
        b.setActivity(this);
        setSupportActionBar(b.toolbar);
        getSupportActionBar().setTitle(R.string.house_activity_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setItems();

        adapter = new HouseAdapter(items, this);
        b.listview.setAdapter(adapter);
        b.listview.setEmptyView(b.emptyView);
    }

    public void setItems(){
        //items.add(new HouseListItem("건물이름", "", "서울시 관악구 봉천동", "196-160번지"));
    }

    public void clickEnter(View v){
        Toast.makeText(this, "enter", Toast.LENGTH_SHORT).show();
    }
}
