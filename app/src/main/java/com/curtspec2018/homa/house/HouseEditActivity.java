package com.curtspec2018.homa.house;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.curtspec2018.homa.G;
import com.curtspec2018.homa.R;
import com.curtspec2018.homa.databinding.ActivityHouseEditBinding;
import com.curtspec2018.homa.vo.Building;

import java.net.URL;
import java.util.ArrayList;

public class HouseEditActivity extends AppCompatActivity {

    ActivityHouseEditBinding b;
    Intent intent;
    String type;

    int index;
    Building building;
    ArrayList<Building> buildings = new ArrayList<>();

    static final int RESULT_DELETE = 4444;
    final int REQUEST_PICK = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_edit);
        b = DataBindingUtil.setContentView(this, R.layout.activity_house_edit);
        setSupportActionBar(b.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.house_edit_activity_title);

        b.rbElevatorNot.setChecked(true);
        b.rgParking.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_parking_get:
                        b.parkingLocal.setAlpha(1);
                        b.rbParkingLocaOver.setEnabled(true);
                        b.rbParkingLocaUnder.setEnabled(true);
                        break;
                    case R.id.rb_parking_not:
                        b.parkingLocal.setAlpha(0.5f);
                        b.rbParkingLocaOver.setEnabled(false);
                        b.rbParkingLocaOver.setChecked(false);
                        b.rbParkingLocaUnder.setEnabled(false);
                        b.rbParkingLocaUnder.setChecked(false);
                        break;
                }
            }
        });
        b.rbParkingNot.setChecked(true);

        b.editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                b.tvName.setText(s.toString());
            }
        });
        b.editAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {     }
            @Override
            public void afterTextChanged(Editable s) {
                b.tvAddress.setText(s.toString());
            }
        });

        b.fab.setFocusableInTouchMode(true);
        b.fab.requestFocus();

        intent = getIntent();
        type = intent.getStringExtra("type");
        index = intent.getIntExtra("index", -2);
        buildings = G.getBuildings();

        if (index >= 0)                           building = buildings.get(index);
        if (type.equals("edit") && index == -1)   building = G.getCurrentBuilding();

        if (building != null){
            b.editName.setText(building.getName());
            b.editAddress.setText(building.getAddress());
            b.editFloor.setText(building.getNumOfFloor()+"");
            if (building.isElevator()) b.rbElevatorGet.setChecked(true);
            if (building.isParking()) b.rbParkingGet.setChecked(true);
            if (building.isUnderGround()) b.rbParkingLocaUnder.setChecked(true);
        }

    }

    public void clickMap(View view) {
        //TODO : 현제의 주소를 기반으로 위치보여줌
        if (building == null) return;
    }

    public void clickOK(View view) {
        String name = b.editName.getText().toString();
        String address = b.editAddress.getText().toString();
        String numOfFloor = b.editFloor.getText().toString();
        boolean isElevator = b.rbElevatorGet.isChecked();
        boolean isParking = b.rbParkingGet.isChecked();
        boolean isUnderGround = false;
        if (isParking) isUnderGround = b.rbParkingLocaUnder.isChecked();

        Log.i("ErrorTrace", "type : " + type);

        if (name.equals("") || address.equals("") || numOfFloor.equals("")){
            Toast.makeText(this, "정보를 모두 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }else if(!address.contains("시")){
            Toast.makeText(this, "정확한 주소를 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }else if (Integer.parseInt(numOfFloor) == 0){
            Toast.makeText(this, "정확한 층수를 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }
//        intent.putExtra("type", type);
//        intent.putExtra("index", index);
//        intent.putExtra("name" , name);
//        intent.putExtra("address" , address);
//        intent.putExtra("numOfFloor", Integer.parseInt(numOfFloor));
//        intent.putExtra("isElevator", isElevator);
//        intent.putExtra("isParking", isParking);
//        intent.putExtra("isUnderGround", isUnderGround);
//
        building = new Building(name, address, Integer.parseInt(numOfFloor), isElevator, isParking, isUnderGround);

        Log.i("ErrorTrace", "Edit에서 buildings size : " + buildings.size());
        if (type.equals("new")){
            if(G.getCurrentBuilding() == null) G.setCurrentBuilding(building);
            else {
                buildings.add(building);
                G.setBuildings(buildings);
                Log.i("ErrorTrace", "edit 추가 후"+buildings.size()+"");
            }
        }else {
            if (index >= 0){
                buildings.remove(index);
                buildings.add(index, building);
                G.setBuildings(buildings);
            }else if (index == -1)   G.setCurrentBuilding(building);
        }

        Log.i("ErrorTrace", "edit 추가 후"+buildings.size()+"");
        Log.i("ErrorTrace", "edit 추가 후"+G.getBuildings().size()+"");

        setResult(RESULT_OK, intent);
        finish();
    }

    public void clickPick(View view) {
        Intent intentPick = new Intent(Intent.ACTION_PICK);
        intentPick.setType("image/*");
        startActivityForResult(intentPick, REQUEST_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_PICK :
                if (resultCode == RESULT_OK) {
                    Bundle extra = data.getExtras();
                    if (extra != null){
                        Bitmap bitmap = (Bitmap) extra.get("data");
                        Glide.with(this).load(bitmap).into(b.iv);
                    }else {
                        Uri uri = data.getData();
                        Glide.with(this).load(uri).into(b.iv);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (type.equals("new")) return true;
        getMenuInflater().inflate(R.menu.house_edit, menu);
        Drawable drawable = menu.getItem(0).getIcon();
        if (drawable != null){
            drawable.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_delete :
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("정말로 삭제하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent.putExtra("index", index);
                        setResult(RESULT_DELETE, intent);
                        finish();
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
