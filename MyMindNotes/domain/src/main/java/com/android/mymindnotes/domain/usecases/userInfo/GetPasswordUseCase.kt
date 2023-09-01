package com.android.mymindnotes.domain.usecases.userInfo

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPasswordUseCase @Inject constructor(
    private val repository: MemberRepository
) {
    suspend operator fun invoke(): Flow<String?> {
        return repository.getPassword()
    }
}