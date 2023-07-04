package com.android.mymindnotes.domain.usecases.loginStates

import com.android.mymindnotes.data.repositoryInterfaces.MemberLocalRepository
import javax.inject.Inject

class SaveAutoSaveStateUseCase @Inject constructor(
    private val repository: MemberLocalRepository
) {
//    suspend fun saveAutoSaveCheck(state: Boolean) {
//        repository.saveAutoSaveCheck(state)
//    }

    suspend operator fun invoke(state: Boolean) {
        repository.saveAutoSaveCheck(state)
    }
}