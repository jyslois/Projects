package com.android.mymindnotes.domain.usecases.userInfo

import com.android.mymindnotes.data.repositoryInterfaces.MemberLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetIdUseCase @Inject constructor(
    private val repository: MemberLocalRepository
) {
//    suspend fun getId(): Flow<String?> {
//        return repository.getId()
//    }

    suspend operator fun invoke(): Flow<String?> {
        return repository.getId()
    }
}