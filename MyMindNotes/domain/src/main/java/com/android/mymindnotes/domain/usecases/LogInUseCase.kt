package com.android.mymindnotes.domain.usecases

import com.android.mymindnotes.data.repositoryInterfaces.MemberRemoteRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class LogInUseCase @Inject constructor(
    private val repository: MemberRemoteRepository
) {

//    suspend fun login(email: String, password: String): Flow<Map<String, Object>> {
//        return repository.login(email, password)
//    }

    suspend operator fun invoke(email: String, password: String): Flow<Map<String, Object>> {
        return repository.login(email, password)
    }

}