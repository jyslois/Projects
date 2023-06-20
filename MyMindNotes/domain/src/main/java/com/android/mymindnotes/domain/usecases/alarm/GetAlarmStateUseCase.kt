package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlarmStateUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {
//    suspend fun getAlarmState(): Flow<Boolean> = repository.getAlarmState()

    suspend operator fun invoke(): Flow<Boolean> = repository.getAlarmState()
}