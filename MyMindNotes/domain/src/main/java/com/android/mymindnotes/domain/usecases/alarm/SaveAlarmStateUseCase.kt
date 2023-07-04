package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.data.repositoryInterfaces.MemberLocalRepository
import javax.inject.Inject

class SaveAlarmStateUseCase @Inject constructor(
    private val repository: MemberLocalRepository
) {
//    suspend fun saveAlarmState(state: Boolean) = repository.saveAlarmState(state)

    suspend operator fun invoke(state:Boolean) = repository.saveAlarmState(state)
}