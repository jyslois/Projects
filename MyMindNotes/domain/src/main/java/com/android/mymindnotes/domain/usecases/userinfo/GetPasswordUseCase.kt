package com.android.mymindnotes.domain.usecases.userinfo

import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPasswordUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {
//    suspend fun getPassword(): Flow<String?> {
//        return repository.getPassword()
//    }

    suspend operator fun invoke(): Flow<String?> {
        return repository.getPassword()
    }
}