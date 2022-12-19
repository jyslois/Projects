package com.android.mymindnotes;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    // 부팅시 알람 재설정을 위한 sharedpreferences
    SharedPreferences timeSave;
    SharedPreferences.Editor timeSaveEdit;

    @Override
    public void onReceive(Context context, Intent intent) {
        timeSave = context.getSharedPreferences("time", Activity.MODE_PRIVATE);
        timeSaveEdit = timeSave.edit();

        NotificationHelper notificationHelper = new NotificationHelper(context);

        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        //  알림창 클릭했을 때 알람창은 사라지기
        nb.setAutoCancel(true);
        // 기본 사운드 출력
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        nb.setSound(alarmSound);

        notificationHelper.getManager().notify(1, nb.build());
        AlarmSetting.stopAlarm(context);
        // 부팅시 알람 재설정을 위한 sharedPrefenreces의 시간 삭제하기
        timeSaveEdit.clear();
        timeSaveEdit.commit();
        Log.e("MyChecker:TimeCheck", "TimeCancel : " + timeSave.getLong("time", 0));

        // 알람 재호출 (반복 알람 세팅을 위해)
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        AlarmSetting.setAlarm(calendar, context);
        // 부팅시 알람 재설정을 위해 sharedPrefenreces에 calendar의 time 저장
        timeSaveEdit.putLong("time", calendar.getTimeInMillis());
        timeSaveEdit.commit();
    }
}
