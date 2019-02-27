package com.curtspec2018.homa.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.curtspec2018.homa.G;
import com.curtspec2018.homa.R;
import com.curtspec2018.homa.databinding.ItemHouseBinding;
import com.curtspec2018.homa.house.HouseActivity;
import com.curtspec2018.homa.house.HouseEditActivity;
import com.curtspec2018.homa.vo.Building;
import com.curtspec2018.homa.vo.HouseListItem;

import java.util.ArrayList;

public class HouseAdapter extends BaseAdapter{

    ArrayList<Building> items;
    LayoutInflater inflater;
    Context context;
    ItemHouseBinding b;

    public HouseAdapter(ArrayList<Building> items, Context context) {
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

        if (!items.get(position).getProfileUrl().equals("default"))
            Glide.with(context).load(items.get(position).getProfileUrl()).into(b.circleIv);

        b.ivEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HouseActivity activity = (HouseActivity) context;
                activity.editBuilding(position);
            }
        });
        b.ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Building currentBuilding = G.getCurrentBuilding();
                if (currentBuilding == null){
                    G.setCurrentBuilding(G.getBuildings().get(position));
                    G.getBuildings().remove(position);
                }else {
                    G.getBuildings().add(currentBuilding);
                    G.setCurrentBuilding(G.getBuildings().get(position));
                    G.getBuildings().remove(position);
                }
            }
        });
        return convertView;
    }

}
