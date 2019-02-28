package com.curtspec2018.homa;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.curtspec2018.homa.vo.Building;
import com.curtspec2018.homa.vo.MonthAccount;
import com.curtspec2018.homa.vo.Schedule;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class G {

    private static Building currentBuilding;
    private static ArrayList<Building> buildings = new ArrayList<>();
    private static ArrayList<Schedule> memos = new ArrayList<>();

    public static ArrayList<Schedule> getMemos(){
        return memos;
    }

    public static ArrayList<Schedule> getAddableSchedules(){
        ArrayList<Schedule> addable = new ArrayList<>();
        addable.addAll(memos);
        if (currentBuilding != null) addable.addAll(currentBuilding.getSchedulesFromTenant());
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
