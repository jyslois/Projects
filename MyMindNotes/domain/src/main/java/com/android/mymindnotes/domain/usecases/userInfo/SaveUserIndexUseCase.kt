package com.android.mymindnotes.domain.usecases.userInfo

import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import javax.inject.Inject

class SaveUserIndexUseCase  @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {
//    suspend fun saveUserIndex(index: Int) {
//        repository.saveUserIndex(index)
//    }

    suspend operator fun invoke(index: Int) {
        repository.saveUserIndex(index)
    }

}