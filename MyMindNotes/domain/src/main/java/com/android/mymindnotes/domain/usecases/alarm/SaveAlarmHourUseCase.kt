package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.data.repositoryInterfaces.MemberLocalRepository
import javax.inject.Inject

class SaveAlarmHourUseCase @Inject constructor(
    private val repository: MemberLocalRepository
) {
    // 시간 저장하기
//    suspend fun saveHour(hour: Int) = repository.saveHour(hour)

    suspend operator fun invoke(hour: Int) = repository.saveHour(hour)
}