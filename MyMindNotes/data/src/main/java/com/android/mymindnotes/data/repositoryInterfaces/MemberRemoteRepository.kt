package com.android.mymindnotes.data.repositoryInterfaces

import kotlinx.coroutines.flow.*

interface MemberRemoteRepository {
    // Get User Info
    suspend fun getUserInfo(): Flow<Map<String, Object>>

    // Log in & Log out
    suspend fun login(email: String, password: String): Flow<Map<String, Object>>

    // Email & Nickname Duplicate Check
    suspend fun checkEmail(emailInput: String): Flow<Map<String, Object>>
    suspend fun checkNickName(nickNameInput: String): Flow<Map<String, Object>>

    // Join
    suspend fun join(email: String, nickname: String, password: String, birthyear: Int): Flow<Map<String, Object>>

    // Delete
    suspend fun deleteUser(): Flow<Map<String, Object>>

    // change NickName
    suspend fun changeNickName(nickName: String): Flow<Map<String, Object>>

    // change Password
    suspend fun changePassword(password: String, originalPassword: String): Flow<Map<String, Object>>

    // change to TemporaryPassword
    suspend fun changeToTemporaryPassword(email: String, randomPassword: String): Flow<Map<String, Object>>
}