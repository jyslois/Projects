package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlarmTimeUseCase @Inject constructor(
    private val repository: MemberRepository
) {
//    // time 가져오기
//    suspend fun getTime(): Flow<String?> = repository.getTime()

    suspend operator fun invoke(): Flow<String?> = repository.getTime()
    
}