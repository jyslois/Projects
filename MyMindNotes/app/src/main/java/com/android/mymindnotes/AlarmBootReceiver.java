package com.android.mymindnotes;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;

public class AlarmBootReceiver extends BroadcastReceiver {
    SharedPreferences timeSave;

    @Override
    public void onReceive(Context context, Intent intent) {
        timeSave = context.getSharedPreferences("time", Activity.MODE_PRIVATE);

        // 부팅 시 알람 설정을 위한 코드
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.e("MyChecker:BootCheck", "BootCheck : " + "여기까지 무사히 들어왔다.");
            // Set the alarm here.
            if (timeSave.getLong("time", 0) != 0) {
                // 저장된 알람이 있을 경우에만 reset alarm
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(timeSave.getLong("time", 0));
                AlarmSetting.setAlarm(calendar, context);
                Log.e("MyChecker:BootCheck", "BootCheck : " + calendar.getTime());
            }
        }
    }
}
