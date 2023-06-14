package com.android.mymindnotes.domain.usecases

import com.android.mymindnotes.core.hilt.coroutineModules.MainDispatcherCoroutineScope
import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
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