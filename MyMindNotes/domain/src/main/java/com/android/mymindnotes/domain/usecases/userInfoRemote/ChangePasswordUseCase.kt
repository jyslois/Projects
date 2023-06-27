package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.core.hilt.coroutineModules.MainDispatcherCoroutineScope
import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val repository: MemberRepository,
    @MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
) {

//    // 비밀번호 바꾸기
//    suspend fun changePassword(password: String, originalPassword: String): Flow<Map<String, Object>> {
//        return repository.changePassword(password, originalPassword)
//    }

    suspend operator fun invoke(password: String, originalPassword: String): Flow<Map<String, Object>> {
        return repository.changePassword(password, originalPassword)
    }
}