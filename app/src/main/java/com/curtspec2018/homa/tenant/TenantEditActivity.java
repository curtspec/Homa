package com.curtspec2018.homa.tenant;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Toast;

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

    Room room;
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
        b.editPeriod.setText(1+"");
        b.editPeriod.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //if (count == 0) b.editPeriod.setText("0");
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) b.editPeriod.setText("0");
                else{
                    int period = Integer.parseInt(s.toString());
                    String tmp = b.editContract.getText().toString();
                    if (tmp != null && tmp.length() != 0){
                        String[] times = tmp.split("\\.");
                        b.editContractOver.setText((Integer.parseInt(times[0]) + period) +"."+times[1]+"."+times[2]);
                    }
                }
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
        room = (Room) intent.getSerializableExtra("room");
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
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar selectedDay = Calendar.getInstance();
                selectedDay.set(year, month, dayOfMonth);
                b.editContract.setText(new SimpleDateFormat("yyyy.MM.dd").format(new Date(selectedDay.getTimeInMillis())));
                Calendar contractOver = selectedDay;
                int period = Integer.parseInt(b.editPeriod.getText().toString());
                contractOver.set(selectedDay.get(Calendar.YEAR) + period, selectedDay.get(Calendar.MONTH), selectedDay.get(Calendar.DAY_OF_MONTH));
                b.editContractOver.setText(new SimpleDateFormat("yyyy.MM.dd").format(new Date(contractOver.getTimeInMillis())));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void clickFloorBtn(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("층수 선택");
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_numer_picker, null);
        final NumberPicker picker = v.findViewById(R.id.number_picker);
        picker.setMinValue(1);
        //TODO : 건물의 층수로 max limit 걸기
        picker.setMaxValue(100);
        picker.setValue(1);
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                b.editFloor.setText(picker.getValue()+"");
                dialog.dismiss();
            }
        });
        builder.setView(v);
        builder.show().setCanceledOnTouchOutside(false);
    }

    public void clickPayday(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("날자 선택");
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_numer_picker, null);
        final NumberPicker picker = v.findViewById(R.id.number_picker);
        picker.setMinValue(1);
        picker.setMaxValue(28);
        picker.setValue(15);
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                b.editPayday.setText(picker.getValue()+"");
                dialog.dismiss();
            }
        });
        builder.setView(v);
        builder.show().setCanceledOnTouchOutside(false);
    }

    public void clickPick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO);
    }

    public void clickOK(View view) {
        String floor = b.editFloor.getText().toString();
        String name = b.editName.getText().toString();
        String nick = b.editNick.getText().toString();
        boolean isUnderGround = b.switchUnder.isChecked();
        boolean isOccupied = b.switchRegister.isChecked();

        if (floor.equals("") || name.equals("") || nick.equals("")) {
            new AlertDialog.Builder(this).setMessage("필수요소를 모두 입력하세요.").show();
            return;
        }
        room = new Room(name, nick, Integer.parseInt(floor), isOccupied, isUnderGround);
        if (isOccupied){
            String rent = b.editMonthly.getText().toString().trim();
            String maintenance = b.editMaintenance.getText().toString().trim();
            String contractDay = b.editContract.getText().toString();
            String contractOver = b.editContractOver.getText().toString();
            String period = b.editPeriod.getText().toString().trim();
            String deposit = b.editDeposit.getText().toString().trim();
            String payday = b.editPayday.getText().toString().trim();
            String arrear = b.editArrear.getText().toString().trim();

            if (rent.equals("") || maintenance.equals("") || contractDay.equals("") ||
              deposit.equals("") || payday.equals("") || arrear.equals("")){
                new AlertDialog.Builder(this).setMessage("필수요소를 모두 입력하세요").show();
                return;
            }else if(Integer.parseInt(period) == 0){
                new AlertDialog.Builder(this).setMessage("계약기간은 1년이상이어야합니다").show();
                return;
            }else {
                String[] date = contractDay.split("\\.");
                Calendar contract = Calendar.getInstance();
                contract.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]));
                String tenantName = b.editTenantName.getText().toString().trim();
                String tenantNumber = b.editTenantNumber.getText().toString().trim();
                Tenant t = new Tenant(Integer.parseInt(rent), Integer.parseInt(maintenance), Integer.parseInt(deposit), Integer.parseInt(payday),
                            Integer.parseInt(arrear), contract, Integer.parseInt(period));
                if (!tenantName.equals("")) t.setTenantName(tenantName);
                if (!tenantNumber.equals("")) t.setPhoneNumber(tenantNumber);
                room.setTenants(t);
            }
        }
        intent.putExtra("room", room);
        setResult(RESULT_OK, intent);
        finish();
        return;
    }
}
