package com.android.mymindnotes.domain.usecase

import android.util.Log
import com.android.mymindnotes.domain.repositoryinterfaces.SharedPreferencesRepository
import com.android.mymindnotes.hilt.module.IoDispatcherCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    suspend fun getAutoLogin() {
        repository.getAutoLoginfromAutoSaveSharedPreferences()
    }

    init {
        ioDispatcherCoroutineScope.launch {
            repository.autoLoginCheck.collect {
                _autoLoginCheck.emit(it)
            }
        }
    }

}