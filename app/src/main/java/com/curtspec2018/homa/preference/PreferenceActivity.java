package com.curtspec2018.homa.preference;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.curtspec2018.homa.R;
import com.curtspec2018.homa.databinding.ActivityPreferenceBinding;

public class PreferenceActivity extends AppCompatActivity {

    //ActivityPreferenceBinding b;
    TextView tvVer;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        //b = DataBindingUtil.setContentView(this, R.layout.activity_preference);

        tvVer = findViewById(R.id.tv_ver);
        toolbar = findViewById(R.id.toolbar);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            tvVer.setText("ver " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("설정");
    }
}
