package com.curtspec2018.homa;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.curtspec2018.homa.databinding.FragMfBinding;
import com.riontech.calendar.dao.EventData;
import com.riontech.calendar.dao.dataAboutDate;

import java.util.ArrayList;

public class MFFragment extends Fragment {

    FragMfBinding b;
    FloatingActionButton fab;
    LayoutInflater inflater;

    int year, month, day;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b= DataBindingUtil.inflate(inflater, R.layout.frag_mf, container, false);
        b.setFrag(this);
        this.inflater = inflater;
        return inflater.inflate(R.layout.frag_mf, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        //=========================================================test data set=============================================================
        ArrayList<EventData> eventDataList  = new ArrayList<>();

        EventData eventData = new EventData();
        ArrayList<dataAboutDate> dataAboutDates = new ArrayList<>();

        dataAboutDate dates = new dataAboutDate();
        dates.setTitle("Test title");
        dates.setSubject("today's work to do");
        dates.setSubmissionDate("submission Date? what it is?");
        dataAboutDates.add(dates);

        eventData.setSection("what's the section?");
        eventData.setData(dataAboutDates);

        eventDataList.add(eventData);

        b.calendar.addAnEvent("2019-02-01", 1, eventDataList);
        //=========================================================test data set=============================================================


        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        b.calendar.invalidate();
    }
}
