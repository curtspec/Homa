package com.curtspec2018.homa.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.curtspec2018.homa.R;
import com.curtspec2018.homa.vo.Schedule;

import java.util.ArrayList;

public class MFFragListAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<Schedule> schedules;

    public MFFragListAdapter(LayoutInflater inflater, ArrayList<Schedule> schedules) {
        this.inflater = inflater;
        this.schedules = schedules;
    }

    @Override
    public int getCount() {
        return schedules.size();
    }

    @Override
    public Object getItem(int position) {
        return schedules.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = inflater.inflate(R.layout.item_mf, parent, false);
        TextView tvWhere = convertView.findViewById(R.id.tv_where);
        TextView tvTitle = convertView.findViewById(R.id.tv_title);
        TextView tvSubTitle = convertView.findViewById(R.id.tv_sub_title);

        tvWhere.setText(schedules.get(position).getWhere());
        tvTitle.setText(schedules.get(position).getTitle());
        tvSubTitle.setText(schedules.get(position).getSubTitle());

        return convertView;
    }
}
