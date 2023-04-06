package com.android.mymindnotes.domain.usecase

import com.android.mymindnotes.domain.repositoryinterfaces.MemberRepository
import com.android.mymindnotes.hilt.module.MainDispatcherCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class LogInUseCase @Inject constructor(
    private val repository: MemberRepository,
    @MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
) {

    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>()
    val error = _error.asSharedFlow()


    suspend fun login(email: String, password: String): Flow<Map<String, Object>> {
        return repository.login(email, password)
    }

    init {
        mainDispatcherCoroutineScope.launch {
            launch {
                // error collect & emit
                repository.error.collect {
                    _error.emit(it)
                }
            }
        }
    }


}