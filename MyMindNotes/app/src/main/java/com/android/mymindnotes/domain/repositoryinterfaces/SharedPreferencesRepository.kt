package com.android.mymindnotes.domain.repositoryinterfaces

import kotlinx.coroutines.flow.SharedFlow

interface SharedPreferencesRepository {

    // 가져오는 메서드: autoSave SharedPreferences로부터 AutoLoginCheck, AutoSaveCheck, Id&password 가져오는 메서드
    suspend fun getAutoLoginCheckfromAutoSaveSharedPreferences()
    suspend fun getAutoSaveCheckfromAutoSaveSharedPreferences()
    suspend fun getIdAndPasswordfromAutoSaveSharedPreferences()
    suspend fun getUserIndexfromUserSharedPreferences()
    suspend fun getFirstTimefromFirstTimeSharedPreferences()

    // 저장하는 메서드
    suspend fun saveAutoLoginChecktoAutoSaveSharedPreferences(state: Boolean)
    suspend fun saveAutoSaveChecktoAutoSaveSharedPreferences(state: Boolean)
    suspend fun saveIdAndPasswordtoAutoSaveSharedPreferences(id: String?, password: String?)

    suspend fun saveUserIndextoUserSharedPreferences(index: Int)

    suspend fun saveFirstTimetoFirstTimeSharedPreferences(boolean: Boolean)

    // clear SharedPreferences
    suspend fun clearAutoSaveSharedPreferences()

    // get methods의 결과 값들을 저장하는 변수
    val autoLoginCheck: SharedFlow<Boolean>
    val autoSaveCheck: SharedFlow<Boolean>
    val id: SharedFlow<String>
    val password: SharedFlow<String>
    val userIndex: SharedFlow<Int>
    val firstTime: SharedFlow<Boolean>

}