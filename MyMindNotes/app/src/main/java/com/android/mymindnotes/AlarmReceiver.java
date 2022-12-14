package com.android.mymindnotes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);

        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        //  알림창 클릭했을 때 알람창은 사라지기
        nb.setAutoCancel(true);

        // 알람 호출
        notificationHelper.getManager().notify(1, nb.build());
    }
}
