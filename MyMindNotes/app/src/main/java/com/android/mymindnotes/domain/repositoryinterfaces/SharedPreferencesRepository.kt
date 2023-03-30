package com.android.mymindnotes.domain.repositoryinterfaces

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface SharedPreferencesRepository {

    // get Methods
    suspend fun getAutoLoginCheck(): Flow<Boolean>
    suspend fun getAutoSaveCheck(): Flow<Boolean>
    suspend fun getId(): Flow<String?>
    suspend fun getPassword(): Flow<String?>
    suspend fun getFirstTime(): Flow<Boolean>

    // save Methods
    suspend fun saveAutoLoginCheck(state: Boolean)
    suspend fun saveAutoSaveCheck(state: Boolean)
    suspend fun saveIdAndPassword(id: String?, password: String?)
    suspend fun saveUserIndex(index: Int)
    suspend fun saveFirstTime(boolean: Boolean)

    // clear SharedPreferences
    suspend fun clearAutoSaveSharedPreferences()
    suspend fun clearAlarmSharedPreferences()
    suspend fun clearTimeSharedPreferences()
}