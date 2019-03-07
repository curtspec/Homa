package com.curtspec2018.homa;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import com.curtspec2018.homa.vo.Building;
import com.curtspec2018.homa.vo.MonthAccount;
import com.curtspec2018.homa.vo.Schedule;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class G {

    public static final String SERVER_URL = "http://curtspec2019.dothome.co.kr/homa/";
    private static Building currentBuilding;
    private static ArrayList<Building> buildings = new ArrayList<>();
    private static ArrayList<Schedule> memos = new ArrayList<>();
    public static ArrayList<Schedule> getMemos(){
        return memos;
    }

    private static String id;

    public static ArrayList<Schedule> getAddableSchedules(){
        ArrayList<Schedule> addable = new ArrayList<>();
        addable.addAll(memos);
        if (currentBuilding != null) addable.addAll(currentBuilding.getSchedulesFromTenant());
        for (Schedule s : addable){
            Log.i("ErrorTrace", new SimpleDateFormat("yyyy.MM.dd").format(new Date(s.getDate().getTimeInMillis())) + "\n");
        }
        return addable;
    }

    public static ArrayList<Building> getBuildings(){
        return buildings;
    }

    public static void setCurrentBuilding(Building building){
        currentBuilding = building;
    }

    public static Building getCurrentBuilding(){
        return currentBuilding;
    }

    public static void setBuildings(ArrayList<Building> buildings) {
        G.buildings = buildings;
    }

    public static void setMemos(ArrayList<Schedule> memos) {
        G.memos = memos;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        G.id = id;
    }

    //==================================== method ==============================================
    public static String divisionThousand(long value){
        DecimalFormat format = new DecimalFormat("###,###");
        return format.format(value);
    }

    public static float dpToPx(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }
}
