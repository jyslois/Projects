package com.android.mymindnotes.domain.usecases.loginStates

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import javax.inject.Inject

class ClearLoginStatesUseCase @Inject constructor(
    private val repository: MemberRepository
) {
//    suspend fun clearLoginStates() {
//        repository.clearAutoSaveSharedPreferences()
//    }

    suspend operator fun invoke() {
        repository.clearAutoSaveSharedPreferences()
    }
}