package com.android.mymindnotes.domain.usecase

import com.android.mymindnotes.domain.repositoryinterfaces.SharedPreferencesRepository
import com.android.mymindnotes.hilt.module.IoDispatcherCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    // Id 값을 저장하는 SharedFlow
    private val _id = MutableSharedFlow<String>()
    val id = _id.asSharedFlow()
    // Password 값을 저장하는 SharedFlow
    private val _password = MutableSharedFlow<String>()
    val password = _password.asSharedFlow()


    suspend fun getAutoLogin() {
        repository.getAutoLoginCheckfromAutoSaveSharedPreferences()
    }

    suspend fun getAutoSave() {
        repository.getAutoSaveCheckfromAutoSaveSharedPreferences()
    }

    suspend fun getIdAndPassword() {
        repository.getIdAndPasswordfromAutoSaveSharedPreferences()
    }

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
        }
    }

}