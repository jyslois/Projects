package com.android.mymindnotes.domain.repositoryinterfaces

import kotlinx.coroutines.flow.SharedFlow

interface SharedPreferencesRepository {

    // SharedPreferences로부터 AutoSave (로그인 상태 유지 값) 가져오는 메서드
    suspend fun getAutoLoginfromAutoSaveSharedPreferences()

    // AutoLoginCheck 값을 저장하는 변수
    val autoLoginCheck: SharedFlow<Boolean>

}