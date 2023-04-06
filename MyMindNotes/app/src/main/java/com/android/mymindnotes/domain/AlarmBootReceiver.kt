package com.android.mymindnotes.domain

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.android.mymindnotes.domain.usecase.AlarmUseCase
import com.android.mymindnotes.hilt.module.MainDispatcherCoroutineScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AlarmBootReceiver: BroadcastReceiver(){

    @Inject
    lateinit var alarmUseCase: AlarmUseCase

    @MainDispatcherCoroutineScope
    @Inject
    lateinit var mainDispatcherCoroutineScope: CoroutineScope

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onReceive(context: Context, intent: Intent) {

        // Code to set alarm on boot
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            Log.e("알람 체크", "BootCheck : 부팅 액션까지 들어옴")
            // Set the alarm here.
            mainDispatcherCoroutineScope.launch {
                val rebootTime = alarmUseCase.getRebootTime().first()
                if (rebootTime != 0L) {
                    // reset alarm only if there is a saved alarm
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = rebootTime
                    alarmUseCase.setAlarm(calendar, context)
                    Log.e("알람 체크", "BootCheck : " + calendar.time)
                }
            }
        }
    }
}