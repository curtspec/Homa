package com.curtspec2018.homa.SMS;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.curtspec2018.homa.R;
import com.curtspec2018.homa.adapter.SmsListAdapter;
import com.curtspec2018.homa.databinding.ActivitySmsBinding;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;

public class SMSActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    ActivitySmsBinding b;

    ArrayList<Target> targets = new ArrayList<>();
    SmsListAdapter adapter;

    ArrayList<Target> addable = new ArrayList<>();
    ArrayList<Target> added = new ArrayList<>();
    ListView addableList;
    ListView addedList;
    SmsListAdapter addableAdapter;
    SmsListAdapter addedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        b = DataBindingUtil.setContentView(this, R.layout.activity_sms);
        setSupportActionBar(b.toolbar);
        getSupportActionBar().setTitle("SMS/문자");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        b.switchType.setFocusableInTouchMode(true);
        b.switchType.requestFocus();
        b.editMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String src = s.toString();
                int size = src.length();
                int limit = 0;
                for (int i = 0; i < size; i++){
                    if (src.charAt(i) < 128) limit++;
                    else limit += 2;
                    if (limit > 139){
                        src = src.substring(0, src.length() -1);
                        b.editMsg.setText(src);
                        b.editMsg.setSelection(src.length());
                        continue;
                    }
                }
                b.tvLimit.setText(limit + "/140");
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        adapter = new SmsListAdapter(targets, getLayoutInflater());
        b.listview.setAdapter(adapter);

        b.switchType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    targets.addAll(addable);
                    addable.clear();
                    adapter.notifyDataSetChanged();
                }else {
                    addable.addAll(targets);
                    targets.clear();
                    adapter.notifyDataSetChanged();
                }
                b.tvNum.setText(targets.size() + "명");
            }
        });

        //testdata
        addable.add(new Target("201호", "박성진", "01027117876"));
        addable.add(new Target("202호", "방주연", "01092325327"));
        addable.add(new Target("203호", "김지윤", "01045276767"));
    }

    public void clickAdd(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_sms, null);
        initDialog(v);
        builder.setView(v);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                targets = added;
                adapter.notifyDataSetChanged();
                b.tvNum.setText(targets.size() + "명");
                initLists();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initLists();
                dialog.dismiss();
            }
        });
        builder.show().setCanceledOnTouchOutside(false);
    }

    private void initDialog(View v){
        addableList = v.findViewById(R.id.list_addable);
        addedList = v.findViewById(R.id.list_added);
        added = targets;
        addableAdapter = new SmsListAdapter(addable, getLayoutInflater());
        addedAdapter = new SmsListAdapter(added, getLayoutInflater());
        addableList.setAdapter(addableAdapter);
        addedList.setAdapter(addedAdapter);
        addableList.setOnItemClickListener(this);
        addedList.setOnItemClickListener(this);
        ImageView btnAdd = v.findViewById(R.id.iv_add);
        ImageView btnRemove = v.findViewById(R.id.iv_remove);
        btnAdd.setOnClickListener(this);
        btnRemove.setOnClickListener(this);
    }

    private void initLists(){
        for (Target t : targets){
            if (t.isSelected) t.isSelected = false;
        }
        for (Target t : addable){
            if (t.isSelected) t.isSelected = false;
        }
        for (Target t : added){
            if (t.isSelected) t.isSelected = false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SmsListAdapter adapter = (SmsListAdapter) parent.getAdapter();
        Target t = (Target) adapter.getItem(position);
        t.isSelected = !t.isSelected;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_add:
                moveData(addable, added);
                break;
            case R.id.iv_remove :
                moveData(added, addable);
                break;
        }
    }

    private void moveData(ArrayList<Target> from, ArrayList<Target> to){
        ArrayList<Target> selected = new ArrayList<>();
        int cnt = 0;
        for (Target t : from) {
            if (t.isSelected) {
                t.isSelected = false;
                selected.add(t);
                cnt++;
            }
        }
        if (cnt == 0) return;
        to.addAll(selected);
        from.removeAll(selected);
        addableAdapter.notifyDataSetChanged();
        addedAdapter.notifyDataSetChanged();
    }


    public void clickOK(View view) {
        if (targets.size() == 0) {
            Toast.makeText(this, "보내는 대상이 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        String msg = b.editMsg.getText().toString();
        if (msg.equals("")) msg = b.editMsg.getHint().toString();
        StringBuffer buffer = new StringBuffer();
        for (Target t  : targets) buffer.append(t.number + ",");

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("sms:"+buffer.toString()));
        intent.putExtra("sms_body", msg);
        startActivity(intent);
    }

    public class Target {

        public String name;
        public String tenantName;
        public String number;
        public boolean isSelected = false;

        public Target(String name, String tenantName, String number) {
            this.name = name;
            this.tenantName = tenantName;
            this.number = number;
        }

    }

}
