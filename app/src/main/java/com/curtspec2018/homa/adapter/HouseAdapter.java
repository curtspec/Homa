package com.curtspec2018.homa.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.curtspec2018.homa.R;
import com.curtspec2018.homa.databinding.ItemHouseBinding;
import com.curtspec2018.homa.vo.HouseListItem;

import java.util.ArrayList;

public class HouseAdapter extends BaseAdapter implements View.OnClickListener {

    ArrayList<HouseListItem> items;
    LayoutInflater inflater;
    Context context;
    ItemHouseBinding b;

    public HouseAdapter(ArrayList<HouseListItem> items, Context context) {
        this.items = items;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_house, parent, false);
            b = DataBindingUtil.inflate(inflater, R.layout.item_house, parent, false);
        }
        b.tvName.setText(items.get(position).getName());
        b.tvAddress.setText(items.get(position).getAddress());
        b.tvDetail.setText(items.get(position).getDetailAdd());
        b.ivEnter.setOnClickListener(this);
        b.ivSelect.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_select:

                break;
            case R.id.iv_enter:
                //Intent intent = new Intent(context, );
                break;
        }
    }
}
