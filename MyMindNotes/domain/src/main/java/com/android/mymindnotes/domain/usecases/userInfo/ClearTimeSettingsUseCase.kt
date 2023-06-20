package com.android.mymindnotes.domain.usecases.userInfo

import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import javax.inject.Inject

class ClearTimeSettingsUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {
//    suspend fun clearTimeSharedPreferences() {
//        repository.clearTimeSharedPreferences()
//    }

    suspend operator fun invoke() {
        repository.clearTimeSharedPreferences()
    }
}