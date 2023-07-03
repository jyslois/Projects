package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlarmMinuteUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {

//    // 분 불러오기
//    suspend fun getMinute(): Flow<Int> = repository.getMinute()

    suspend operator fun invoke(): Flow<Int> = repository.getMinute()

}