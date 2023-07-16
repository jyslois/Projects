package com.android.mymindnotes.domain.usecases.userInfo

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFirstTimeStateUseCase @Inject constructor(
    private val repository: MemberRepository
) {
//    suspend fun getFirstTime(): Flow<Boolean> {
//        return repository.getFirstTime()
//    }

    suspend operator fun invoke(): Flow<Boolean> {
        return repository.getFirstTime()
    }
}