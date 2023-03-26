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

class DeleteUserUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    @MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
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
        mainDispatcherCoroutineScope.launch {
            memberRepository.deleteUserResult.collect {
                _deleteUserResult.emit(it)
            }
        }
    }
}