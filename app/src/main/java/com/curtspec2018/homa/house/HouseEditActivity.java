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
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.curtspec2018.homa.R;
import com.curtspec2018.homa.databinding.ActivityHouseEditBinding;

import java.net.URL;

public class HouseEditActivity extends AppCompatActivity {

    ActivityHouseEditBinding b;
    Intent intent;
    String type;

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

        intent = getIntent();
        type = intent.getStringExtra("type");
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

    }

    public void clickMap(View view) {
        //TODO : 현제의 주소를 기반으로 위치보여줌
    }

    public void clickOK(View view) {
        //TODO : intent 에 데이터 넣기
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
