package com.android.mymindnotes.domain.usecase

import com.android.mymindnotes.domain.repositoryinterfaces.LogInandOutRepository
import com.android.mymindnotes.hilt.module.IoDispatcherCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LogInandOutUseCase @Inject constructor(
    private val repository: LogInandOutRepository,
    @IoDispatcherCoroutineScope private val ioDispatcherCoroutineScope: CoroutineScope
) {

    private val _logInResult = MutableSharedFlow<Map<String, Object>>()
    val logInResult = _logInResult.asSharedFlow()

    suspend fun login(email: String, password: String) {
        repository.login(email, password)
    }

    init {
        ioDispatcherCoroutineScope.launch {
            repository.logInResult.collect {
                _logInResult.emit(it)
            }
        }
    }


}