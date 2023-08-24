package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CheckNickNameDuplicateUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke(nickNameInput: String): Flow<Result<String?>> {
        return try {
            val response = memberRepository.checkNickName(nickNameInput).first()
            when(response.code) {
                1003 -> flowOf(Result.failure(RuntimeException(response.msg)))
                1002 -> flowOf(Result.success(response.msg))
                else -> flowOf(Result.failure(RuntimeException("닉네임 체크 중 오류 발생")))
            }
        } catch(e: Exception) {
            flowOf(Result.failure(RuntimeException(e.message ?: "닉네임 중복 체크 실패. 인터넷 연결을 확인해 주세요.")))
        }
    }
}