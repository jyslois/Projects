package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveAlarmTimeUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {
    // time 저장하기
//    suspend fun saveTime(time: String) = repository.saveTime(time)
    suspend operator fun invoke(time: String) = repository.saveTime(time)


}