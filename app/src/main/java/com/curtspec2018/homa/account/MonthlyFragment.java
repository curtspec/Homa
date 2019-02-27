package com.curtspec2018.homa.account;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.curtspec2018.homa.R;
import com.curtspec2018.homa.adapter.CurrentListAdapter;
import com.curtspec2018.homa.adapter.MonthlyListAdapter;
import com.curtspec2018.homa.vo.Account;
import com.curtspec2018.homa.vo.MonthAccount;

import java.util.ArrayList;

public class MonthlyFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView listView;
    LayoutInflater inflater;
    LinearLayout emptyView;

    ArrayList<MonthAccount> monthAccounts = new ArrayList<>();
    MonthlyListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        adapter = new MonthlyListAdapter(inflater, monthAccounts);
        this.inflater = inflater;



        return inflater.inflate(R.layout.frag_account_monthly, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        emptyView = view.findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MonthAccount ma = (MonthAccount) adapter.getItem(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(ma.getWhen());
        View v = inflater.inflate(R.layout.dialog_monthly, null);
        initDialog(v, ma);
        builder.setView(v);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void initDialog(View v, MonthAccount ma){
        final ListView listSI = v.findViewById(R.id.listview_static_income);
        final ListView listFI = v.findViewById(R.id.listview_float_income);
        final ListView listSE= v.findViewById(R.id.listview_static_expense);
        final ListView listFE= v.findViewById(R.id.listview_float_expense);

        ToggleButton toggleSI =v.findViewById(R.id.toggle_static_income);
        toggleSI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listSI.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
        ToggleButton toggleFI =v.findViewById(R.id.toggle_float_income);
        toggleFI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listFI.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
        ToggleButton toggleSE =v.findViewById(R.id.toggle_static_expense);
        toggleSE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listSE.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
        ToggleButton toggleFE =v.findViewById(R.id.toggle_float_expense);
        toggleFE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listFE.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        CurrentListAdapter adapterSI = new CurrentListAdapter(inflater, ma.getStaticIncomes());
        CurrentListAdapter adapterFI = new CurrentListAdapter(inflater, ma.getFloatIncomes());
        CurrentListAdapter adapterSE = new CurrentListAdapter(inflater, ma.getStaticExpense());
        CurrentListAdapter adapterFE = new CurrentListAdapter(inflater, ma.getFloatExpense());

        listSI.setAdapter(adapterSI);
        listFI.setAdapter(adapterFI);
        listSE.setAdapter(adapterSE);
        listFE.setAdapter(adapterFE);
        setListviewHeight(listSI, adapterSI);
        setListviewHeight(listFI, adapterFI);
        setListviewHeight(listSE, adapterSE);
        setListviewHeight(listFE, adapterFE);

        TextView tvRent = v.findViewById(R.id.tv_rent);
        tvRent.setText(ma.getRent()+"");
    }

    private void setListviewHeight(ListView list, CurrentListAdapter adapter){
        ViewGroup.LayoutParams params = list.getLayoutParams();
        int h = (int) (adapter.getCount() * dpToPx(30, getContext()));
        params.height = h;
        list.setLayoutParams(params);
    }

    public static float dpToPx(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }
}
