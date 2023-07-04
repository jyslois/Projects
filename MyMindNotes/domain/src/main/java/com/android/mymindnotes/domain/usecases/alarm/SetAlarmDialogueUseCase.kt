package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.domain.usecases.userInfo.ClearTimeSettingsUseCase
import java.util.Calendar
import javax.inject.Inject

class SetAlarmDialogueUseCase @Inject constructor(
    private var clearTimeSettingsUseCase: ClearTimeSettingsUseCase,
    private val saveAlarmTimeUseCase: SaveAlarmTimeUseCase,
    private val saveAlarmHourUseCase: SaveAlarmHourUseCase,
    private val saveAlarmMinuteUseCase: SaveAlarmMinuteUseCase,
    private val saveRebootAlarmTimeUseCase: SaveRebootAlarmTimeUseCase,
    private val setAlarmUseCase: SetAlarmUseCase,
    private val stopAlarmUseCase: StopAlarmUseCase
) {

    suspend operator fun invoke(time: String, hourOfDay: Int, min: Int, minute: Int) {
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
    }
}