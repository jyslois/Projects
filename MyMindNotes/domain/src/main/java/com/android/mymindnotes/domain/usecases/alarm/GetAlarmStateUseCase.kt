package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.data.repositoryInterfaces.MemberLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlarmStateUseCase @Inject constructor(
    private val repository: MemberLocalRepository
) {
//    suspend fun getAlarmState(): Flow<Boolean> = repository.getAlarmState()

    suspend operator fun invoke(): Flow<Boolean> = repository.getAlarmState()
}