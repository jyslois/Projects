package com.android.mymindnotes.domain.repositoryinterfaces

import kotlinx.coroutines.flow.SharedFlow

interface SharedPreferencesRepository {

    // autoSave SharedPreferences로부터 AutoLoginCheck (로그인 상태 유지 값) 가져오는 메서드
    suspend fun getAutoLoginCheckfromAutoSaveSharedPreferences()
    suspend fun getAutoSaveCheckfromAutoSaveSharedPreferences()
    suspend fun getIdAndPasswordfromAutoSaveSharedPreferences()

    // AutoLoginCheck 값을 저장하는 변수
    val autoLoginCheck: SharedFlow<Boolean>
    val autoSaveCheck: SharedFlow<Boolean>
    val id: SharedFlow<String>
    val password: SharedFlow<String>

}