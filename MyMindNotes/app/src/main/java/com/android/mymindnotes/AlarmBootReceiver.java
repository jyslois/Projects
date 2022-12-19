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
    SharedPreferences.Editor timeSaveEdit;

    @Override
    public void onReceive(Context context, Intent intent) {
        timeSave = context.getSharedPreferences("time", Activity.MODE_PRIVATE);
        timeSaveEdit = timeSave.edit();

        // 부팅 시 알람 설정을 위한 코드
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            if (timeSave.getLong("time", 0) != 0) {
                // 저장된 알람이 있을 경우에만 reset alarm
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(timeSave.getLong("time", 0));

                // 현재 시간보다 이전이면
                if (calendar.before(Calendar.getInstance())) {
                    // 다음 날로 설정
                    calendar.add(Calendar.DATE, 1);
                }

                AlarmSetting.setAlarm(calendar, context);
                // sharedPrefenreces에 calendar의 time 저장
                timeSaveEdit.putLong("time", calendar.getTimeInMillis());
                timeSaveEdit.commit();
            }
        }
    }
}
