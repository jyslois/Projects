package com.android.mymindnotes.domain.usecases.userInfo

import com.android.mymindnotes.data.repositoryInterfaces.MemberLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPasswordUseCase @Inject constructor(
    private val repository: MemberLocalRepository
) {
//    suspend fun getPassword(): Flow<String?> {
//        return repository.getPassword()
//    }

    suspend operator fun invoke(): Flow<String?> {
        return repository.getPassword()
    }
}