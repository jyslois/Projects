package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChangeToTemporaryPasswordUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {


//    // 임시 비밀번호로 비밀번호 변경하기
//    suspend fun changeToTemporaryPassword(email: String, randomPassword: String): Flow<Map<String, Object>> {
//        return repository.changeToTemporaryPassword(email, randomPassword)
//    }

    suspend operator fun invoke(
        email: String,
        randomPassword: String
    ): Flow<Result<String>> {
        return memberRepository.changeToTemporaryPassword(email, randomPassword).map { response ->
            when (response.code) {
                3007 -> Result.failure(RuntimeException(response.msg))
                3006 -> Result.success(response.msg)
                else -> Result.failure(RuntimeException("임시 비밀번호 발송 중 오류 발생"))
            }
        }.catch {
            emit(Result.failure(RuntimeException("임시 비밀번호 발송에 실패했습니다. 인터넷 연결을 확인해 주세요.")))
        }

    }
}


