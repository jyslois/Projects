package com.android.mymindnotes.domain.usecases

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JoinUseCase @Inject constructor(
    private val repository: MemberRepository
) {

//    // (서버) 회원 가입 함수 호출
//    suspend fun join(email: String, nickname: String, password: String, birthyear: Int): Flow<Map<String, Object>> {
//        return repository.join(email, nickname, password, birthyear)
//    }

    suspend operator fun invoke(email: String, nickname: String, password: String, birthyear: Int): Flow<Map<String, Object>> {
        return repository.join(email, nickname, password, birthyear)
    }


}