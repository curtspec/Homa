package com.curtspec2018.homa.account;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.curtspec2018.homa.R;
import com.curtspec2018.homa.databinding.FragAccountEntireBinding;
import com.curtspec2018.homa.databinding.FragAccountMonthlyBinding;

public class EntireFragment extends Fragment {

    FragAccountEntireBinding b;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.frag_account_entire, container, false);
        return inflater.inflate(R.layout.frag_account_entire, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
