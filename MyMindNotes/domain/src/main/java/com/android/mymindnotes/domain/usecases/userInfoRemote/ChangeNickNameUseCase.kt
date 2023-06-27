package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.core.hilt.coroutineModules.MainDispatcherCoroutineScope
import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChangeNickNameUseCase @Inject constructor(
    private val repository: MemberRepository,
    @MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
) {

//    // 닉네임 바꾸기
//    suspend fun changeNickName(nickName: String): Flow<Map<String, Object>> {
//        return repository.changeNickName(nickName)
//    }

    suspend operator fun invoke(nickName: String): Flow<Map<String, Object>> {
        return repository.changeNickName(nickName)
    }
}