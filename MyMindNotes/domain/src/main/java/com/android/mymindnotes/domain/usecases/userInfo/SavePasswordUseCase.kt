package com.android.mymindnotes.domain.usecases.userInfo

import com.android.mymindnotes.data.repositoryInterfaces.MemberLocalRepository
import javax.inject.Inject

class SavePasswordUseCase @Inject constructor(
    private val repository: MemberLocalRepository
) {
//    suspend fun savePassword(password: String?) {
//        repository.savePassword(password)
//    }

    suspend operator fun invoke(password: String?) {
        repository.savePassword(password)
    }
}