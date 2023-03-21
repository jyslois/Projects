package com.android.mymindnotes.data.repositoryImpl

import android.util.Log
import com.android.mymindnotes.data.datasources.SharedPreferencesDataSource
import com.android.mymindnotes.domain.repositoryinterfaces.SharedPreferencesRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
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

    // UserIndex 값을 저장하는 SharedFlow
    private val _userIndex = MutableSharedFlow<Int>()
    override val userIndex = _userIndex.asSharedFlow()
    //override val userIndex: SharedFlow<Int> get() = _userIndex.asSharedFlow()

    // FirstTime 값 저장하는 SharedFlow
    private val _firstTime = MutableSharedFlow<Boolean>()
    override val firstTime = _firstTime.asSharedFlow()

    // dataSource의 SharedPreferences에 접근하는 변수
    private val sharedPreferencesforAutoSave = dataSource.sharedPreferencesforAutoSave
    private val sharedPreferencesforAutoSaveEditor = sharedPreferencesforAutoSave.edit()

    private val sharedPreferenceforUser = dataSource.sharedPreferenceforUser
    private val sharedPreferenceforUserEditor = sharedPreferenceforUser.edit()

    private val sharedPreferenceforFirstTime = dataSource.sharedPreferenceforFirstTime
    private val sharedPreferenceforFirstTimeEditor = sharedPreferenceforFirstTime.edit()

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

    override suspend fun getUserIndexfromUserSharedPreferences() {
        sharedPreferenceforUser.getInt("userindex", 0).let {_userIndex.emit(it) }
    }

    override suspend fun getFirstTimefromFirstTimeSharedPreferences() {
        sharedPreferenceforFirstTime.getBoolean("firstTime", false).let { _firstTime.emit(it) }
        Log.e("FirstTimeCheck", "결과 emit - Repository")
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

    override suspend fun saveUserIndextoUserSharedPreferences(index: Int) {
        sharedPreferenceforUserEditor.putInt("userindex", index).commit()
    }

    override suspend fun saveFirstTimetoFirstTimeSharedPreferences(boolean: Boolean) {
        sharedPreferenceforFirstTimeEditor.putBoolean("firstTime", boolean).commit()
    }

}