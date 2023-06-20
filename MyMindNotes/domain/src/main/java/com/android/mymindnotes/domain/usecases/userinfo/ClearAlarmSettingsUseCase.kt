package com.android.mymindnotes.domain.usecases.userinfo

import com.android.mymindnotes.data.repositoryInterfaces.MemberSharedPreferencesRepository
import javax.inject.Inject

class ClearAlarmSettingsUseCase @Inject constructor(
    private val repository: MemberSharedPreferencesRepository
) {
//    suspend fun clearAlarmSharedPreferences() {
//        repository.clearAlarmSharedPreferences()
//    }

    suspend operator fun invoke() {
        repository.clearAlarmSharedPreferences()
    }

}