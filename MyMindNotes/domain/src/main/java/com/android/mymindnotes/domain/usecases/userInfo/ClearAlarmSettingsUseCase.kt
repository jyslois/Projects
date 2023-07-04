package com.android.mymindnotes.domain.usecases.userInfo

import com.android.mymindnotes.data.repositoryInterfaces.MemberLocalRepository
import javax.inject.Inject

class ClearAlarmSettingsUseCase @Inject constructor(
    private val repository: MemberLocalRepository
) {
//    suspend fun clearAlarmSharedPreferences() {
//        repository.clearAlarmSharedPreferences()
//    }

    suspend operator fun invoke() {
        repository.clearAlarmSharedPreferences()
    }

}