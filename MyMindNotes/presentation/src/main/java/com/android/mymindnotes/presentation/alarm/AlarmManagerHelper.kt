package com.android.mymindnotes.presentation.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import com.android.mymindnotes.domain.alarmInterface.AlarmManagerHelperInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AlarmManagerHelper @Inject constructor(
    @ApplicationContext private val context: Context
): ContextWrapper(context), AlarmManagerHelperInterface {

    override fun setAlarm(calendar: java.util.Calendar) {
        // 알람 매니저 선언
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // Receiver 설정
        val intent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            PendingIntent.getBroadcast(context.applicationContext, 1, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        } else {
            PendingIntent.getBroadcast(context.applicationContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        // 마시멜로(6.0 / api23) 버전부터 도즈모드가 도입되면서 기존에 사용하던 setExact, set 메소드를 사용했을 경우 도즈모드에 진입한 경우 알람이 울리지 않는다.
        // 'setExactAndAllowWhileIdle'은 도즈모드에서도 잠깐 깨어나 알람을 울리게 한다
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }

    override fun stopAlarm() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            PendingIntent.getBroadcast(context.applicationContext, 1, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        } else {
            PendingIntent.getBroadcast(context.applicationContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

}