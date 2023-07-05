package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.alarm.ChangeAlarmSwitchUseCase
import com.android.mymindnotes.domain.usecases.alarm.GetAlarmStateUseCase
import com.android.mymindnotes.domain.usecases.alarm.SetAlarmDialogueUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmSettingViewModel @Inject constructor(
    private val changeAlarmSwitchUseCase: ChangeAlarmSwitchUseCase,
    private val setAlarmDialogueUseCase: SetAlarmDialogueUseCase,
    private val getAlarmStateUseCase: GetAlarmStateUseCase,
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

        changeAlarmSwitchUseCase(isChecked).collect {
            when (it) {
                is ChangeAlarmSwitchUseCase.SuccessResult.AlarmSwitchedOn -> {
                    _uiState.value = AlarmSettingUiState.AlarmSwitchedOn
                }
                is ChangeAlarmSwitchUseCase.SuccessResult.AlarmTime -> {
                    _uiState.value = AlarmSettingUiState.AlarmSwitchedOnWithTime(
                        it.time,
                        it.hour,
                        it.minute
                    )
                }
                is ChangeAlarmSwitchUseCase.SuccessResult.AlarmSwitchedOff -> {
                    _uiState.value = AlarmSettingUiState.AlarmSwitchedOff
                }
            }
        }
    }

    fun alarmDialogueSet(time: String, hourOfDay: Int, min: Int, minute: Int) {
        viewModelScope.launch {
            setAlarmDialogueUseCase(time, hourOfDay, min, minute)
            _uiState.value = AlarmSettingUiState.AlarmSwitchedOnWithTime(time, hourOfDay, minute)
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