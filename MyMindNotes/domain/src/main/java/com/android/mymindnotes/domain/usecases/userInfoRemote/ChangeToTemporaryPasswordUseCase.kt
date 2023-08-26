package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChangeToTemporaryPasswordUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke(email: String, randomPassword: String): Flow<Result<String?>> {

        return flow {
            try {
                val response = memberRepository.changeToTemporaryPassword(email, randomPassword).first()
                when (response.code) {
                    3007 -> emit(Result.failure(RuntimeException(response.msg)))
                    3006 -> emit(Result.success(response.msg))
                }
            } catch(e: Exception) {
                emit(Result.failure(RuntimeException("임시 비밀번호 발송에 실패했습니다. 인터넷 연결을 확인해 주세요.")))
            }
        }
    }
}


