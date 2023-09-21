package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.domain.alarmInterface.AlarmManagerHelperInterface
import javax.inject.Inject

class StopAlarmUseCase @Inject constructor(
    private val alarmManagerHelper: AlarmManagerHelperInterface
) {
    operator fun invoke() {
        alarmManagerHelper.stopAlarm()
    }

}