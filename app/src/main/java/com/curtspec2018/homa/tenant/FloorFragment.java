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

import com.curtspec2018.homa.R;
import com.curtspec2018.homa.adapter.FloorParentsRAdapter;
import com.curtspec2018.homa.vo.Floor;

import java.util.ArrayList;

public class FloorFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Floor> floors = new ArrayList<>();
    LinearLayout emptyView;

    public static final int REQUEST_EDIT_ROOM = 1;
    public static final int REQUEST_CREATE_ROOM = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_tenant_fllor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        FloorParentsRAdapter adapter = new FloorParentsRAdapter(getContext(), floors);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);

        emptyView = view.findViewById(R.id.empty_view);
    }

    @Override
    public void onResume() {
        super.onResume();
        emptyView.setVisibility(floors.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        recyclerView.setVisibility(floors.size() != 0 ? View.VISIBLE : View.INVISIBLE);
    }
}
