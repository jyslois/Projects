package com.android.mymindnotes.domain.usecase

import com.android.mymindnotes.domain.repositoryinterfaces.DuplicateCheckRepository
import com.android.mymindnotes.hilt.module.IoDispatcherCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DuplicateCheckUseCase @Inject constructor(
    private val duplicateCheckRepository: DuplicateCheckRepository,
    @IoDispatcherCoroutineScope private val ioDispatcherCoroutineScope: CoroutineScope
) {
    // SharedFlow
    // 이메일 중복 체크 결과 저장 플로우
    private val _emailCheckResult = MutableSharedFlow<Map<String, Object>>()
    val emailCheckResult = _emailCheckResult.asSharedFlow()

    // 이메일 중복 체크 함수 호출
    suspend fun checkEmail(emailInput: String) {
        duplicateCheckRepository.checkEmail(emailInput)
    }

    init {
        ioDispatcherCoroutineScope.launch {
            // 이메일 중복 체크 결과 플로우 구독
            launch {
                duplicateCheckRepository.emailCheckResult.collect {
                    _emailCheckResult.emit(it)
                }
            }
        }
    }
}