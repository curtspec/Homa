package com.curtspec2019.homa.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.curtspec2019.homa.R;
import com.curtspec2019.homa.vo.MonthAccount;

import java.util.ArrayList;

public class MonthlyListAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<MonthAccount> monthAccounts;

    public MonthlyListAdapter(LayoutInflater inflater, ArrayList<MonthAccount> monthAccounts) {
        this.inflater = inflater;
        this.monthAccounts = monthAccounts;
    }

    @Override
    public int getCount() {
        return monthAccounts.size();
    }

    @Override
    public Object getItem(int position) {
        return monthAccounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_account_monthly, parent, false);
        }

        TextView tvPeriod = convertView.findViewById(R.id.tv_period);
        TextView tvIncom = convertView.findViewById(R.id.tv_income);
        TextView tvExpense = convertView.findViewById(R.id.tv_expense);
        TextView tvPure = convertView.findViewById(R.id.tv_pure);


        tvPeriod.setText(monthAccounts.get(position).getWhen());
        tvIncom.setText(monthAccounts.get(position).getTotalIncome()+"");
        tvExpense.setText(monthAccounts.get(position).getTotalExpense()+"");
        tvPure.setText(monthAccounts.get(position).getPure()+"");

        return convertView;
    }
}
