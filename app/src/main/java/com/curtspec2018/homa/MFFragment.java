package com.curtspec2018.homa;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarUtils;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;

import java.util.Calendar;

public class MFFragment extends Fragment {

    FloatingActionButton fab;
    CalendarView calendar;

    LayoutInflater inflater;

    Calendar today;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        return inflater.inflate(R.layout.frag_mf, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        today = Calendar.getInstance();
        calendar = view.findViewById(R.id.calendar);
        fab = view.findViewById(R.id.fab);

        Drawable d = CalendarUtils.getDrawableText(getContext(), today.get(Calendar.DAY_OF_MONTH) + "",
                Typeface.DEFAULT, R.color.White, (int) G.dpToPx(5, getContext()));
        fab.setImageDrawable(d);

        try {
            calendar.setDate(today);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        super.onViewCreated(view, savedInstanceState);
    }

}
