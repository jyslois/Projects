package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.data.repositoryInterfaces.MemberLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlarmMinuteUseCase @Inject constructor(
    private val repository: MemberLocalRepository
) {

//    // 분 불러오기
//    suspend fun getMinute(): Flow<Int> = repository.getMinute()

    suspend operator fun invoke(): Flow<Int> = repository.getMinute()

}