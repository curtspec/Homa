package com.curtspec2018.homa;

import android.annotation.SuppressLint;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarUtils;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.curtspec2018.homa.adapter.MFFragListAdapter;
import com.curtspec2018.homa.vo.Schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MFFragment extends Fragment implements OnCalendarPageChangeListener {

    FloatingActionButton fab;
    CalendarView calendar;
    ListView listView;
    TextView tvDate;

    LayoutInflater inflater;

    Calendar today;
    Calendar currentDay;
    ArrayList<Schedule> schedules = new ArrayList<>();
    MFFragListAdapter adapter;

    ArrayList<Schedule> addable = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        today = Calendar.getInstance();
        currentDay = today;
        adapter = new MFFragListAdapter(getLayoutInflater(), schedules);
        setData(today);
        return inflater.inflate(R.layout.frag_mf, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        calendar = view.findViewById(R.id.calendar);
        fab = view.findViewById(R.id.fab);
        tvDate = view.findViewById(R.id.tv_date);
        tvDate.setText(new SimpleDateFormat("yyyy.MM.dd").format(new Date(today.getTimeInMillis())));

        listView = view.findViewById(R.id.listview);
        listView.setAdapter(adapter);
        setListviewHeight(listView, adapter);

        Drawable d = CalendarUtils.getDrawableText(getContext(), today.get(Calendar.DAY_OF_MONTH) + "",
                Typeface.DEFAULT, R.color.White, 7);
        fab.setImageDrawable(d);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    calendar.setDate(today);
                    currentDay = today;
                    tvDate.setText(new SimpleDateFormat("yyyy.MM.dd").format(new Date(currentDay.getTimeInMillis())));
                    checkMatch();
                    setData(currentDay);
                } catch (OutOfDateRangeException e) {
                    return;
                }
            }
        });

        calendar.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                currentDay = eventDay.getCalendar();
                tvDate.setText(new SimpleDateFormat("yyyy.MM.dd").format(new Date(currentDay.getTimeInMillis())));
                checkMatch();
                setData(currentDay);
            }
        });
        calendar.setOnPreviousPageChangeListener(this);
        calendar.setOnForwardPageChangeListener(this);

        try { calendar.setDate(today); } catch (OutOfDateRangeException e) { e.printStackTrace(); }
        super.onViewCreated(view, savedInstanceState);
    }

    private void setListviewHeight(ListView list, MFFragListAdapter adapter){
        ViewGroup.LayoutParams params = list.getLayoutParams();
        int h = (int) (adapter.getCount() * G.dpToPx(85, getContext()));
        params.height = h;
        list.setLayoutParams(params);
    }

    private void checkMatch(){
        boolean isSameDay = equalDay(today, currentDay);
        if (isSameDay) {
            fabGone();
        }
        else {
            fabShow();
        }
    }

    private void fabGone(){
        if (fab.getVisibility() == View.GONE) return;
        Animation zoomOut = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_out);
        zoomOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {  }
            @SuppressLint("RestrictedApi")
            @Override
            public void onAnimationEnd(Animation animation) {
                fab.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        fab.startAnimation(zoomOut);
    }

    private void fabShow(){
        if (fab.getVisibility() == View.VISIBLE) return;
        Animation zoomIn = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        zoomIn.setAnimationListener(new Animation.AnimationListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onAnimationStart(Animation animation) {
                fab.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animation animation) { }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        fab.startAnimation(zoomIn);
    }

    private boolean equalDay(Calendar A, Calendar B){
        if (A.get(Calendar.YEAR) == B.get(Calendar.YEAR) && A.get(Calendar.MONTH) == B.get(Calendar.MONTH)
           && A.get(Calendar.DAY_OF_MONTH) == B.get(Calendar.DAY_OF_MONTH)) return true;
        else return false;
    }

    private boolean equalMonth(Calendar A, Calendar B){
        if (A.get(Calendar.YEAR) == B.get(Calendar.YEAR) && A.get(Calendar.MONTH) == B.get(Calendar.MONTH)) return true;
        else return false;
    }

    @Override
    public void onChange() {
        Calendar pageCalendar = calendar.getCurrentPageDate();
        if (equalMonth(pageCalendar, today) && equalDay(currentDay, today)) fabGone();
        else fabShow();
    }

    private void setData(Calendar selected){
        for (Schedule t: addable){
            if (equalDay(t.getDate(), selected)) schedules.add(t);
        }
        adapter.notifyDataSetChanged();
    }
}
