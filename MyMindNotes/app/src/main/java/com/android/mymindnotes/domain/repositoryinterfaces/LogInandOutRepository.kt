package com.android.mymindnotes.domain.repositoryinterfaces

import kotlinx.coroutines.flow.SharedFlow

interface LogInandOutRepository {
    suspend fun login(email: String, password: String)
    // 나중에 logout 함수도 추가
    val logInResult: SharedFlow<Map<String, Object>>
}