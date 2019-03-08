package com.curtspec2018.homa;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.curtspec2018.homa.adapter.ChartMarker;
import com.curtspec2018.homa.databinding.FragMtBinding;
import com.curtspec2018.homa.vo.Building;
import com.curtspec2018.homa.vo.Floor;
import com.curtspec2018.homa.vo.Room;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.StackedValueFormatter;

import java.util.ArrayList;
import java.util.Random;

public class MTFragment extends Fragment {

    FragMtBinding b;

    BarChart chart;
    CombinedData data = new CombinedData();
    Random rand = new Random();

    ArrayList<Floor> floors;
    int maxNumOfFloor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.frag_mt, container, false);
        b.setFrag(this);
        Building currentBuilding = G.getCurrentBuilding();
        if (currentBuilding == null) floors = new ArrayList<>();
        else{
            floors = currentBuilding.getFloors();
            maxNumOfFloor = currentBuilding.getNumOfFloor();
        }
        return inflater.inflate(R.layout.frag_mt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        chart = view.findViewById(R.id.chart);
        chart.setDrawBarShadow(false);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);

        //xAxis
        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return ((int)value) + "층";
            }
        });

        //YAxis
        chart.getAxisRight().setEnabled(false);
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setGranularityEnabled(true);
        yAxis.setGranularity(1);
        yAxis.setAxisMinimum(0);

        chart.setData(getBarData());
        ChartMarker marker = new ChartMarker(getContext(), R.layout.marker_chart);
        chart.setMarker(marker);

        super.onViewCreated(view, savedInstanceState);
    }

    public BarData getBarData(){
        ArrayList<Integer> roomCnt = new ArrayList<>();
        for (int i =0; i < maxNumOfFloor; i ++ )  roomCnt.add(i+1);

        ArrayList<BarEntry> entries1 = new ArrayList<>();
        for (Floor f : floors){
            int x = f.getFloor();
            roomCnt.remove(new Integer(x));
            int y = f.getRooms().size();
            int occupiedRoom = 0;
            for (Room r : f.getRooms()){
                if (r.isOccupied() && r.getTenants() != null){
                    occupiedRoom ++;
                }
            }
            entries1.add(new BarEntry(x, new float[]{occupiedRoom, y-occupiedRoom}));
        }

        for (Integer t : roomCnt){
            entries1.add(new BarEntry(t.intValue(), new float[]{0,0}));
        }

        BarDataSet set1 = new BarDataSet(entries1, "label1");
        set1.setDrawValues(false);
        set1.setColors(0xff80c6af, 0xff00ffaa);
        set1.setStackLabels(new String[]{"거주중","공실"});

        BarData data = new BarData(set1);
        data.setBarWidth(0.6f);
        data.setValueFormatter(new StackedValueFormatter(true, "s", 0));

        return data;
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
