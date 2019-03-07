package com.curtspec2018.homa;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.curtspec2018.homa.databinding.FragMsBinding;
import com.curtspec2018.homa.vo.Building;
import com.curtspec2018.homa.vo.Room;

import java.util.ArrayList;

public class MSFragment extends Fragment {

    FragMsBinding b;
    ArrayList<Room> arrears;

    Building currentBuilding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.frag_ms, container, false);
        b.setFrag(this);
        currentBuilding = G.getCurrentBuilding();
        if (currentBuilding == null) arrears = new ArrayList<>();
        else arrears = currentBuilding.getArrears();
        return inflater.inflate(R.layout.frag_ms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        b.listviewNot.setEmptyView(b.emptyView);
        b.listviewYet.setEmptyView(b.emptyView);

        super.onViewCreated(view, savedInstanceState);
    }


}
