package com.curtspec2018.homa.tenant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.NumberPicker;

import com.bumptech.glide.Glide;
import com.curtspec2018.homa.R;
import com.curtspec2018.homa.databinding.ActivityTenantEditBinding;
import com.curtspec2018.homa.vo.Room;
import com.curtspec2018.homa.vo.Tenant;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TenantEditActivity extends AppCompatActivity {

    ActivityTenantEditBinding b;
    Intent intent;

    int currentVal;
    final static int PICK_PHOTO = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_edit);
        b = DataBindingUtil.setContentView(this, R.layout.activity_tenant_edit);

        setSupportActionBar(b.toolbar);
        getSupportActionBar().setTitle(R.string.tenant_edit_activity_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();
        initViewsValue();
        b.toolbar.setFocusableInTouchMode(true);
        b.toolbar.requestFocus();

        //register 창 visible 설정.
        b.switchRegister.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                b.registerTenant.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PICK_PHOTO:
                if (resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    if (bundle != null){
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        Glide.with(this).load(bitmap).into(b.iv);
                    }else {
                        Uri uri = data.getData();
                        Glide.with(this).load(uri).into(b.iv);
                    }
                }
                break;
        }
    }

    private void initViewsValue() {
        Room room = (Room) intent.getSerializableExtra("room");
        if (room != null){
            b.editFloor.setText(room.getFloor()+"");
            b.editName.setText(room.getName());
            b.editNick.setText(room.getNickname());
            Tenant t = room.getTenants();
            if (room.isOccupied() && t != null){
                b.editMonthly.setText(t.getRent()+"");
                b.editMaintenance.setText(t.getMaintenanceFee() + "");
                Calendar contractDay = t.getContractDay();
                b.editContract.setText(new SimpleDateFormat("yyyy.MM.dd").format(new Date(contractDay.getTimeInMillis())));
                b.editPeriod.setText(t.getPeriod()+"");
                Calendar contractOver = t.getContractOver();
                b.editContractOver.setText(new SimpleDateFormat("yyyy.MM.dd").format(new Date(contractOver.getTimeInMillis())));
                b.editDeposit.setText(t.getDeposit()+"");
                b.editPayday.setText(t.getPayday()+"");
                b.editArrear.setText(t.getArrear()+"");
            }
        }
    }

    public void clickContract(View view) {

    }

    public void clickFloorBtn(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("층수 선택");
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_numer_picker, null);
        NumberPicker picker = v.findViewById(R.id.number_picker);
        picker.setMinValue(1);
        //TODO : 건물의 층수로 max limit 걸기
        picker.setMaxValue(100);
        picker.setValue(1);
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
               currentVal = newVal;
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                b.editFloor.setText(currentVal+"");
                dialog.dismiss();
            }
        });
        builder.setView(v);
        builder.show();
    }

    public void clickPayday(View view) {

    }

    public void clickPick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO);
    }

    public void clickOK(View view) {
        setResult(RESULT_OK);
        finish();
    }
}
