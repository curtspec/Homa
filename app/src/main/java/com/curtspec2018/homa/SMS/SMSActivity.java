package com.curtspec2018.homa.SMS;

import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.curtspec2018.homa.R;
import com.curtspec2018.homa.adapter.SmsListAdapter;
import com.curtspec2018.homa.databinding.ActivitySmsBinding;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;

public class SMSActivity extends AppCompatActivity {

    ActivitySmsBinding b;

    ArrayList<Target> targets = new ArrayList<>();
    SmsListAdapter adapter;

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
    }

    public void clickAdd(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
    }

    public void clickOK(View view) {

    }

    public class Target {
        public String name;
        public String tenantName;
        public String number;

        public Target(String name, String tenantName, String number) {
            this.name = name;
            this.tenantName = tenantName;
            this.number = number;
        }
    }
}
