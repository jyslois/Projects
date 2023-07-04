package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.data.repositoryInterfaces.MemberLocalRepository
import javax.inject.Inject

class SaveAlarmTimeUseCase @Inject constructor(
    private val repository: MemberLocalRepository
) {
    // time 저장하기
//    suspend fun saveTime(time: String) = repository.saveTime(time)
    suspend operator fun invoke(time: String) = repository.saveTime(time)


}