package com.android.mymindnotes.domain.repositoryinterfaces

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface MemberRepository {
    // Get User Info
//    suspend fun getUserInfo()
//    val userInfo: SharedFlow<Map<String, Object>>
    suspend fun getUserInfo(): Flow<Map<String, Object>>

    // Log in & Log out
    suspend fun login(email: String, password: String)
    val logInResult: SharedFlow<Map<String, Object>>

    // Email & Nickname Duplicate Check
    suspend fun checkEmail(emailInput: String)
    val emailCheckResult: SharedFlow<Map<String, Object>>

    suspend fun checkNickName(nickNameInput: String)
    val nickNameCheckResult: SharedFlow<Map<String, Object>>

    // Join
    suspend fun join(email: String, nickname: String, password: String, birthyear: Int)
    val joinResult: SharedFlow<Map<String, Object>>

    // Delete
    suspend fun deleteUser()
    val deleteUserResult: SharedFlow<Map<String, Object>>
}