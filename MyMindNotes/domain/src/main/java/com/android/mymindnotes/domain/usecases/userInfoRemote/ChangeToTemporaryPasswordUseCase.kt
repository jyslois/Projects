package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.core.hilt.coroutineModules.MainDispatcherCoroutineScope
import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChangeToTemporaryPasswordUseCase @Inject constructor(
    private val repository: MemberRepository,
    @MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
) {

    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>(replay = 1)
    val error = _error.asSharedFlow()

    init {
        mainDispatcherCoroutineScope.launch {
            repository.error.collect {
                _error.emit(it)
            }
        }
    }

//    // 임시 비밀번호로 비밀번호 변경하기
//    suspend fun changeToTemporaryPassword(email: String, randomPassword: String): Flow<Map<String, Object>> {
//        return repository.changeToTemporaryPassword(email, randomPassword)
//    }

    suspend operator fun invoke(email: String, randomPassword: String): Flow<Map<String, Object>> {
        return repository.changeToTemporaryPassword(email, randomPassword)
    }
}