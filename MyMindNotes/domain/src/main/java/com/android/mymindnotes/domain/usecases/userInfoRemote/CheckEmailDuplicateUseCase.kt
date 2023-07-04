package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRemoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckEmailDuplicateUseCase @Inject constructor(
    private val repository: MemberRemoteRepository
) {

//    // 이메일
//    // (서버) 이메일 중복 체크 함수 호출
//    suspend fun checkEmail(emailInput: String): Flow<Map<String, Object>> {
//        return repository.checkEmail(emailInput)
//    }

    suspend operator fun invoke(emailInput: String): Flow<Map<String, Object>> {
        return repository.checkEmail(emailInput)
    }
}