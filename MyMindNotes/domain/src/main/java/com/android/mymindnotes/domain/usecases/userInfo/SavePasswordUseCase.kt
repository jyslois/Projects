package com.android.mymindnotes.domain.usecases.userInfo

import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import javax.inject.Inject

class SavePasswordUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {
//    suspend fun savePassword(password: String?) {
//        repository.savePassword(password)
//    }

    suspend operator fun invoke(password: String?) {
        repository.savePassword(password)
    }
}