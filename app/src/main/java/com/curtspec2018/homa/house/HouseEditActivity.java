package com.curtspec2018.homa.house;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HouseEditActivity extends AppCompatActivity {

    ActivityHouseEditBinding b;
    Intent intent;
    String type;

    int index;
    Building building;
    ArrayList<Building> buildings = new ArrayList<>();

    String imageUrl;
    String tag;

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
            Glide.with(this).load(building.getProfileUrl()).into(b.iv);
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

        if (name.equals("") || address.equals("") || numOfFloor.equals("")){
            Toast.makeText(this, "정보를 모두 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }else if(!address.contains("시")){
            Toast.makeText(this, "정확한 주소를 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }else if (Integer.parseInt(numOfFloor) == 0){
            Toast.makeText(this, "정확한 층수를 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }else if (imageUrl == null){
            Toast.makeText(this, "사진을 등록해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        building = new Building(imageUrl ,name, address, Integer.parseInt(numOfFloor), isElevator, isParking, isUnderGround);
        building.setTag(tag);

        if (type.equals("new")){
            if(G.getCurrentBuilding() == null) G.setCurrentBuilding(building);
            else {
                buildings.add(building);
                G.setBuildings(buildings);
            }
        }else {
            if (index >= 0){
                buildings.remove(index);
                buildings.add(index, building);
                G.setBuildings(buildings);
            }else if (index == -1)   G.setCurrentBuilding(building);
        }
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
                    ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("이미지 저장중...");
                    progressDialog.show();

                    Bundle extra = data.getExtras();
                    if (extra != null){
                        Bitmap bitmap = (Bitmap) extra.get("data");
                        Glide.with(this).load(bitmap).into(b.iv);
                    }else {
                        Uri uri = data.getData();
                        Glide.with(this).load(uri).into(b.iv);
                        String[] realPaths = getRealPathFromUri(uri).split("\\.");
                        String format = realPaths[realPaths.length - 1];

                        if (building == null || building.getTag() == null) tag = Calendar.getInstance().getTimeInMillis() + "";
                        else tag = building.getTag();

                        String fileName = tag + "profiles." + format;
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference rootRef = storage.getReference();
                        StorageReference targetRef = rootRef.child(G.getId() + "/buildingsProfiles/" + fileName);
                        UploadTask task = targetRef.putFile(uri);
                        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                targetRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imageUrl = uri.toString();
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        });
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getRealPathFromUri(Uri uri){
        String[] proj= {MediaStore.Images.Media.DATA};
        CursorLoader loader= new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor= loader.loadInBackground();
        int column_index= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result= cursor.getString(column_index);
        cursor.close();
        return  result;
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
                        if (index == -1){
                            G.setCurrentBuilding(null);
                            if (G.getBuildings().size() > 0){
                                G.setCurrentBuilding(G.getBuildings().get(0));
                                G.getBuildings().remove(0);
                            }
                        }else if (index >= 0){
                            G.getBuildings().remove(index);
                        }
                        dialog.dismiss();
                        setResult(RESULT_OK, intent);
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
