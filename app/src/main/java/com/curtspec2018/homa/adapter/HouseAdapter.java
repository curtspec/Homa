package com.curtspec2018.homa.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.curtspec2018.homa.G;
import com.curtspec2018.homa.R;
import com.curtspec2018.homa.databinding.ItemHouseBinding;
import com.curtspec2018.homa.house.HouseActivity;
import com.curtspec2018.homa.house.HouseEditActivity;
import com.curtspec2018.homa.vo.Building;
import com.curtspec2018.homa.vo.HouseListItem;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
        }
        TextView tvName = convertView.findViewById(R.id.tv_name);
        TextView tvAddress = convertView.findViewById(R.id.tv_address);
        CircleImageView ci = convertView.findViewById(R.id.circle_iv);
        ImageView ivEnter = convertView.findViewById(R.id.iv_enter);
        ImageView ivSelect = convertView.findViewById(R.id.iv_select);

        tvName.setText(items.get(position).getName());
        tvAddress.setText(items.get(position).getAddress());

        if (!items.get(position).getProfileUrl().equals("default"))
            Glide.with(context).load(items.get(position).getProfileUrl()).into(ci);

        ivEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HouseActivity activity = (HouseActivity) context;
                activity.editBuilding(position);
            }
        });

        ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HouseActivity activity = (HouseActivity) context;
                activity.exchangeCurrent(position);
            }
        });
        return convertView;
    }

}
