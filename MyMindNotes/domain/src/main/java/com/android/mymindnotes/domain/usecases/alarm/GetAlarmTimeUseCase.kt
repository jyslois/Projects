package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlarmTimeUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {
    // time 가져오기
    suspend fun getTime(): Flow<String?> = repository.getTime()

    // 시간 불러오기
    suspend fun getHour(): Flow<Int> = repository.getHour()

    // 분 불러오기
    suspend fun getMinute(): Flow<Int> = repository.getMinute()
}