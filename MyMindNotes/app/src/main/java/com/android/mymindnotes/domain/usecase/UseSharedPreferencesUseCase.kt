package com.android.mymindnotes.domain.usecase

import android.util.Log
import com.android.mymindnotes.domain.repositoryinterfaces.SharedPreferencesRepository
import com.android.mymindnotes.hilt.module.IoDispatcherCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class UseSharedPreferencesUseCase @Inject constructor(
    private val repository: SharedPreferencesRepository,
    @IoDispatcherCoroutineScope private val ioDispatcherCoroutineScope: CoroutineScope
) {

    // autoLoginCheck 값을 저장하는 SharedFlow
    private val _autoLoginCheck = MutableSharedFlow<Boolean>()
    val autoLoginCheck = _autoLoginCheck.asSharedFlow()
    // autoSave 값을 저장하는 SharedFlow
    private val _autoSaveCheck = MutableSharedFlow<Boolean>()
    val autoSaveCheck = _autoSaveCheck.asSharedFlow()
    // autoLoginState 값을 저장하는 SharedFlow
    private val _autoLoginState = MutableSharedFlow<Boolean>()
    val autoLoginState = _autoLoginState.asSharedFlow()
    // Id 값을 저장하는 SharedFlow
    private val _id = MutableSharedFlow<String>()
    val id = _id.asSharedFlow()
    // Password 값을 저장하는 SharedFlow
    private val _password = MutableSharedFlow<String>()
    val password = _password.asSharedFlow()
    // FirstTime 값 저장하는 SharedFlow
    private val _firstTime = MutableSharedFlow<Boolean>()
    val firstTime = _firstTime.asSharedFlow()

    // get methods
    suspend fun getAutoLogin() {
        repository.getAutoLoginCheckfromAutoSaveSharedPreferences()
    }

    suspend fun getAutoSave() {
        repository.getAutoSaveCheckfromAutoSaveSharedPreferences()
    }

    suspend fun getIdAndPassword() {
        repository.getIdAndPasswordfromAutoSaveSharedPreferences()
    }

    suspend fun getUserIndex() {
        repository.getUserIndexfromUserSharedPreferences()
    }

    suspend fun getFirstTime() {
        repository.getFirstTimefromFirstTimeSharedPreferences()
    }

    // emit values got from Sharedpreference
    init {
        ioDispatcherCoroutineScope.launch {
            launch {
                repository.autoLoginCheck.collect {
                    _autoLoginCheck.emit(it)
                }
            }

            launch {
                repository.autoSaveCheck.collect {
                    _autoSaveCheck.emit(it)
                }
            }

            launch {
                repository.id.collect {
                    _id.emit(it)
                }
            }

            launch {
                repository.password.collect {
                    _password.emit(it)
                }
            }

            launch {
                repository.firstTime.collect {
                    _firstTime.emit(it)
                    Log.e("FirstTimeCheck", "결과 emit - UseCase, 결과: $it")
                }
            }
        }
    }

    // save methods
    suspend fun saveAutoLoginCheck(state: Boolean) {
        repository.saveAutoLoginChecktoAutoSaveSharedPreferences(state)
    }

    suspend fun saveAutoSaveCheck(state: Boolean) {
        repository.saveAutoSaveChecktoAutoSaveSharedPreferences(state)
    }

    suspend fun saveIdAndPassword(id: String?, password: String?) {
        repository.saveIdAndPasswordtoAutoSaveSharedPreferences(id, password)
    }

    suspend fun saveUserIndex(index: Int) {
        repository.saveUserIndextoUserSharedPreferences(index)
    }

    suspend fun saveFirstTime(boolean: Boolean) {
        repository.saveFirstTimetoFirstTimeSharedPreferences(boolean)
    }

    // clear sharedpreferences
    suspend fun clearAutoSaveSharedPreferences() {
        repository.clearAutoSaveSharedPreferences()
    }

}