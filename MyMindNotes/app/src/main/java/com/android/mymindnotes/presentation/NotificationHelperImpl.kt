package com.android.mymindnotes.presentation

import android.app.Notification
import android.content.ContextWrapper
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import android.app.NotificationChannel
import androidx.core.app.NotificationCompat
import android.content.Intent
import com.android.mymindnotes.presentation.ui.RecordMindChoice
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import com.android.mymindnotes.R
import com.android.mymindnotes.domain.NotificationHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationHelperImpl @Inject constructor(
    @ApplicationContext context: Context
) : ContextWrapper(context), NotificationHelper {

    companion object {
        const val channelId = "Alarm"
        const val channelName = "일기 알람"
    }

    private var manager: NotificationManager? = null

    init {
        // 오레오보다 같거나 크면
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }

    // 채널 생성
    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun createChannel() {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        channel.enableLights(true)
        channel.lightColor = Color.parseColor("#e0037a")
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        getManager().createNotificationChannel(channel)
    }

    // NotificationManager 생성
    override fun getManager(): NotificationManager {
        if (manager == null) {
            manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        return manager!!
    }

    override fun getChannelNotification(): NotificationCompat.Builder {
        // 알림창 클릭했을 때 넘어가는 페이지 세팅
        val intent1 = Intent(this, RecordMindChoice::class.java)
        val pIntent = PendingIntent.getActivity(this, 1, intent1, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.notification_icon)
            // notification 이미지 배경 설정
            .setColor(Color.parseColor("#e0037a"))
            .setContentTitle("오늘 하루는 어떠셨나요?")
            .setContentText("나의 마음을 기록해 봐요")
            // 알림창 클릭했을 때 MainPage 나타나기
            .setContentIntent(pIntent)
    }
}