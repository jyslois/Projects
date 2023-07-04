package com.android.mymindnotes.domain.usecases.loginStates

import com.android.mymindnotes.data.repositoryInterfaces.MemberLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAutoLoginStateUseCase @Inject constructor(
    private val repository: MemberLocalRepository
) {
//    suspend fun getAutoLogin(): Flow<Boolean> {
//        return repository.getAutoLoginCheck()
//    }

    suspend operator fun invoke(): Flow<Boolean> {
        return repository.getAutoLoginCheck()
    }
}