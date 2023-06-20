package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.core.hilt.coroutineModules.MainDispatcherCoroutineScope
import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    @com.android.mymindnotes.core.hilt.coroutineModules.MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
) {

//    // 회원탈퇴 함수 콜
//    suspend fun deleteUser(): Flow<Map<String, Object>> {
//        return memberRepository.deleteUser()
//    }

    suspend operator fun invoke(): Flow<Map<String, Object>> {
        return memberRepository.deleteUser()
    }

    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>(replay = 1)
    val error = _error.asSharedFlow()

    init {
        mainDispatcherCoroutineScope.launch {
            // 에러 감지
            memberRepository.error.collect {
                _error.emit(it)
            }
        }
    }
}