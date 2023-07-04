package com.android.mymindnotes.domain.usecases.userInfo

import com.android.mymindnotes.data.repositoryInterfaces.MemberLocalRepository
import javax.inject.Inject

class ClearTimeSettingsUseCase @Inject constructor(
    private val repository: MemberLocalRepository
) {
//    suspend fun clearTimeSharedPreferences() {
//        repository.clearTimeSharedPreferences()
//    }

    suspend operator fun invoke() {
        repository.clearTimeSharedPreferences()
    }
}