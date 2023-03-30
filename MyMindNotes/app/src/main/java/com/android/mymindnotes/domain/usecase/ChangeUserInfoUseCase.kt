package com.android.mymindnotes.domain.usecase

import com.android.mymindnotes.domain.repositoryinterfaces.MemberRepository
import com.android.mymindnotes.hilt.module.MainDispatcherCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChangeUserInfoUseCase @Inject constructor(
    private val repository: MemberRepository,
    @MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
) {

    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>(replay = 1)
    val error = _error.asSharedFlow()

    // 닉네임 바꾸기
    suspend fun changeNickName(nickName: String): Flow<Map<String, Object>> {
        return repository.changeNickName(nickName)
    }

    // 비밀번호 바꾸기
    suspend fun changePassword(password: String, originalPassword: String): Flow<Map<String, Object>> {
        return repository.changePassword(password, originalPassword)
    }

    // 임시 비밀번호로 비밀번호 변경하기
    suspend fun changeToTemporaryPassword(email: String, randomPassword: String): Flow<Map<String, Object>> {
        return repository.changeToTemporaryPassword(email, randomPassword)
    }

    init {
        mainDispatcherCoroutineScope.launch {
            repository.error.collect {
                _error.emit(it)
            }
        }
    }

}