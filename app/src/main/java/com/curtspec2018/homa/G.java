package com.curtspec2018.homa;

import com.curtspec2018.homa.vo.MonthAccount;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class G {

    public static ArrayList<MonthAccount> monthAccounts;

    public static String divisionThousand(long value){
        DecimalFormat format = new DecimalFormat("###,###");
        return format.format(value);
    }

}
