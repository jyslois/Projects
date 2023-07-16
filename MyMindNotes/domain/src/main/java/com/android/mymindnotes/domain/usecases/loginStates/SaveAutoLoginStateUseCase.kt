package com.android.mymindnotes.domain.usecases.loginStates

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import javax.inject.Inject

class SaveAutoLoginStateUseCase @Inject constructor(
    private val repository: MemberRepository
) {
//    suspend fun saveAutoLoginCheck(state: Boolean) {
//        repository.saveAutoLoginCheck(state)
//    }

    suspend operator fun invoke(state: Boolean) {
        repository.saveAutoLoginCheck(state)
    }
}