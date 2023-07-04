package com.android.mymindnotes.domain.usecases.loginStates

import com.android.mymindnotes.data.repositoryInterfaces.MemberLocalRepository
import javax.inject.Inject

class ClearLoginStatesUseCase @Inject constructor(
    private val repository: MemberLocalRepository
) {
//    suspend fun clearLoginStates() {
//        repository.clearAutoSaveSharedPreferences()
//    }

    suspend operator fun invoke() {
        repository.clearAutoSaveSharedPreferences()
    }
}