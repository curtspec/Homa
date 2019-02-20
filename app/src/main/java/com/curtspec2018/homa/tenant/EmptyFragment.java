package com.curtspec2018.homa.tenant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.curtspec2018.homa.R;
import com.curtspec2018.homa.adapter.EmptyRecycleAdapter;
import com.curtspec2018.homa.adapter.RecyclerVerticalDeco;
import com.curtspec2018.homa.vo.Room;

import java.util.ArrayList;
import java.util.Collections;

public class EmptyFragment extends Fragment {

    ArrayList<Room> emptyRoom = new ArrayList<>();
    RecyclerView recycler;
    EmptyRecycleAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Room r = new Room("103호", "1층끝방", 1, false, false);
        emptyRoom.add(r);
        emptyRoom.add(new Room("101호", "1층첫방", 1, false, false));
        return inflater.inflate(R.layout.frag_tenant_empty, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Collections.sort(emptyRoom);
        adapter = new EmptyRecycleAdapter(emptyRoom, getContext());
        recycler = view.findViewById(R.id.recycler);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new RecyclerVerticalDeco());
    }

    @Override
    public void onResume() {
        super.onResume();
        recycler.setVisibility(emptyRoom.size() == 0 ? View.GONE : View.VISIBLE);
    }

    public void editItem(Room room, int position){
        if (room.isOccupied()) {
            emptyRoom.remove(position);
            adapter.notifyDataSetChanged();
        }
        else {
            emptyRoom.remove(position);
            emptyRoom.add(position, room);
            adapter.notifyItemChanged(position);
        }
    }

    public void deleteItem(Room room, int position){
        emptyRoom.remove(position);
        adapter.notifyDataSetChanged();
    }
}
