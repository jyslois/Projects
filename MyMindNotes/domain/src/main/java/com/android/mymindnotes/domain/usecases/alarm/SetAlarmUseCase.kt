package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.domain.alarmInterface.AlarmManagerHelperInterface
import java.util.Calendar
import javax.inject.Inject

class SetAlarmUseCase @Inject constructor(
    private val alarmManagerHelper: AlarmManagerHelperInterface
) {

//    fun setAlarm(calendar: Calendar) {
//        alarmManagerHelper.setAlarm(calendar)
//    }

    operator fun invoke(calendar: Calendar) {
        alarmManagerHelper.setAlarm(calendar)
    }
}