package com.curtspec2019.homa.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.curtspec2019.homa.R;
import com.curtspec2019.homa.vo.Account;

import java.util.ArrayList;

public class CurrentListAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<Account> accounts;

    public CurrentListAdapter(LayoutInflater inflater, ArrayList<Account> accounts) {
        this.inflater = inflater;
        this.accounts = accounts;
    }

    @Override
    public int getCount() {
        return accounts.size();
    }
    @Override
    public Object getItem(int position) {
        return accounts.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = inflater.inflate(R.layout.item_account_current, parent, false);
        TextView tvTitle = convertView.findViewById(R.id.tv_title);
        TextView tvAmount = convertView.findViewById(R.id.tv_amount);
        tvTitle.setText(accounts.get(position).getTitle());
        tvAmount.setText(accounts.get(position).getAmount() + "");
        return convertView;
    }
}
