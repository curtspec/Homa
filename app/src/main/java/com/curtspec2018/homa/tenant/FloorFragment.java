package com.curtspec2018.homa.tenant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.curtspec2018.homa.R;
import com.curtspec2018.homa.adapter.FloorParentsRAdapter;
import com.curtspec2018.homa.vo.Floor;
import com.curtspec2018.homa.vo.Room;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class FloorFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Floor> floors = new ArrayList<>();
    LinearLayout emptyView;
    FloatingActionButton fab;
    FloorParentsRAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // ===================================== testdata =====================================
        ArrayList<Room> rooms = new ArrayList<>();
        Room r = new Room("202호", "2층 첫방", 2, false, false);
        Room r2 = new Room("203호", "2층 두번째방", 2, false, false);
        rooms.add(r);
        rooms.add(r2);
        rooms.add(new Room("205호", "2층 두번째방", 2, false, false));
        rooms.add(new Room("209호", "2층 끝방", 2, false, false));
        rooms.add(new Room("208호", "2층 두번째방", 2, false, false));
        rooms.add(new Room("201호", "2층 두번째방", 2, false, false));
        rooms.add(new Room("204호", "2층 두번째방", 2, false, false));
        floors.add(new Floor(2, rooms));
        // ===================================== testdata =====================================

        return inflater.inflate(R.layout.frag_tenant_fllor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        Collections.sort(floors);
        adapter = new FloorParentsRAdapter(getContext(), floors);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        emptyView = view.findViewById(R.id.empty_view);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TenantActivity activity = (TenantActivity) getContext();
                activity.createRoomInfo();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        emptyView.setVisibility(floors.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        recyclerView.setVisibility(floors.size() != 0 ? View.VISIBLE : View.INVISIBLE);
    }

    public void addItem(Room room){
        int floor = room.getFloor();
        int cnt = -1;
        for (int i = 0; i< floors.size(); i++){
            if (floors.get(i).getFloor() == floor){
                cnt = i;
                break;
            }
        }
        if (cnt < 0) {
            ArrayList<Room> rooms = new ArrayList<>();
            rooms.add(room);
            floors.add(new Floor(floor, rooms));
        }else {
            floors.get(cnt).getRooms().add(room);
        }
    }

}
