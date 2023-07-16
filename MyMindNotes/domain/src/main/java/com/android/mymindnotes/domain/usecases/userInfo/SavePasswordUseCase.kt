package com.android.mymindnotes.domain.usecases.userInfo

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import javax.inject.Inject

class SavePasswordUseCase @Inject constructor(
    private val repository: MemberRepository
) {
//    suspend fun savePassword(password: String?) {
//        repository.savePassword(password)
//    }

    suspend operator fun invoke(password: String?) {
        repository.savePassword(password)
    }
}