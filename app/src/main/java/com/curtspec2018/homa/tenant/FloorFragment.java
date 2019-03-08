package com.curtspec2018.homa.tenant;

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
import android.widget.Toast;

import com.curtspec2018.homa.G;
import com.curtspec2018.homa.R;
import com.curtspec2018.homa.adapter.FloorParentsRAdapter;
import com.curtspec2018.homa.adapter.RecyclerVerticalDeco;
import com.curtspec2018.homa.vo.Building;
import com.curtspec2018.homa.vo.Floor;
import com.curtspec2018.homa.vo.Room;

import java.util.ArrayList;
import java.util.Collections;

public class FloorFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Floor> floors = new ArrayList<>();
    LinearLayout emptyView;
    FloatingActionButton fab;
    FloorParentsRAdapter adapter;

    Building currentBuilding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        currentBuilding = G.getCurrentBuilding();
        if (currentBuilding != null) floors = currentBuilding.getFloors();

//        // ===================================== testdata =====================================
//        ArrayList<Room> rooms = new ArrayList<>();
//        Room r = new Room("202호", "2층 첫방", 2, false, false);
//        Room r2 = new Room("203호", "2층 두번째방", 2, false, false);
//        rooms.add(r);
//        rooms.add(r2);
//        rooms.add(new Room("205호", "2층 두번째방", 2, false, false));
//        rooms.add(new Room("209호", "2층 끝방", 2, false, false));
//        rooms.add(new Room("208호", "2층 두번째방", 2, false, false));
//        rooms.add(new Room("201호", "2층 두번째방", 2, false, false));
//        rooms.add(new Room("204호", "2층 두번째방", 2, false, false));
//        floors.add(new Floor(2, rooms));
//        // ===================================== testdata =====================================

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
        recyclerView.addItemDecoration(new RecyclerVerticalDeco());

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TenantActivity activity = (TenantActivity) getContext();
                activity.createRoomInfo();
            }
        });

        emptyView.setVisibility(floors.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        recyclerView.setVisibility(floors.size() != 0 ? View.VISIBLE : View.INVISIBLE);
    }

    public void saveData(){
        if (currentBuilding != null){
            G.getCurrentBuilding().setFloors(floors);
        }
    }

    public void refreshView(){
        if (currentBuilding != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void addItem(Room room){
        int floor = room.getFloor();
        int index = -1;
        for (int i = 0; i< floors.size(); i++){
            if (floors.get(i).getFloor() == floor){
                index = i;
                break;
            }
        }
        if (index < 0) {
            ArrayList<Room> rooms = new ArrayList<>();
            rooms.add(room);
            floors.add(new Floor(floor, rooms));
            adapter.notifyDataSetChanged();
        }else {
            floors.get(index).getRooms().add(room);
            adapter.notifyItemChanged(index);
        }
        if (!room.isOccupied() && room.getTenants() == null){
            EmptyFragment empty = (EmptyFragment) getFragmentManager().getFragments().get(1);
            empty.emptyRoom.add(room);
            empty.adapter.notifyDataSetChanged();
        }
    }

    public void editItem(Room room, int position){
        int floor = room.getFloor();
        int index = -1;
        for (int i = 0; i< floors.size(); i++){
            if (floors.get(i).getFloor() == floor){
                index = i;
                break;
            }
        }
        if (index >= 0){
            ArrayList<Room> rooms = floors.get(index).getRooms();
            rooms.remove(position);
            rooms.add(position, room);
            adapter.adapter.notifyItemChanged(position);

            if (currentBuilding != null) {
                currentBuilding.setRoom(room);
            }

            if (room.isOccupied()){
                EmptyFragment empty = (EmptyFragment) getFragmentManager().getFragments().get(1);
                empty.emptyRoom.clear();
                empty.emptyRoom.addAll(currentBuilding.getEmptyRoom());
                empty.adapter.notifyDataSetChanged();
            }
        }
    }

    public void editItem(Room room){
        int floor = room.getFloor();
        int index = -1;
        for (int i = 0; i< floors.size(); i++){
            if (floors.get(i).getFloor() == floor){
                index = i;
                break;
            }
        }
        if (index >= 0){
            ArrayList<Room> rooms = floors.get(index).getRooms();
            int position = -1;
            for (int i =0; i < rooms.size(); i++){
                if (rooms.get(i).equals(room)){
                    position = i;
                    break;
                }
            }
            if (position >= 0){
                rooms.remove(position);
                rooms.add(position, room);
                adapter.notifyItemChanged(index);
            }
            if (currentBuilding != null) {
                currentBuilding.setRoom(room);
            }

            if (room.isOccupied()){
                EmptyFragment empty = (EmptyFragment) getFragmentManager().getFragments().get(1);
                empty.emptyRoom.clear();
                empty.emptyRoom.addAll(currentBuilding.getEmptyRoom());
                empty.adapter.notifyDataSetChanged();
            }
        }
    }

    public void deleteItem(Room room, int position){
        int floor = room.getFloor();
        int index = -1;
        for (int i = 0; i< floors.size(); i++){
            if (floors.get(i).getFloor() == floor){
                index = i;
                break;
            }
        }
        if (index >= 0){
            ArrayList<Room> rooms = floors.get(index).getRooms();
            rooms.remove(position);
            adapter.adapter.notifyDataSetChanged();
            if (rooms.size() == 0) {
                floors.remove(index);
                if (floors.size() == 0){
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }
            }
            adapter.notifyItemChanged(index);
            if (currentBuilding != null) {
                currentBuilding.removeRoom(room);
                EmptyFragment empty = (EmptyFragment) getFragmentManager().getFragments().get(1);
                empty.emptyRoom.clear();
                empty.emptyRoom.addAll(currentBuilding.getEmptyRoom());
                empty.adapter.notifyDataSetChanged();
            }
        }

    }

    public void deleteItem(Room room){
        int floor = room.getFloor();
        int index = -1;
        for (int i = 0; i< floors.size(); i++){
            if (floors.get(i).getFloor() == floor){
                index = i;
                break;
            }
        }
        if (index >= 0){
            ArrayList<Room> rooms = floors.get(index).getRooms();
            int position = -1;
            for (int i =0; i < rooms.size(); i++){
                if (rooms.get(i).equals(room)){
                    position = i;
                    break;
                }
            }
            if (position >= 0){
                rooms.remove(position);
                adapter.adapter.notifyItemChanged(position);
            }
            if (rooms.size() == 0) {
                floors.remove(index);
                if (floors.size() == 0){
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }
            }
            adapter.notifyItemChanged(index);
            if (currentBuilding != null) {
                currentBuilding.removeRoom(room);
                EmptyFragment empty = (EmptyFragment) getFragmentManager().getFragments().get(1);
                empty.emptyRoom.clear();
                empty.emptyRoom.addAll(currentBuilding.getEmptyRoom());
                empty.adapter.notifyDataSetChanged();
            }
        }

    }
}
