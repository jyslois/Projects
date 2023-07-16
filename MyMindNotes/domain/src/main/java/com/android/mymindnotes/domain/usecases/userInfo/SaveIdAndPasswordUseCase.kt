package com.android.mymindnotes.domain.usecases.userInfo

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import javax.inject.Inject

class SaveIdAndPasswordUseCase @Inject constructor(
    private val repository: MemberRepository
) {
//    suspend fun saveIdAndPassword(id: String?, password: String?) {
//        repository.saveIdAndPassword(id, password)
//    }
//
    suspend operator fun invoke(id: String?, password: String?) {
        repository.saveIdAndPassword(id, password)
    }
}
