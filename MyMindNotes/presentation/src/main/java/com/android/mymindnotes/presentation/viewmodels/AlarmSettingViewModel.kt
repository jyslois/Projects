package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.alarm.GetAlarmStateUseCase
import com.android.mymindnotes.domain.usecases.alarm.GetAlarmTimeUseCase
import com.android.mymindnotes.domain.usecases.alarm.SaveAlarmStateUseCase
import com.android.mymindnotes.domain.usecases.alarm.SaveAlarmTimeUseCase
import com.android.mymindnotes.domain.usecases.alarm.SaveRebootAlarmTimeUseCase
import com.android.mymindnotes.domain.usecases.alarm.SetAlarmUseCase
import com.android.mymindnotes.domain.usecases.alarm.StopAlarmUseCase
import com.android.mymindnotes.domain.usecases.userinfo.ClearAlarmSettingsUseCase
import com.android.mymindnotes.domain.usecases.userinfo.ClearTimeSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmSettingViewModel @Inject constructor(
    private val clearAlarmSettingsUseCase: ClearAlarmSettingsUseCase,
    private var clearTimeSettingsUseCase: ClearTimeSettingsUseCase,

    private val getAlarmStateUseCase: GetAlarmStateUseCase,
    private val saveAlarmStateUseCase: SaveAlarmStateUseCase,
    private val getAlarmTimeUseCase: GetAlarmTimeUseCase,
    private val saveAlarmTimeUseCase: SaveAlarmTimeUseCase,

    private val saveRebootAlarmTimeUseCase: SaveRebootAlarmTimeUseCase,
    private val setAlarmUseCase: SetAlarmUseCase,
    private val stopAlarmUseCase: StopAlarmUseCase
): ViewModel() {
    // Alarm

    // alarm 상태 저장 플로우
    private val _alarmState = MutableSharedFlow<Boolean>()
    val alarmState = _alarmState.asSharedFlow()


    // time 저장 플로우
    private val _time = MutableSharedFlow<String?>()
    val time = _time.asSharedFlow()

    // alarm 상태 저장하기
    suspend fun saveAlarmState(state: Boolean) {
        saveAlarmStateUseCase(state)
    }

    // time 저장하기
    suspend fun saveTime(time: String) {
        saveAlarmTimeUseCase.saveTime(time)
    }

    // 시간 저장하기
    suspend fun saveHour(hour: Int) {
        saveAlarmTimeUseCase.saveHour(hour)
    }

    // 시간 가져오기
    suspend fun getHour(): Flow<Int> {
        return getAlarmTimeUseCase.getHour()
    }

    // 분 저장하기
    suspend fun saveMinute(minute: Int) {
        saveAlarmTimeUseCase.saveMinute(minute)
    }

    // 분 가져오기
    suspend fun getMinute(): Flow<Int> {
        return getAlarmTimeUseCase.getMinute()
    }

    // Reboot 위한 시간 저장하기
    suspend fun saveRebootTime(time: Long) {
        saveRebootAlarmTimeUseCase(time)
    }


    // Clear Preferences
    // Clear Alarm SharedPreferences
    suspend fun clearAlarmSettings() {
        clearAlarmSettingsUseCase()
    }

    // Clear Time SharedPreferences
    suspend fun clearTimeSharedPreferences() {
        clearTimeSettingsUseCase()
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

    fun setAlarm(calendar: java.util.Calendar) {
        setAlarmUseCase(calendar)
    }

    fun stopAlarm() {
        stopAlarmUseCase()
    }

    init {
        viewModelScope.launch {
            // 알람 상태 collect & emit
            getAlarmStateUseCase().collect {
                _alarmState.emit(it)
            }

            // 시간 collect & emit
            getAlarmTimeUseCase.getTime().collect {
                _time.emit(it)
            }
        }
    }

}