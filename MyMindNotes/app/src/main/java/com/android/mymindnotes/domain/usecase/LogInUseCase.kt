package com.android.mymindnotes.domain.usecase

import com.android.mymindnotes.domain.repositoryinterfaces.MemberRepository
import com.android.mymindnotes.hilt.module.IoDispatcherCoroutineScope
import com.android.mymindnotes.hilt.module.MainDispatcherCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LogInUseCase @Inject constructor(
    private val repository: MemberRepository,
    @MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
) {

    private val _logInResult = MutableSharedFlow<Map<String, Object>>()
    val logInResult = _logInResult.asSharedFlow()

    suspend fun login(email: String, password: String) {
        repository.login(email, password)
    }

    init {
        mainDispatcherCoroutineScope.launch {
            repository.logInResult.collect {
                _logInResult.emit(it)
            }
        }
    }


}