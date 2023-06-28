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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
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
    private val stopAlarmUseCase: StopAlarmUseCase,


    ): ViewModel() {

    // ui 상태를 나타내는 sealed 클래스
    sealed class AlarmSettingUiState {
        object Loading : AlarmSettingUiState()
        object AlarmSwitchedOn : AlarmSettingUiState()
        data class AlarmSwitchedOnWithTime(val time: String) : AlarmSettingUiState()
        object AlarmSwitchedOff : AlarmSettingUiState()
    }

    // uiState
    private val _uiState = MutableStateFlow<AlarmSettingUiState>(AlarmSettingUiState.Loading)
    val uiState: StateFlow<AlarmSettingUiState> = _uiState



    suspend fun alarmSwitchChanged(isChecked: Boolean) {
        val time = getAlarmTimeUseCase.getTime().first() // 저장한 알람 시간 불러오기

        // switch가 on만 됐을 때 (지정한 알람 시간이 없을 때)
        if(isChecked && time.isNullOrEmpty()) {
            saveAlarmStateUseCase(true) // 상태 저장
            _uiState.value = AlarmSettingUiState.AlarmSwitchedOn
            // 이미 지정한 알람 시간이 있을 때
        } else if(isChecked && !time.isNullOrEmpty()) {
            saveAlarmStateUseCase(true)
            _uiState.value = AlarmSettingUiState.AlarmSwitchedOnWithTime(time)
            // Off일 때
        } else {
            clearAlarmSettingsUseCase()
            clearTimeSettingsUseCase()
            stopAlarmUseCase()
            _uiState.value = AlarmSettingUiState.AlarmSwitchedOff
        }
    }

    suspend fun getHour(): Int {
        return getAlarmTimeUseCase.getHour().first()
    }

    suspend fun getMinute(): Int {
        return getAlarmTimeUseCase.getMinute().first()
    }

    fun alarmDialogueSet(time: String, hourOfDay: Int, min: Int, minute: Int) {
        // store Selected time
        viewModelScope.launch {
            saveAlarmTimeUseCase.saveTime(time)
            saveAlarmTimeUseCase.saveHour(hourOfDay)
            saveAlarmTimeUseCase.saveMinute(min)

            // // Delete time in sharedPrefenreces to reset alarm on boot
            clearTimeSettingsUseCase()
            // Unset the alarm that was originally set.
            stopAlarmUseCase()

            // Set the alarm to the selected time.
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)

            // if earlier than the current time
            if (calendar.before(Calendar.getInstance())) {
                // set to the next day
                calendar.add(Calendar.DATE, 1)
            }
            saveRebootAlarmTimeUseCase(calendar.timeInMillis)
            setAlarmUseCase(calendar)
        }

    }

    init {
        viewModelScope.launch {

            // 알람을 세팅한 적이 있으면, switch를 on으로 해 주기
            if (getAlarmStateUseCase().first()) {
                alarmSwitchChanged(true)
            }
        }

    }
}