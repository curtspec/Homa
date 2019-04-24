package com.curtspec2019.homa.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.curtspec2019.homa.R;
import com.curtspec2019.homa.vo.Schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MemoListAdapter extends BaseAdapter {

    ArrayList<Schedule> schedules;
    LayoutInflater inflater;

    public MemoListAdapter(ArrayList<Schedule> schedules, LayoutInflater inflater) {
        this.schedules = schedules;
        this.inflater = inflater;
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
        if (convertView == null) convertView = inflater.inflate(R.layout.item_memo, parent, false);
        TextView tvTitle = convertView.findViewById(R.id.tv_title);
        TextView tvTime = convertView.findViewById(R.id.tv_time);
        TextView tvSubTitle = convertView.findViewById(R.id.tv_memo);

        tvTitle.setText(schedules.get(position).getTitle());
        String time = new SimpleDateFormat("yyyy.MM.dd").format(new Date(schedules.get(position).getDate().getTimeInMillis()));
        tvTime.setText(time);
        tvSubTitle.setText(schedules.get(position).getSubTitle());
        return convertView;
    }
}
