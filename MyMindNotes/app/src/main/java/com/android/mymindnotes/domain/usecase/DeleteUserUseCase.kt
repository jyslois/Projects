package com.android.mymindnotes.domain.usecase

import com.android.mymindnotes.domain.repositoryinterfaces.MemberRepository
import com.android.mymindnotes.hilt.module.IoDispatcherCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    @IoDispatcherCoroutineScope private val ioDispatcherCoroutineScope: CoroutineScope
) {

    // 회원탈퇴 함수 콜
    suspend fun deleteUser() {
        memberRepository.deleteUser()
    }

    // 회원탈퇴 결과 저장 플로우
    private val _deleteUserResult = MutableSharedFlow<Map<String, Object>>()
    val deleteUserResult = _deleteUserResult.asSharedFlow()

    // 회원탈퇴 결과 collect & emit
    init {
        ioDispatcherCoroutineScope.launch {
            memberRepository.deleteUserResult.collect {
                _deleteUserResult.emit(it)
            }
        }
    }
}