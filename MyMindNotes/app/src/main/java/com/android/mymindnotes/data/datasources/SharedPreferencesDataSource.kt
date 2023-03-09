package com.android.mymindnotes.data.datasources

import android.content.SharedPreferences
import com.android.mymindnotes.hilt.module.AutoSave
import javax.inject.Inject

class SharedPreferencesDataSource @Inject constructor(
    @AutoSave private val autoSave: SharedPreferences
) {
    private val _sharedPreferencesforAutoSave = autoSave
    // Repository에서 접근 가능하도록 public 변수에 SharedPreference 객체 할당
    val sharedPreferencesforAutoSave = _sharedPreferencesforAutoSave
}