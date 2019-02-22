package com.curtspec2018.homa.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.curtspec2018.homa.R;
import com.curtspec2018.homa.SMS.SMSActivity;


import java.util.ArrayList;

public class SmsListAdapter extends BaseAdapter {

    ArrayList<SMSActivity.Target> targets;
    LayoutInflater inflater;

    public SmsListAdapter(ArrayList<SMSActivity.Target> targets, LayoutInflater inflater) {
        this.targets = targets;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return targets.size();
    }

    @Override
    public Object getItem(int position) {
        return targets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = inflater.inflate(R.layout.item_acount_entire, parent, false);

        TextView tvName = convertView.findViewById(R.id.tv_name);
        TextView tvTenantName = convertView.findViewById(R.id.tv_tenant_name);
        TextView tvTenantNumber = convertView.findViewById(R.id.tv_tenant_number);

        tvName.setText(targets.get(position).name);
        tvTenantName.setText(targets.get(position).tenantName);
        tvTenantNumber.setText(targets.get(position).number);

        return convertView;
    }
}
