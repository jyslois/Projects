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
) : SharedPreferencesRepository {

    // SharedFlows
    // autoLoginCheck 값을 저장하는 SharedFlow
    private val _autoLoginCheck = MutableSharedFlow<Boolean>()
    override val autoLoginCheck = _autoLoginCheck.asSharedFlow()

    // autoSave 값을 저장하는 SharedFlow
    private val _autoSaveCheck = MutableSharedFlow<Boolean>()
    override val autoSaveCheck = _autoSaveCheck.asSharedFlow()

    // Id 값을 저장하는 SharedFlow
    private val _id = MutableSharedFlow<String>()
    override val id = _id.asSharedFlow()

    // Password 값을 저장하는 SharedFlow
    private val _password = MutableSharedFlow<String>()
    override val password = _password.asSharedFlow()

    // dataSource의 SharedPreferences에 접근하는 변수
    private val sharedPreferencesforAutoSave = dataSource.sharedPreferencesforAutoSave
    private val sharedPreferencesforAutoSaveEditor = dataSource.sharedPreferencesforAutoSave.edit()

    // get methods
    override suspend fun getAutoLoginCheckfromAutoSaveSharedPreferences() {
        // dataSource의 SharedPreferences에 접근해서 autoLoginCheck 값을 가져와 SharedFlow에 emit하기
        _autoLoginCheck.emit(sharedPreferencesforAutoSave.getBoolean("autoLoginCheck", false))
    }

    override suspend fun getAutoSaveCheckfromAutoSaveSharedPreferences() {
        // dataSource의 SharedPreferences에 접근해서 autoSaveCheck 값을 가져와 SharedFlow에 emit하기
        _autoSaveCheck.emit(sharedPreferencesforAutoSave.getBoolean("autoSaveCheck", false))
    }

    override suspend fun getIdAndPasswordfromAutoSaveSharedPreferences() {
        // dataSource의 SharedPreferences에 접근해서 id 값을 가져와 SharedFlow에 emit하기
        sharedPreferencesforAutoSave.getString("id", null)?.let { _id.emit(it) }
        // dataSource의 SharedPreferences에 접근해서 id 값을 가져와 SharedFlow에 emit하기
        sharedPreferencesforAutoSave.getString("password", null)?.let { _password.emit(it) }
    }

    // save methods
    override suspend fun saveAutoLoginChecktoAutoSaveSharedPreferences(state: Boolean) {
        sharedPreferencesforAutoSaveEditor.putBoolean("autoLoginCheck", state).commit()
    }

    override suspend fun saveAutoSaveChecktoAutoSaveSharedPreferences(state: Boolean) {
        sharedPreferencesforAutoSaveEditor.putBoolean("autoSaveCheck", state).commit()
    }

    override suspend fun saveIdAndPasswordtoAutoSaveSharedPreferences(id: String?, password: String?) {
        sharedPreferencesforAutoSaveEditor.putString("id", id).commit()
        sharedPreferencesforAutoSaveEditor.putString("password", password).commit()
    }

}