package com.android.mymindnotes.domain.usecase

import com.android.mymindnotes.domain.repositoryinterfaces.MemberRepository
import com.android.mymindnotes.hilt.module.IoDispatcherCoroutineScope
import com.android.mymindnotes.hilt.module.MainDispatcherCoroutineScope
import com.bumptech.glide.Glide.init
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class JoinUseCase @Inject constructor(
    private val repository: MemberRepository,
    @MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
) {

    // (서버) 회원 가입 함수 호출
    suspend fun join(email: String, nickname: String, password: String, birthyear: Int) {
        repository.join(email, nickname, password, birthyear)
    }

    // 회원가입 결과 저장 플로우
    private val _joinResult = MutableSharedFlow<Map<String, Object>>()
    val joinResult = _joinResult.asSharedFlow()

    init {
        mainDispatcherCoroutineScope.launch {
            // 회원가입 결과 플로우 구독
            launch {
                repository.joinResult.collect {
                    _joinResult.emit(it)
                }
            }
        }
    }

}