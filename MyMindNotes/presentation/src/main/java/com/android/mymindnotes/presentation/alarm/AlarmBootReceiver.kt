package com.android.mymindnotes.presentation.alarm

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.Context
import android.util.Log
import com.android.mymindnotes.core.hilt.coroutineModules.MainDispatcherCoroutineScope
import com.android.mymindnotes.domain.usecases.alarm.GetRebootAlarmTimeUseCase
import com.android.mymindnotes.domain.usecases.alarm.SetAlarmUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AlarmBootReceiver: BroadcastReceiver() {

    @Inject
    lateinit var getRebootAlarmTimeUseCase: GetRebootAlarmTimeUseCase

    @Inject
    lateinit var setAlarmUseCase: SetAlarmUseCase

    @MainDispatcherCoroutineScope
    @Inject
    lateinit var mainDispatcherCoroutineScope: CoroutineScope

    override fun onReceive(context: Context, intent: Intent) {

        // Code to set alarm on boot
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            Log.e("알람 체크", "BootCheck : 부팅 액션까지 들어옴")
            // Set the alarm here.
            mainDispatcherCoroutineScope.launch {
                val rebootTime = getRebootAlarmTimeUseCase().first()
                if (rebootTime != 0L) {
                    // reset alarm only if there is a saved alarm
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = rebootTime
                    setAlarmUseCase(calendar)
                    Log.e("알람 체크", "BootCheck : " + calendar.time)
                }
            }
        }
    }
}