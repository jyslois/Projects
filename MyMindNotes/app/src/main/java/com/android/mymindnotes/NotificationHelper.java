package com.android.mymindnotes;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

public class NotificationHelper extends ContextWrapper {

    static final String channelId = "Alarm";
    static final String channelName = "일기 알람";

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
        channel.setLightColor(Color.parseColor("#e0037a"));
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
        // 알림창 클릭했을 때 넘어가는 페이지 세팅
        Intent intent1 = new Intent(this, MainPage.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 1, intent1, PendingIntent.FLAG_IMMUTABLE);


        return new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.notification_icon)
                // notification 이미지 배경 설정
                .setColor(Color.parseColor("#e0037a"))
                .setContentTitle("나의 마음 일지")
                .setContentText("오늘의 마음을 기록해 봐요")
                // 림창 클릭했을 때 MainPage 나타나기
                .setContentIntent(pIntent);
    }
}
