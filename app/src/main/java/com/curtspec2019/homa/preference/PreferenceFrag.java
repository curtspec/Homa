package com.curtspec2019.homa.preference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

import com.curtspec2019.homa.G;
import com.curtspec2019.homa.LoginActivity;
import com.curtspec2019.homa.R;
import com.curtspec2019.homa.account.CurrentFragment;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class PreferenceFrag extends PreferenceFragment {

    SharedPreferences preferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Preference idPref = findPreference("id");
        String id = G.getId();
        if (id != null) idPref.setSummary(id);

        Preference versionPref = findPreference("version");
        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            int versionCode = pInfo.versionCode;
            versionPref.setSummary("ver " + version + " - " + versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        SharedPreferences pref = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        SwitchPreference autoPref = (SwitchPreference) findPreference("auto");
        autoPref.setChecked(pref.getBoolean("auto", false));

        preferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals("auto")){
                    boolean isAuto = preferences.getBoolean(key, false);

                    if (isAuto){
                        pref.edit().putBoolean("auto", true).putString("id", G.getId()).apply();
                    }else {
                        pref.edit().clear().apply();
                    }
                }
            }
        });

        Preference logoutPref = findPreference("logout");
        logoutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference prfce) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("로그아웃 하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        G.setId(null);
                        G.setCurrentBuilding(null);
                        G.setBuildings(new ArrayList<>());
                        G.setMemos(new ArrayList<>());
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        pref.edit().clear().apply();
                        SharedPreferences accPref = getActivity().getSharedPreferences(CurrentFragment.PREFERENCE_NAME, Context.MODE_PRIVATE);
                        accPref.edit().clear().apply();
                        dialog.dismiss();
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show().setCanceledOnTouchOutside(false);

                return true;
            }
        });

    }
}
