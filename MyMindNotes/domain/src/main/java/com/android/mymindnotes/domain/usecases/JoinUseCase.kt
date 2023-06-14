package com.android.mymindnotes.domain.usecases

import com.android.mymindnotes.core.hilt.coroutineModules.MainDispatcherCoroutineScope
import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class JoinUseCase @Inject constructor(
    private val repository: MemberRepository,
    @MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
) {

    // (서버) 회원 가입 함수 호출
    suspend fun join(email: String, nickname: String, password: String, birthyear: Int): Flow<Map<String, Object>> {
        return repository.join(email, nickname, password, birthyear)
    }

    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>(replay = 1)
    val error = _error.asSharedFlow()

    init {
        mainDispatcherCoroutineScope.launch {
            // 에러 감지
            repository.error.collect {
                _error.emit(it)
            }
        }
    }

}