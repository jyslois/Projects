package com.android.mymindnotes.domain.usecases.loginStates

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAutoLoginStateUseCase @Inject constructor(
    private val repository: MemberRepository
) {
    suspend operator fun invoke(): Flow<Boolean> {
        return repository.getAutoLoginCheck()
    }
}