package com.android.mymindnotes.domain.usecases

import android.os.Build
import androidx.annotation.RequiresApi
import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import com.android.mymindnotes.domain.alarmInterface.AlarmManagerHelperInterface
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class AlarmUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {

    @Inject
    lateinit var alarmManagerHelper: AlarmManagerHelperInterface

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
    fun setAlarm(calendar: Calendar) {
        alarmManagerHelper.setAlarm(calendar)
    }

    // stop Alarm
    fun stopAlarm() {
        alarmManagerHelper.stopAlarm()
    }

}