package com.android.mymindnotes.domain.usecase

import com.android.mymindnotes.domain.repositoryinterfaces.MemberRepository
import com.android.mymindnotes.hilt.module.MainDispatcherCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DuplicateCheckUseCase @Inject constructor(
    private val repository: MemberRepository,
    @MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
) {
    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>(replay = 1)
    val error = _error.asSharedFlow()

    // 이메일
    // (서버) 이메일 중복 체크 함수 호출
    suspend fun checkEmail(emailInput: String): Flow<Map<String, Object>> {
        return repository.checkEmail(emailInput)
    }

    // 닉네임
    // (서버) 닉네임 중복 체크 함수 호출
    suspend fun checkNickName(nickNameInput: String): Flow<Map<String, Object>> {
        return repository.checkNickName(nickNameInput)
    }

    init {
        mainDispatcherCoroutineScope.launch {
            repository.error.collect {
                _error.emit(it)
            }
        }
    }
}