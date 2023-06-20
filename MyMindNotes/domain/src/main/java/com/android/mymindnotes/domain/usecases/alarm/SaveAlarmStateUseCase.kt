package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import javax.inject.Inject

class SaveAlarmStateUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {
//    suspend fun saveAlarmState(state: Boolean) = repository.saveAlarmState(state)

    suspend operator fun invoke(state:Boolean) = repository.saveAlarmState(state)
}