package com.android.mymindnotes.domain.usecase

import com.android.mymindnotes.domain.repositoryinterfaces.MemberRepository
import com.android.mymindnotes.hilt.module.IoDispatcherCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DuplicateCheckUseCase @Inject constructor(
    private val repository: MemberRepository,
    @IoDispatcherCoroutineScope private val ioDispatcherCoroutineScope: CoroutineScope
) {
    // 이메일
    // (서버) 이메일 중복 체크 함수 호출
    suspend fun checkEmail(emailInput: String) {
        repository.checkEmail(emailInput)
    }

    // 이메일 중복 체크 결과 저장 플로우
    private val _emailCheckResult = MutableSharedFlow<Map<String, Object>>()
    val emailCheckResult = _emailCheckResult.asSharedFlow()

    // 닉네임
    // (서버) 닉네임 중복 체크 함수 호출
    suspend fun checkNickName(nickNameInput: String) {
        repository.checkNickName(nickNameInput)
    }

    // 닉네임 중복 체크 결과 저장 플로우
    private val _nickNameCheckResult = MutableSharedFlow<Map<String, Object>>()
    val nickNameCheckResult = _nickNameCheckResult.asSharedFlow()

    init {
        ioDispatcherCoroutineScope.launch {
            // 이메일 중복 체크 결과 플로우 구독
            launch {
                repository.emailCheckResult.collect {
                    _emailCheckResult.emit(it)
                }
            }

            // 닉네임 중복 체크 결과 플로우 구독
            launch {
                repository.nickNameCheckResult.collect {
                    _nickNameCheckResult.emit(it)
                }
            }
        }
    }
}