package com.android.mymindnotes.domain.usecase

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.android.mymindnotes.domain.AlarmReceiver
import com.android.mymindnotes.domain.repositoryinterfaces.MemberSharedPreferencesRepository
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class AlarmUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository,
    private val alarmReceiver: AlarmReceiver
) {

    // Alarm
    // alarm 상태 가져오기
    suspend fun getAlarmState(): Flow<Boolean> = repository.getAlarmState()

    // alarm 상태 저장하기
    suspend fun saveAlarmState(state: Boolean) = repository.saveAlarmState(state)

    // time 가져오기
    suspend fun getTime(): Flow<String?> = repository.getTime()

    // time 저장하기
    suspend fun saveTime(time: String) = repository.saveTime(time)

    // 시간 불러오기
    suspend fun getHour(): Flow<Int> = repository.getHour()

    // 시간 저장하기
    suspend fun saveHour(hour: Int) = repository.saveHour(hour)

    // 분 불러오기
    suspend fun getMinute(): Flow<Int> = repository.getMinute()

    // 분 저장하기
    suspend fun saveMinute(minute: Int) = repository.saveMinute(minute)

    // Reboot 시간 가져오기
    suspend fun getRebootTime(): Flow<Long> = repository.getRebootTime()

    // Reboot 위한 시간 저장하기
    suspend fun saveRebootTime(time: Long) = repository.saveRebootTime(time)


    // Clear Preferences
    // Clear Alarm SharedPreferences
    suspend fun clearAlarmSharedPreferences() = repository.clearAlarmSharedPreferences()

    // Clear Time SharedPreferences
    suspend fun clearTimeSharedPreferences() = repository.clearTimeSharedPreferences()

    // set Alarm
    @RequiresApi(Build.VERSION_CODES.N)
    fun setAlarm(calendar: java.util.Calendar, context: Context) {

        // 알람 메니져 선언
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Receiver 설정
        val intent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            PendingIntent.getBroadcast(context.applicationContext, 1, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        } else {
            PendingIntent.getBroadcast(context.applicationContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        // 마시멜로(6.0 / api23) 버전부터 도즈모드가 도입되면서 기존에 사용하던 setExact, set 메소드를 사용했을 경우 도즈모드에 진입한 경우 알람이 울리지 않는다.
        // 'setExactAndAllowWhileIdle'은 도즈모드에서도 잠깐 깨어나 알람을 울리게 한다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }

        Log.e("알람 체크", "PendingIntent - Start>> $pendingIntent");
        Log.e("알람 체크", "TimeSet - " + calendar.time);
    }

    // stop Alarm
    fun stopAlarm(context: Context) {
        // 알람 메니져 선언
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, alarmReceiver::class.java)

        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            PendingIntent.getBroadcast(context.applicationContext, 1, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        } else {
            PendingIntent.getBroadcast(context.applicationContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        Log.e("알람 체크", "PendingIntent - Stop>> $pendingIntent");

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

}