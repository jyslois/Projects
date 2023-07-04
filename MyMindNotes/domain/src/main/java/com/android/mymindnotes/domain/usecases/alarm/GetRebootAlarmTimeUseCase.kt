package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.data.repositoryInterfaces.MemberLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRebootAlarmTimeUseCase @Inject constructor(
    private val repository: MemberLocalRepository
) {

//    suspend fun getRebootTime(): Flow<Long> = repository.getRebootTime()

    suspend operator fun invoke(): Flow<Long> = repository.getRebootTime()
}