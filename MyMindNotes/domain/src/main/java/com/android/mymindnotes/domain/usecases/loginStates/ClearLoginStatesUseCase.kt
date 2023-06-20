package com.android.mymindnotes.domain.usecases.loginStates

import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import javax.inject.Inject

class ClearLoginStatesUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {
//    suspend fun clearLoginStates() {
//        repository.clearAutoSaveSharedPreferences()
//    }

    suspend operator fun invoke() {
        repository.clearAutoSaveSharedPreferences()
    }
}