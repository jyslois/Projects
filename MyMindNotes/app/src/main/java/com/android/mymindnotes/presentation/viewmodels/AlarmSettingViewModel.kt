package com.android.mymindnotes.presentation.viewmodels

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecase.AlarmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmSettingViewModel @Inject constructor(
    private val alarmUseCase: AlarmUseCase
): ViewModel() {
    // Alarm

    // alarm 상태 저장 플로우
    private val _alarmState = MutableSharedFlow<Boolean>()
    val alarmState = _alarmState.asSharedFlow()

    // time 가져오기
    suspend fun getTime(): Flow<String?> {
        return alarmUseCase.getTime()
    }

    // time 저장 플로우
    private val _time = MutableSharedFlow<String?>()
    val time = _time.asSharedFlow()

    // alarm 상태 저장하기
    suspend fun saveAlarmState(state: Boolean) {
        alarmUseCase.saveAlarmState(state)
    }

    // time 저장하기
    suspend fun saveTime(time: String) {
        alarmUseCase.saveTime(time)
    }

    // 시간 저장하기
    suspend fun saveHour(hour: Int) {
        alarmUseCase.saveHour(hour)
    }

    // 시간 가져오기
    suspend fun getHour(): Flow<Int> {
        return alarmUseCase.getHour()
    }

    // 분 저장하기
    suspend fun saveMinute(minute: Int) {
        alarmUseCase.saveMinute(minute)
    }

    // 분 가져오기
    suspend fun getMinute(): Flow<Int> {
        return alarmUseCase.getMinute()
    }

    // Reboot 위한 시간 저장하기
    suspend fun saveRebootTime(time: Long) {
        alarmUseCase.saveRebootTime(time)
    }


    // Clear Preferences
    // Clear Alarm SharedPreferences
    suspend fun clearAlarmSharedPreferences() {
        alarmUseCase.clearAlarmSharedPreferences()
    }

    // Clear Time SharedPreferences
    suspend fun clearTimeSharedPreferences() {
        alarmUseCase.clearTimeSharedPreferences()
    }

    // 클릭 이벤트 감지
    // 알람 스위치 바뀔 때 이벤트 감지 플로우
    private val _alarmSwitch = MutableSharedFlow<Boolean>()
    val alarmSwitch = _alarmSwitch.asSharedFlow()

    suspend fun changeAlarmSwitch(isChecked: Boolean) {
        _alarmSwitch.emit(isChecked)
    }

    // setTimeButtton 클릭 이벤트 감지 플로우
    private val _setTimeButton = MutableSharedFlow<Boolean>()
    val setTimeButton = _setTimeButton.asSharedFlow()

    suspend fun clickSetTimeButton() {
        _setTimeButton.emit(true)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun setAlarm(calendar: java.util.Calendar, context: Context) {
        alarmUseCase.setAlarm(calendar, context)
    }

    fun stopAlarm(context: Context) {
        alarmUseCase.stopAlarm(context)
    }

    init {
        viewModelScope.launch {
            // 알람 상태 collect & emit
            alarmUseCase.getAlarmState().collect {
                _alarmState.emit(it)
            }

            // 시간 collect & emit
            alarmUseCase.getTime().collect {
                _time.emit(it)
            }
        }
    }

}