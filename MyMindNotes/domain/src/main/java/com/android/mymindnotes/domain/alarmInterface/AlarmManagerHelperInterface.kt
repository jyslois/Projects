package com.android.mymindnotes.domain.alarmInterface


interface AlarmManagerHelperInterface {
    fun setAlarm(calendar: java.util.Calendar)
    fun stopAlarm()
}