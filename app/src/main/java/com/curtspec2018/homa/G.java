package com.curtspec2018.homa;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.curtspec2018.homa.vo.MonthAccount;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class G {


    public static ArrayList<MonthAccount> monthAccounts;

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