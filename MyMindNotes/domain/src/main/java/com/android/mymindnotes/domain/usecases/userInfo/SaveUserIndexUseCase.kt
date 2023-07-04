package com.android.mymindnotes.domain.usecases.userInfo

import com.android.mymindnotes.data.repositoryInterfaces.MemberLocalRepository
import javax.inject.Inject

class SaveUserIndexUseCase  @Inject constructor(
    private val repository: MemberLocalRepository
) {
//    suspend fun saveUserIndex(index: Int) {
//        repository.saveUserIndex(index)
//    }

    suspend operator fun invoke(index: Int) {
        repository.saveUserIndex(index)
    }

}