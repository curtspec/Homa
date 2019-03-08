package com.curtspec2018.homa.account;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curtspec2018.homa.G;
import com.curtspec2018.homa.R;
import com.curtspec2018.homa.adapter.ChartMarker;
import com.curtspec2018.homa.databinding.FragAccountEntireBinding;
import com.curtspec2018.homa.databinding.FragAccountMonthlyBinding;
import com.curtspec2018.homa.vo.Building;
import com.curtspec2018.homa.vo.MonthAccount;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.StackedValueFormatter;

import java.util.ArrayList;
import java.util.Random;

public class EntireFragment extends Fragment {

    ArrayList<MonthAccount> monthAccounts = new ArrayList<>();
    Random rand = new Random();

    TextView tvIncome, tvExpense, tvPure;
    CombinedChart chart;
    CombinedData data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Building currentBuilding = G.getCurrentBuilding();
        if (currentBuilding != null){
            monthAccounts = currentBuilding.getAccounts();
            MonthAccount thisMonth = currentBuilding.getCurrnetMonth();
            if (thisMonth != null){
                monthAccounts.add(0, thisMonth);
            }
        }
        return inflater.inflate(R.layout.frag_account_entire, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvIncome = view.findViewById(R.id.tv_income);
        tvExpense = view.findViewById(R.id.tv_expense);
        tvPure = view.findViewById(R.id.tv_pure);
        int incomes = 0, expenses = 0;
        for (MonthAccount t : monthAccounts){
            incomes += t.getTotalIncome();
            expenses += t.getTotalExpense();
        }
        tvIncome.setText(G.divisionThousand(incomes));
        tvExpense.setText(G.divisionThousand(expenses));
        tvPure.setText(G.divisionThousand(incomes - expenses));

        chart = view.findViewById(R.id.chart);
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE});

        chart.setDrawOrder(new CombinedChart.DrawOrder[]{CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE});
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);

        data = new CombinedData();

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        chart.getAxisRight().setEnabled(false);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawGridLines(false);

        data.setData(getBarData());
        data.setData(getLineData());

        chart.setData(data);
        ChartMarker marker = new ChartMarker(getContext(), R.layout.marker_chart);

        chart.setMarker(marker);

        xAxis.setAxisMaximum(data.getXMax() + 0.5f);
        xAxis.setAxisMinimum(data.getXMin() - 0.5f);



    }

    public LineData getLineData(){

        ArrayList<Entry> inEntries = new ArrayList<>();
        ArrayList<Entry> outEntries = new ArrayList<>();

        //Temp data
        for (int i = 0; i <= 12; i++) {
            inEntries.add(new Entry(i, rand.nextInt(100)+30));
            outEntries.add(new Entry(i, rand.nextInt(100)+30));
        }

        LineDataSet inDataSet = new LineDataSet(inEntries, "in");
        inDataSet.setDrawCircles(false);
        inDataSet.setDrawFilled(false);
        inDataSet.setColor(0xffd2c342);
        inDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        inDataSet.setCubicIntensity(0.15f);
        inDataSet.setLineWidth(2.0f);

        LineDataSet outDataSet = new LineDataSet(outEntries, "out");
        outDataSet.setDrawCircles(false);
        outDataSet.setDrawValues(false);
        outDataSet.setColor(0xff74FFA0);
        outDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        outDataSet.setCubicIntensity(0.15f);
        outDataSet.setLineWidth(2.0f);

        LineData data = new LineData(inDataSet, outDataSet);
        data.setDrawValues(false);

        return data;
    }

    public BarData getBarData(){
        ArrayList<BarEntry> entries1 = new ArrayList<>();

        for (int i = 0; i<= 12; i++){
            entries1.add(new BarEntry(i, rand.nextInt(60)+60));
        }

        BarDataSet set1 = new BarDataSet(entries1, "label1");
        set1.setDrawValues(false);
        set1.setColors(0xff80c6af);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarData data = new BarData(set1);
        data.setBarWidth(0.6f);
        data.setValueFormatter(new StackedValueFormatter(false, "s", 0));

        return data;
    }
}
