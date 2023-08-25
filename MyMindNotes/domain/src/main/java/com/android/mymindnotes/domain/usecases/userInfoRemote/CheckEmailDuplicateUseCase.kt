package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckEmailDuplicateUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke(emailInput: String): Flow<Result<String?>> {

        return flow {
            try {
                val response = memberRepository.checkEmail(emailInput).first()
                when (response.code) {
                    1001 -> emit(Result.failure(RuntimeException(response.msg)))
                    1000 -> emit(Result.success(response.msg))
                }
            } catch(e: Exception) {
                emit(Result.failure(RuntimeException("이메일 중복 체크 실패. 인터넷 연결을 확인해 주세요.")))
            }
        }
    }

}