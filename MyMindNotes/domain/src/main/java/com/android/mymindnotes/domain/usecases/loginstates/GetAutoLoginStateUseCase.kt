package com.android.mymindnotes.domain.usecases.loginstates

import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAutoLoginStateUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {
//    suspend fun getAutoLogin(): Flow<Boolean> {
//        return repository.getAutoLoginCheck()
//    }

    suspend operator fun invoke(): Flow<Boolean> {
        return repository.getAutoLoginCheck()
    }
}