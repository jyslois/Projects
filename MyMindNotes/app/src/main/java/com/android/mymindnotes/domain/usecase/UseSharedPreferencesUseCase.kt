package com.android.mymindnotes.domain.usecase

import android.util.Log
import com.android.mymindnotes.domain.repositoryinterfaces.SharedPreferencesRepository
import com.android.mymindnotes.hilt.module.IoDispatcherCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class UseSharedPreferencesUseCase @Inject constructor(
    private val repository: SharedPreferencesRepository,
    @IoDispatcherCoroutineScope private val ioDispatcherCoroutineScope: CoroutineScope
) {

    // autoLoginCheck 값을 저장하는 SharedFlow
    private val _autoLoginCheck = MutableSharedFlow<Boolean>()
    val autoLoginCheck get() = _autoLoginCheck.asSharedFlow()
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

    // get methods
    suspend fun getAutoLogin() {
        Log.e("확인", "UseCase - 메서드 호출")
        repository.getAutoLoginCheckfromAutoSaveSharedPreferences()
    }

    suspend fun getAutoSave() {
        repository.getAutoSaveCheckfromAutoSaveSharedPreferences()
    }

    suspend fun getIdAndPassword() {
        repository.getIdAndPasswordfromAutoSaveSharedPreferences()
    }

    // emit values got from Sharedpreference
    init {
        ioDispatcherCoroutineScope.launch {
            launch {
                repository.autoLoginCheck.collect {
                    _autoLoginCheck.emit(it)
                    Log.e("확인", "UseCase - emit 됨")
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

}