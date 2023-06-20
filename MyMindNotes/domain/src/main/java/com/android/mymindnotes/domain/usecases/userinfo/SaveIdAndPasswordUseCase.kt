package com.android.mymindnotes.domain.usecases.userinfo

import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import javax.inject.Inject

class SaveIdAndPasswordUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {
//    suspend fun saveIdAndPassword(id: String?, password: String?) {
//        repository.saveIdAndPassword(id, password)
//    }
//
    suspend operator fun invoke(id: String?, password: String?) {
        repository.saveIdAndPassword(id, password)
    }
}
