package com.android.mymindnotes.data.repositoryImpl

import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.util.Log
import com.android.mymindnotes.data.datasources.SharedPreferencesDataSource
import com.android.mymindnotes.domain.repositoryinterfaces.SharedPreferencesRepository
import com.android.mymindnotes.hilt.module.FirstTime
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesRepositoryImpl @Inject constructor(
    private val dataSource: SharedPreferencesDataSource
) : SharedPreferencesRepository {

    // SharedFlows

    // UserIndex 값을 저장하는 SharedFlow
    private val _userIndex = MutableSharedFlow<Int>()
    override val userIndex = _userIndex.asSharedFlow()

//    // FirstTime 값 저장하는 SharedFlow
//    private val _firstTime = MutableSharedFlow<Boolean>()
//    override val firstTime = _firstTime.asSharedFlow()
//
    // autoLoginCheck 값을 저장하는 SharedFlow
//    private val _autoLoginCheck = MutableSharedFlow<Boolean>()
//    override val autoLoginCheck = _autoLoginCheck.asSharedFlow()
//
//    // autoSave 값을 저장하는 SharedFlow
//    private val _autoSaveCheck = MutableSharedFlow<Boolean>()
//    override val autoSaveCheck = _autoSaveCheck.asSharedFlow()
//
//    // Id 값을 저장하는 SharedFlow
//    private val _id = MutableSharedFlow<String>()
//    override val id = _id.asSharedFlow()
//
//    // Password 값을 저장하는 SharedFlow
//    private val _password = MutableSharedFlow<String>()
//    override val password = _password.asSharedFlow()

    // dataSource의 SharedPreferences에 접근하는 변수
    private val sharedPreferencesforAutoSave = dataSource.sharedPreferencesforAutoSave
    private val sharedPreferencesforAutoSaveEditor = sharedPreferencesforAutoSave.edit()

    private val sharedPreferenceforUser = dataSource.sharedPreferenceforUser
    private val sharedPreferenceforUserEditor = sharedPreferenceforUser.edit()

    private val sharedPreferenceforFirstTime = dataSource.sharedPreferenceforFirstTime
    private val sharedPreferenceforFirstTimeEditor = sharedPreferenceforFirstTime.edit()

    // get methods
    override suspend fun getAutoLoginCheckfromAutoSaveSharedPreferences(): Flow<Boolean> = flow {
        // dataSource의 SharedPreferences에 접근해서 autoLoginCheck 값을 가져와 SharedFlow에 emit하기
        val autoLoginCheck = sharedPreferencesforAutoSave.getBoolean("autoLoginCheck", false)
        emit(autoLoginCheck)
    }

    override suspend fun getAutoSaveCheckfromAutoSaveSharedPreferences(): Flow<Boolean> = flow {
        // dataSource의 SharedPreferences에 접근해서 autoSaveCheck 값을 가져와 SharedFlow에 emit하기
        val autoSaveCheck = sharedPreferencesforAutoSave.getBoolean("autoSaveCheck", false)
        emit(autoSaveCheck)
    }

    override  suspend fun getIdfromAutoSaveSharedPreferences(): Flow<String?> = flow {
        val id = sharedPreferencesforAutoSave.getString("id", null)
        emit(id)
    }

    override suspend fun getPasswordfromAutoSaveSharedPreferences(): Flow<String?> = flow {
        val password = sharedPreferencesforAutoSave.getString("password", null)
        emit(password)
    }

    //    override suspend fun getIdAndPasswordfromAutoSaveSharedPreferences() {
//        // dataSource의 SharedPreferences에 접근해서 id 값을 가져와 SharedFlow에 emit하기
//        sharedPreferencesforAutoSave.getString("id", null)?.let { _id.emit(it) }
//        // dataSource의 SharedPreferences에 접근해서 id 값을 가져와 SharedFlow에 emit하기
//        sharedPreferencesforAutoSave.getString("password", null)?.let { _password.emit(it) }
//    }

    override suspend fun getUserIndexfromUserSharedPreferences() {
        sharedPreferenceforUser.getInt("userindex", 0).let {_userIndex.emit(it) }
    }

    override suspend fun getFirstTimefromFirstTimeSharedPreferences(): Flow<Boolean> = flow {
        val firstTime = sharedPreferenceforFirstTime.getBoolean("firstTime", false)
        emit(firstTime)
    }

//    override suspend fun getFirstTimefromFirstTimeSharedPreferences() {
//        sharedPreferenceforFirstTime.getBoolean("firstTime", false).let { _firstTime.emit(it) }
//    }

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


    // clear sharedpreferencs
    override suspend fun clearAutoSaveSharedPreferences() {
        sharedPreferencesforAutoSaveEditor.clear().commit()
    }

}