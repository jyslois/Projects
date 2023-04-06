package com.android.mymindnotes.domain

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.android.mymindnotes.domain.usecase.AlarmUseCase
import com.android.mymindnotes.hilt.module.MainDispatcherCoroutineScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver: BroadcastReceiver() {

    @Inject
    lateinit var alarmUseCase: AlarmUseCase

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @MainDispatcherCoroutineScope
    @Inject
    lateinit var mainDispatcherCoroutineScope: CoroutineScope

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onReceive(context: Context, intent: Intent) {

        val nb = notificationHelper.getChannelNotification()

        //  알림창 클릭했을 때 알람창은 사라지기
        nb.setAutoCancel(true)

        // 기본 사운드 출력
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        nb.setSound(alarmSound)

        // notification 띄우기
        notificationHelper.getManager().notify(1, nb.build())

        alarmUseCase.stopAlarm(context)

        // 부팅시 알람 재설정을 위한 sharedPrefenreces의 시간 삭제하기
        mainDispatcherCoroutineScope.launch {
            alarmUseCase.clearTimeSharedPreferences()
        }

        // 알람 재호출 (반복 알람 세팅을 위해) - 하루 후에
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)
        alarmUseCase.setAlarm(calendar, context)

        // 부팅시 알람 재설정을 위해 sharedPrefenreces에 calendar의 time 저장
        mainDispatcherCoroutineScope.launch {
            alarmUseCase.saveRebootTime(calendar.timeInMillis)
        }
    }

}