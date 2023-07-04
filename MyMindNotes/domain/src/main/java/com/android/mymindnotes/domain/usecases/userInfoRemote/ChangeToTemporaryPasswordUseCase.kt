package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRemoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChangeToTemporaryPasswordUseCase @Inject constructor(
    private val repository: MemberRemoteRepository
) {


//    // 임시 비밀번호로 비밀번호 변경하기
//    suspend fun changeToTemporaryPassword(email: String, randomPassword: String): Flow<Map<String, Object>> {
//        return repository.changeToTemporaryPassword(email, randomPassword)
//    }

    suspend operator fun invoke(email: String, randomPassword: String): Flow<Map<String, Object>> {
        return repository.changeToTemporaryPassword(email, randomPassword)
    }
}