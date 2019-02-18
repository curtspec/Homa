package com.curtspec2018.homa.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.curtspec2018.homa.R;
import com.curtspec2018.homa.tenant.FloorFragment;
import com.curtspec2018.homa.tenant.TenantActivity;
import com.curtspec2018.homa.vo.Room;

import java.util.ArrayList;

public class FloorChildRAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Room> rooms;

    public FloorChildRAdapter(Context context, ArrayList<Room> rooms) {
        this.context = context;
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new VH(inflater.inflate(R.layout.item_tenant_floor_child, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        VH v = (VH) viewHolder;
        v.tvName.setText(rooms.get(i).getName());
        v.tvNick.setText(rooms.get(i).getNickname());
        int floor = rooms.get(i).getFloor();
        v.tvFloor.setText(floor < 0 ? "지하 "+ (-1 * floor) +"층" : floor + "층");
        v.tvOccupied.setText(rooms.get(i).isOccupied() ? "거주중" : "공실");
        v.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TenantActivity tenantActivity = (TenantActivity) context;
                tenantActivity.editRoomInfo(rooms.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    class VH extends RecyclerView.ViewHolder{

        TextView tvName, tvNick, tvFloor, tvOccupied;
        RelativeLayout item;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvNick = itemView.findViewById(R.id.tv_nick);
            tvFloor = itemView.findViewById(R.id.tv_floor);
            tvOccupied = itemView.findViewById(R.id.tv_occupied);
            item = itemView.findViewById(R.id.item_view);
        }
    }

}
