package com.android.mymindnotes.domain.usecases.loginstates

import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAutoSaveStateUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {
//    suspend fun getAutoSave(): Flow<Boolean> {
//        return repository.getAutoSaveCheck()
//    }

    suspend operator fun invoke(): Flow<Boolean> {
        return repository.getAutoSaveCheck()
    }
}