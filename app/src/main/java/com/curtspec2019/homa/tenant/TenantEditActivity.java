package com.curtspec2019.homa.tenant;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import com.bumptech.glide.Glide;
import com.curtspec2019.homa.G;
import com.curtspec2019.homa.R;
import com.curtspec2019.homa.databinding.ActivityTenantEditBinding;
import com.curtspec2019.homa.vo.Building;
import com.curtspec2019.homa.vo.Room;
import com.curtspec2019.homa.vo.Tenant;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TenantEditActivity extends AppCompatActivity {

    ActivityTenantEditBinding b;
    Intent intent;
    String type;

    String imgUrl;
    String tag;

    Room room;
    final static int PICK_PHOTO = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_edit);
        b = DataBindingUtil.setContentView(this, R.layout.activity_tenant_edit);

        setSupportActionBar(b.toolbar);
        intent = getIntent();
        getSupportActionBar().setTitle(R.string.tenant_edit_activity_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        initViewsValue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        type = intent.getStringExtra("type");
        if (type.equals("new")) return true;
        getMenuInflater().inflate(R.menu.house_edit, menu);
        Drawable drawable = menu.getItem(0).getIcon();
        if (drawable != null){
            drawable.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("정말 삭제하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (room != null) G.getCurrentBuilding().removeRoom(room);
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
                builder.show().setCanceledOnTouchOutside(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PICK_PHOTO:
                if (resultCode == RESULT_OK){
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

                        if (room == null || room.getTag() == null) tag = Calendar.getInstance().getTimeInMillis() + "";
                        else tag = room.getTag();

                        String fileName = tag + "contractPaper." + format;
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference rootRef = storage.getReference();
                        StorageReference targetRef = rootRef.child(G.getId() + "/contractPaper/" + fileName);
                        UploadTask task = targetRef.putFile(uri);
                        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                targetRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imgUrl = uri.toString();
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        });
                    }
                }
                break;
        }
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

    private void initViewsValue() {
        room = (Room) intent.getSerializableExtra("room");
        if (room != null){
            b.editFloor.setText(room.getFloor()+"");
            b.editName.setText(room.getName());
            b.editNick.setText(room.getNickname());
            Tenant t = room.getTenants();
            if (room.isOccupied() && t != null){
                b.switchRegister.setChecked(true);
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
                if (t.getImgUrl() != null)  {
                    Glide.with(this).load(t.getImgUrl()).into(b.iv);
                    imgUrl = t.getImgUrl();
                }
                else Glide.with(this).load(R.drawable.ic_request_image).into(b.iv);
                b.editTenantName.setText(t.getTenantName());
                b.editTenantNumber.setText(t.getPhoneNumber());
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

        int maxFloor = 0;
        maxFloor = G.getCurrentBuilding().getNumOfFloor();
        picker.setMaxValue(maxFloor);
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
        String day = "15";
        if (!b.editContract.equals("")){
            String[] tmp = b.editContract.getText().toString().split("\\.");
            day = tmp[tmp.length - 1];
        }
        picker.setValue(Integer.parseInt(day));
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
        }else if (type.equals("new") && G.getCurrentBuilding().isExist(name)){
            new AlertDialog.Builder(this).setMessage("이미 존재하는 호수입니다").show();
            return;
        }

        room = new Room(name, nick, Integer.parseInt(floor), isOccupied, isUnderGround);
        if (isOccupied){
            String rent = b.editMonthly.getText().toString().trim();
            String maintenance = b.editMaintenance.getText().toString().trim();
            String contractDay = b.editContract.getText().toString();
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
                if (imgUrl != null) t.setImgUrl(imgUrl);
                if (tag != null) room.setTag(tag);

                room.setTenants(t);
            }
        }

        Building currentBuilding = G.getCurrentBuilding();

        if (type.equals("new")){
            currentBuilding.addRoom(room);
        }else {
            currentBuilding.setRoom(room);
        }

        setResult(RESULT_OK, intent);
        finish();
        return;
    }
}
