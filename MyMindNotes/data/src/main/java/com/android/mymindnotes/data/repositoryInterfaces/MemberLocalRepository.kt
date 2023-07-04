package com.android.mymindnotes.data.repositoryInterfaces

import kotlinx.coroutines.flow.*

interface MemberLocalRepository {

    // get Methods
    suspend fun getAutoLoginCheck(): Flow<Boolean>
    suspend fun getAutoSaveCheck(): Flow<Boolean>
    suspend fun getId(): Flow<String?>
    suspend fun getPassword(): Flow<String?>
    suspend fun getFirstTime(): Flow<Boolean>
    suspend fun getAlarmState(): Flow<Boolean>
    suspend fun getTime(): Flow<String?>
    suspend fun getHour(): Flow<Int>
    suspend fun getMinute(): Flow<Int>
    suspend fun getRebootTime(): Flow<Long>


    // save Methods
    suspend fun saveAutoLoginCheck(state: Boolean)
    suspend fun saveAutoSaveCheck(state: Boolean)
    suspend fun saveIdAndPassword(id: String?, password: String?)
    suspend fun savePassword(password: String?)
    suspend fun saveUserIndex(index: Int)
    suspend fun saveFirstTime(boolean: Boolean)
    suspend fun saveAlarmState(state: Boolean)
    suspend fun saveTime(time: String)
    suspend fun saveHour(hour: Int)
    suspend fun saveMinute(minute: Int)
    suspend fun saveRebootTime(time: Long)

    // clear SharedPreferences
    suspend fun clearAutoSaveSharedPreferences()
    suspend fun clearAlarmSharedPreferences()
    suspend fun clearTimeSharedPreferences()
}