package com.curtspec2018.homa.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.curtspec2018.homa.R;
import com.curtspec2018.homa.tenant.TenantActivity;
import com.curtspec2018.homa.vo.Room;

import java.util.ArrayList;

public class EmptyRecycleAdapter extends RecyclerView.Adapter {

    ArrayList<Room> emptyRooms;
    Context context;

    public EmptyRecycleAdapter(ArrayList<Room> emptyRooms, Context context) {
        this.emptyRooms = emptyRooms;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VH(LayoutInflater.from(context).inflate(R.layout.item_tenant_empty, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        VH v = (VH) viewHolder;
        v.tvName.setText(emptyRooms.get(i).getName());
        v.tvNick.setText(emptyRooms.get(i).getNickname());
        v.tvFloor.setText(emptyRooms.get(i).getFloor() + "층");
        v.ivEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TenantActivity activity = (TenantActivity) context;
                activity.editRoomInfo(emptyRooms.get(i), TenantActivity.REQUEST_EDIT_EMPTY, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return emptyRooms.size();
    }

    class VH extends RecyclerView.ViewHolder{

        TextView tvName, tvNick, tvFloor;
        ImageView ivEnter;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvFloor = itemView.findViewById(R.id.tv_floor);
            tvNick = itemView.findViewById(R.id.tv_nick);
            ivEnter = itemView.findViewById(R.id.iv_enter);
        }
    }
}
