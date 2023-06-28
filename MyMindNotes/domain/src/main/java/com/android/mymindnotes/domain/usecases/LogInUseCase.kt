package com.android.mymindnotes.domain.usecases

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class LogInUseCase @Inject constructor(
    private val repository: MemberRepository
) {

//    suspend fun login(email: String, password: String): Flow<Map<String, Object>> {
//        return repository.login(email, password)
//    }

    suspend operator fun invoke(email: String, password: String): Flow<Map<String, Object>> {
        return repository.login(email, password)
    }

}