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
import com.android.mymindnotes.domain.usecases.userInfo.ClearAlarmSettingsUseCase
import com.android.mymindnotes.domain.usecases.userInfo.ClearTimeSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
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

    // ui 상태를 나타내는 sealed 클래스
    sealed class AlarmSettingUiState {
        data class AlarmState(val isSet: Boolean) : AlarmSettingUiState()
        data class AlarmTime(val time: String?) : AlarmSettingUiState()
        data class AlarmSwitchState(val isChecked: Boolean) : AlarmSettingUiState()
        data class SetTimeButtonState(val isClicked: Boolean) : AlarmSettingUiState()
    }

    // uiState
    private val _uiState = MutableSharedFlow<AlarmSettingUiState>()
    val uiState: SharedFlow<AlarmSettingUiState> = _uiState


    // Alarm

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
    suspend fun clearTimeSettings() {
        clearTimeSettingsUseCase()
    }


    suspend fun changeAlarmSwitch(isChecked: Boolean) {
        _uiState.emit(AlarmSettingUiState.AlarmSwitchState(isChecked))
    }


    suspend fun clickSetTimeButton() {
        _uiState.emit(AlarmSettingUiState.SetTimeButtonState(true))
    }

    fun setAlarm(calendar: java.util.Calendar) {
        setAlarmUseCase(calendar)
    }

    fun stopAlarm() {
        stopAlarmUseCase()
    }

    init {
        viewModelScope.launch {

            val alarmStateFlow = getAlarmStateUseCase().map { AlarmSettingUiState.AlarmState(it) }
            val alarmTimeFlow = getAlarmTimeUseCase.getTime().map { AlarmSettingUiState.AlarmTime(it) }

            merge(alarmStateFlow, alarmTimeFlow).collect {
                _uiState.emit(it)
            }

        }
    }

}