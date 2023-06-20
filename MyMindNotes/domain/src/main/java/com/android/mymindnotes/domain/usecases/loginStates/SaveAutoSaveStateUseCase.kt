package com.android.mymindnotes.domain.usecases.loginStates

import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import javax.inject.Inject

class SaveAutoSaveStateUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {
//    suspend fun saveAutoSaveCheck(state: Boolean) {
//        repository.saveAutoSaveCheck(state)
//    }

    suspend operator fun invoke(state: Boolean) {
        repository.saveAutoSaveCheck(state)
    }
}