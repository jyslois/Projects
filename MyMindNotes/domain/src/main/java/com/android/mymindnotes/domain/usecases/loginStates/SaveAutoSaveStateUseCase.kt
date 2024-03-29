package com.android.mymindnotes.domain.usecases.loginStates

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import javax.inject.Inject

class SaveAutoSaveStateUseCase @Inject constructor(
    private val repository: MemberRepository
) {
    suspend operator fun invoke(state: Boolean) {
        repository.saveAutoSaveCheck(state)
    }
}