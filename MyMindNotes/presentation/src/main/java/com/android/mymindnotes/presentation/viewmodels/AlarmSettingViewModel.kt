package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.alarm.SuccessResult
import com.android.mymindnotes.domain.usecases.alarm.ChangeAlarmSwitchUseCase
import com.android.mymindnotes.domain.usecases.alarm.GetAlarmHourUseCase
import com.android.mymindnotes.domain.usecases.alarm.GetAlarmMinuteUseCase
import com.android.mymindnotes.domain.usecases.alarm.GetAlarmStateUseCase
import com.android.mymindnotes.domain.usecases.alarm.GetAlarmTimeUseCase
import com.android.mymindnotes.domain.usecases.alarm.SaveAlarmHourUseCase
import com.android.mymindnotes.domain.usecases.alarm.SaveAlarmMinuteUseCase
import com.android.mymindnotes.domain.usecases.alarm.SaveAlarmStateUseCase
import com.android.mymindnotes.domain.usecases.alarm.SaveAlarmTimeUseCase
import com.android.mymindnotes.domain.usecases.alarm.SaveRebootAlarmTimeUseCase
import com.android.mymindnotes.domain.usecases.alarm.SetAlarmUseCase
import com.android.mymindnotes.domain.usecases.alarm.StopAlarmUseCase
import com.android.mymindnotes.domain.usecases.userInfo.ClearAlarmSettingsUseCase
import com.android.mymindnotes.domain.usecases.userInfo.ClearTimeSettingsUseCase
import com.bumptech.glide.Glide.init
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

    private val changeAlarmSwitchUseCase: ChangeAlarmSwitchUseCase,

    private val getAlarmStateUseCase: GetAlarmStateUseCase,
    private val saveAlarmStateUseCase: SaveAlarmStateUseCase,
    private val getAlarmTimeUseCase: GetAlarmTimeUseCase,
    private val getAlarmHourUseCase: GetAlarmHourUseCase,
    private val getAlarmMinuteUseCase: GetAlarmMinuteUseCase,
    private val saveAlarmTimeUseCase: SaveAlarmTimeUseCase,
    private val saveAlarmHourUseCase: SaveAlarmHourUseCase,
    private val saveAlarmMinuteUseCase: SaveAlarmMinuteUseCase,

    private val saveRebootAlarmTimeUseCase: SaveRebootAlarmTimeUseCase,
    private val setAlarmUseCase: SetAlarmUseCase,
    private val stopAlarmUseCase: StopAlarmUseCase,
    ): ViewModel() {

    // ui 상태를 나타내는 sealed 클래스
    sealed class AlarmSettingUiState {
        object Loading : AlarmSettingUiState()
        object AlarmSwitchedOn : AlarmSettingUiState()
        data class AlarmSwitchedOnWithTime(val time: String, val hour: Int, val minute: Int) : AlarmSettingUiState()
        object AlarmSwitchedOff : AlarmSettingUiState()
    }

    // uiState
    private val _uiState = MutableStateFlow<AlarmSettingUiState>(AlarmSettingUiState.Loading)
    val uiState: StateFlow<AlarmSettingUiState> = _uiState



    suspend fun alarmSwitchChanged(isChecked: Boolean) {

        changeAlarmSwitchUseCase(isChecked).collect { successResult ->
            when (successResult) {
                is SuccessResult.AlarmSwitchedOn -> {
                    _uiState.value = AlarmSettingUiState.AlarmSwitchedOn
                }
                is SuccessResult.AlarmTime -> {
                    _uiState.value = AlarmSettingUiState.AlarmSwitchedOnWithTime(
                        successResult.time,
                        successResult.hour,
                        successResult.minute
                    )
                }
                is SuccessResult.AlarmSwitchedOff -> {
                    _uiState.value = AlarmSettingUiState.AlarmSwitchedOff
                }
            }
        }
    }

    fun alarmDialogueSet(time: String, hourOfDay: Int, min: Int, minute: Int) {
        // store Selected time
        viewModelScope.launch {
            saveAlarmTimeUseCase(time)
            saveAlarmHourUseCase(hourOfDay)
            saveAlarmMinuteUseCase(min)

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

            _uiState.value = AlarmSettingViewModel.AlarmSettingUiState.AlarmSwitchedOnWithTime(time, hourOfDay, minute)
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