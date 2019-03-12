package com.curtspec2018.homa.tenant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
        Collections.sort(floors);

        return inflater.inflate(R.layout.frag_tenant_fllor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
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

    public void scrollTo(String name){
        ArrayList<Room> rooms;
        for (int i = 0; i < floors.size(); i ++){
            rooms = floors.get(i).getRooms();
            for (int j =0; j < rooms.size(); j ++){
                if (rooms.get(j).getName().equals(name)){
                    recyclerView.smoothScrollToPosition(i);
                    return;
                }
            }
        }
    }

    public void refreshView(){
        FragmentManager fm = getFragmentManager();
        if (fm != null){
            FragmentTransaction ft = fm.beginTransaction();
            if (ft != null){
                ft.detach(this).attach(this).commit();
            }
        }
    }

}
