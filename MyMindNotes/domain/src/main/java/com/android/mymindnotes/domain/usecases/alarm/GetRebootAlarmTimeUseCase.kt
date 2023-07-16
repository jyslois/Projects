package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRebootAlarmTimeUseCase @Inject constructor(
    private val repository: MemberRepository
) {

//    suspend fun getRebootTime(): Flow<Long> = repository.getRebootTime()

    suspend operator fun invoke(): Flow<Long> = repository.getRebootTime()
}