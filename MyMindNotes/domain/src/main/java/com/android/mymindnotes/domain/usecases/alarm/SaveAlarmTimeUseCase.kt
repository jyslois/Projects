package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import javax.inject.Inject

class SaveAlarmTimeUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {
    // time 저장하기
    suspend fun saveTime(time: String) = repository.saveTime(time)

    // 시간 저장하기
    suspend fun saveHour(hour: Int) = repository.saveHour(hour)

    // 분 저장하기
    suspend fun saveMinute(minute: Int) = repository.saveMinute(minute)
}