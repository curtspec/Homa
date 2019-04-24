package com.curtspec2019.homa.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curtspec2019.homa.R;
import com.curtspec2019.homa.vo.Floor;
import com.curtspec2019.homa.vo.Room;

import java.util.ArrayList;
import java.util.Collections;

public class FloorParentsRAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Floor> floors;
    public FloorChildRAdapter adapter;

    public FloorParentsRAdapter(Context context, ArrayList<Floor> floors) {
        this.context = context;
        this.floors = floors;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new VH(inflater.inflate(R.layout.item_tanent_floor_parents, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        VH v = (VH) viewHolder;
        int floor = floors.get(i).getFloor();
        v.tv.setText(floor < 0 ? "지하 "+ (-1 * floor) +"층" : floor + "층");
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        v.recyclerView.setLayoutManager(manager);
        ArrayList<Room> rooms = floors.get(i).getRooms();
        Collections.sort(rooms);
        adapter = new FloorChildRAdapter(context, rooms);
        v.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return floors.size();
    }

    private class VH extends RecyclerView.ViewHolder {

        TextView tv;
        RecyclerView recyclerView;

        public VH(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_floor);
            recyclerView = itemView.findViewById(R.id.recycler);
        }
    }
}
