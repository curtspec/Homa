package com.curtspec2018.homa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DayChangeReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, MonthChangeService.class));
    }
}
