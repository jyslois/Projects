package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import javax.inject.Inject

class SaveAlarmStateUseCase @Inject constructor(
    private val repository: MemberRepository
) {
//    suspend fun saveAlarmState(state: Boolean) = repository.saveAlarmState(state)

    suspend operator fun invoke(state:Boolean) = repository.saveAlarmState(state)
}