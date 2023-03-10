package com.android.mymindnotes.data.repositoryImpl

import com.android.mymindnotes.data.datasources.SharedPreferencesDataSource
import com.android.mymindnotes.domain.repositoryinterfaces.SharedPreferencesRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesRepositoryImpl @Inject constructor(
    private val dataSource: SharedPreferencesDataSource
): SharedPreferencesRepository {

    // autoLoginCheck 값을 저장하는 SharedFlow
    private val _autoLoginCheck = MutableSharedFlow<Boolean>()
    override val autoLoginCheck = _autoLoginCheck.asSharedFlow()

    override suspend fun getAutoLoginfromAutoSaveSharedPreferences() {
        // dataSource의 SharedPreferences에 접근해서 autoLoginCheck 값을 가져와 SharedFlow에 emit하기
        _autoLoginCheck.emit(dataSource.sharedPreferencesforAutoSave.getBoolean("autoLoginCheck", false))
    }

}