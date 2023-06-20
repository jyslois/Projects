package com.android.mymindnotes.domain.usecases.loginstates

import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import javax.inject.Inject

class SaveAutoLoginStateUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {
//    suspend fun saveAutoLoginCheck(state: Boolean) {
//        repository.saveAutoLoginCheck(state)
//    }

    suspend operator fun invoke(state: Boolean) {
        repository.saveAutoLoginCheck(state)
    }
}