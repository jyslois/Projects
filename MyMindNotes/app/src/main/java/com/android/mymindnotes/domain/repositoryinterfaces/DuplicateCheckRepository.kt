package com.android.mymindnotes.domain.repositoryinterfaces

import kotlinx.coroutines.flow.SharedFlow

interface DuplicateCheckRepository {
    suspend fun checkEmail(emailInput: String)
    // 나중에 checkNickName 함수도 추가
    val emailCheckResult: SharedFlow<Map<String, Object>>
}