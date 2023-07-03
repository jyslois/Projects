package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import javax.inject.Inject

class SaveAlarmMinuteUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {

    // 분 저장하기
//    suspend fun saveMinute(minute: Int) = repository.saveMinute(minute)

    suspend operator fun invoke(minute: Int) = repository.saveMinute(minute)
}