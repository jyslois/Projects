package com.android.mymindnotes.domain.usecases.userInfo

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import javax.inject.Inject

class ClearAlarmSettingsUseCase @Inject constructor(
    private val repository: MemberRepository
) {
//    suspend fun clearAlarmRelatedKeys() {
//        repository.clearAlarmRelatedKeys()
//    }

    suspend operator fun invoke() {
        repository.clearAlarmRelatedKeys()
    }

}