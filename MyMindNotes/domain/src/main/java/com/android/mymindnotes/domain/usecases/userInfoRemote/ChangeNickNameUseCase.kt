package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChangeNickNameUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke(nickName: String): Flow<Result<String?>> {
        return flow {
            try {
                val response = memberRepository.changeNickName(nickName).first()
                when (response.code) {
                    3001 -> emit(Result.failure(RuntimeException(response.msg)))
                    3000 -> emit(Result.success(response.msg))
                }
            } catch(e: Exception) {
                emit(Result.failure(RuntimeException("닉네임 변경 실패. 인터넷 연결을 확인해 주세요.")))
            }
        }
    }
}
