package com.android.mymindnotes.domain.usecases.userInfo

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import javax.inject.Inject

class ClearTimeSettingsUseCase @Inject constructor(
    private val repository: MemberRepository
) {
//    suspend fun clearRebootTimeKey() {
//        repository.clearRebootTimeKey()
//    }

    suspend operator fun invoke() {
        repository.clearRebootTimeKey()
    }
}