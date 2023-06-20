package com.android.mymindnotes.domain.usecases.userinfo

import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetIdUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {
//    suspend fun getId(): Flow<String?> {
//        return repository.getId()
//    }

    suspend operator fun invoke(): Flow<String?> {
        return repository.getId()
    }
}