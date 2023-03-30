package com.android.mymindnotes.domain.repositoryinterfaces

import kotlinx.coroutines.flow.*

interface MemberRepository {
    // Get User Info
    val userInfo: StateFlow<Map<String, Object>>
    suspend fun getUserInfo()

    // Log in & Log out
    suspend fun login(email: String, password: String): Flow<Map<String, Object>>

    // Email & Nickname Duplicate Check
    suspend fun checkEmail(emailInput: String): Flow<Map<String, Object>>
    suspend fun checkNickName(nickNameInput: String): Flow<Map<String, Object>>

    // Join
    suspend fun join(email: String, nickname: String, password: String, birthyear: Int): Flow<Map<String, Object>>

    // Delete
    suspend fun deleteUser(): Flow<Map<String, Object>>

    // 에러 메시지
    val error: SharedFlow<Boolean>
}