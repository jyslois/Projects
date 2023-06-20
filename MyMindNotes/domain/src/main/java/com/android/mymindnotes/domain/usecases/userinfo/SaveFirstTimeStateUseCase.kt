package com.android.mymindnotes.domain.usecases.userinfo

import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import javax.inject.Inject

class SaveFirstTimeStateUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {
//    suspend fun saveFirstTime(state: Boolean) {
//        repository.saveFirstTime(state)
//    }

    suspend operator fun invoke(state: Boolean) {
        repository.saveFirstTime(state)
    }
}