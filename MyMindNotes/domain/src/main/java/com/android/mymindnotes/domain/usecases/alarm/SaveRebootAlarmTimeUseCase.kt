package com.android.mymindnotes.domain.usecases.alarm

import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import javax.inject.Inject

class SaveRebootAlarmTimeUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {

//    suspend fun saveRebootTime(time: Long) = repository.saveRebootTime(time)

    suspend operator fun invoke(time: Long) = repository.saveRebootTime(time)

}