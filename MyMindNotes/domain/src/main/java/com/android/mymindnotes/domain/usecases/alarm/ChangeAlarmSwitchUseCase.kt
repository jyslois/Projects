package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.domain.usecases.userInfo.ClearAlarmSettingsUseCase
import com.android.mymindnotes.domain.usecases.userInfo.ClearTimeSettingsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChangeAlarmSwitchUseCase @Inject constructor(
    private val clearAlarmSettingsUseCase: ClearAlarmSettingsUseCase,
    private var clearTimeSettingsUseCase: ClearTimeSettingsUseCase,
    private val saveAlarmStateUseCase: SaveAlarmStateUseCase,
    private val getAlarmTimeUseCase: GetAlarmTimeUseCase,
    private val getAlarmHourUseCase: GetAlarmHourUseCase,
    private val getAlarmMinuteUseCase: GetAlarmMinuteUseCase,
    private val stopAlarmUseCase: StopAlarmUseCase,
) {

    suspend operator fun invoke(isChecked: Boolean): Flow<SuccessResult> = flow {
        val time = getAlarmTimeUseCase().first() // 저장한 알람 시간 불러오기
        val hour = getAlarmHourUseCase().first()
        val minute = getAlarmMinuteUseCase().first()

        val result = when {
            // switch가 on만 됐을 때 (지정한 알람 시간이 없을 때)
            isChecked && time.isNullOrEmpty() -> {
                saveAlarmStateUseCase(true) // 상태 저장
                SuccessResult.AlarmSwitchedOn
            }
            // 이미 지정한 알람 시간이 있을 때
            isChecked && !time.isNullOrEmpty() -> {
                saveAlarmStateUseCase(true)
                SuccessResult.AlarmTime(time, hour, minute)
            }
            // Off일 때
            else -> {
                clearAlarmSettingsUseCase()
                clearTimeSettingsUseCase()
                stopAlarmUseCase()
                SuccessResult.AlarmSwitchedOff
            }
        }

        emit(result)
    }


    sealed class SuccessResult {
        object AlarmSwitchedOn : SuccessResult()
        data class AlarmTime(val time: String, val hour: Int, val minute: Int) : SuccessResult()
        object AlarmSwitchedOff : SuccessResult()
    }

}