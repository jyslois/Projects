package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.core.hilt.coroutineModules.MainDispatcherCoroutineScope
import com.android.mymindnotes.data.repositoryInterfaces.MemberRemoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckNickNameDuplicateUseCase @Inject constructor(
    private val repository: MemberRemoteRepository,
    @MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
) {

//    // 닉네임
//    // (서버) 닉네임 중복 체크 함수 호출
//    suspend fun checkNickName(nickNameInput: String): Flow<Map<String, Object>> {
//        return repository.checkNickName(nickNameInput)
//    }

    suspend operator fun invoke(nickNameInput: String): Flow<Map<String, Object>> {
        return repository.checkNickName(nickNameInput)
    }
}