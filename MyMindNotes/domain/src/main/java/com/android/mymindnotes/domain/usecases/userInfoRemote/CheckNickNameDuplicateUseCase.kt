package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.core.hilt.coroutineModules.MainDispatcherCoroutineScope
import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CheckNickNameDuplicateUseCase @Inject constructor(
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

//    // 닉네임
//    // (서버) 닉네임 중복 체크 함수 호출
//    suspend fun checkNickName(nickNameInput: String): Flow<Map<String, Object>> {
//        return repository.checkNickName(nickNameInput)
//    }

    suspend operator fun invoke(nickNameInput: String): Flow<Map<String, Object>> {
        return repository.checkNickName(nickNameInput)
    }
}