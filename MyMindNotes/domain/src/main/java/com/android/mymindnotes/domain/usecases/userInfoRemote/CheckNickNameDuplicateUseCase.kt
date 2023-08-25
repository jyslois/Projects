package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckNickNameDuplicateUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke(nickNameInput: String): Flow<Result<String?>> {
        return flow {
            try {
                val response = memberRepository.checkNickName(nickNameInput).first()
                when(response.code) {
                    1003 -> emit(Result.failure(RuntimeException(response.msg)))
                    1002 -> emit(Result.success(response.msg))
                }
            } catch(e: Exception) {
                emit(Result.failure(RuntimeException("닉네임 중복 체크 실패. 인터넷 연결을 확인해 주세요.")))
            }
        }
    }
}