package com.curtspec2018.homa.house;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.curtspec2018.homa.G;
import com.curtspec2018.homa.R;
import com.curtspec2018.homa.adapter.HouseAdapter;
import com.curtspec2018.homa.databinding.ActivityHouseBinding;
import com.curtspec2018.homa.vo.Building;
import com.curtspec2018.homa.vo.HouseListItem;

import java.util.ArrayList;

public class HouseActivity extends AppCompatActivity {

    ActivityHouseBinding b;
    ArrayList<Building> items = new ArrayList<>();
    HouseAdapter adapter;

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

        adapter = new HouseAdapter(items, this);
        b.listview.setAdapter(adapter);
        b.listview.setEmptyView(b.emptyView);
    }


    public void clickEnter(View v){
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
                    //TODO : 전달받은 값으로 주소리스트의 값변경
                }
                if (resultCode == HouseEditActivity.RESULT_DELETE){
                    Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
