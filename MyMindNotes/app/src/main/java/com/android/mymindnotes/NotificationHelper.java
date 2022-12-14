package com.android.mymindnotes;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {

    static final String channelId = "Alarm";
    static final String channelName = "나의 마음 일지";

    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);

        //오레오보다 같거나 크면
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
        }
    }

    //채널 생성
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannel(){
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.setLightColor(Color.parseColor("#C3BE9F98"));
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel);
    }

    // NotificationManager 생성
    public NotificationManager getManager(){
        if(manager == null){
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return manager;
    }

    public NotificationCompat.Builder getChannelNotification(){

        Intent intent1 = new Intent(this, MainPage.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 1, intent1, PendingIntent.FLAG_IMMUTABLE);
        // builder의 build() 함수로 Notification 객체 생성

        return new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("나의 마음 일지")
                .setContentText("오늘의 마음을 기록해 봐요")
                .setContentIntent(pIntent);

    }
}
