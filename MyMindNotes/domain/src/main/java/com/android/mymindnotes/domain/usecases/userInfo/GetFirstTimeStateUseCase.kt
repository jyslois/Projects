package com.android.mymindnotes.domain.usecases.userInfo

import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFirstTimeStateUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {
//    suspend fun getFirstTime(): Flow<Boolean> {
//        return repository.getFirstTime()
//    }

    suspend operator fun invoke(): Flow<Boolean> {
        return repository.getFirstTime()
    }
}