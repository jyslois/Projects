package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CheckEmailDuplicateUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {

//    // 이메일
//    // (서버) 이메일 중복 체크 함수 호출
//    suspend fun checkEmail(emailInput: String): Flow<Map<String, Object>> {
//        return repository.checkEmail(emailInput)
//    }

    suspend operator fun invoke(emailInput: String): Flow<Result<String>> {
        return memberRepository.checkEmail(emailInput).map { response ->
            when (response.code) {
                1001 -> Result.failure(RuntimeException(response.msg))
                1000 -> Result.success("Success")
                else -> Result.failure(RuntimeException("이메일 중복 체크 중 오류 발생"))
            }
        }.catch {
            emit(Result.failure(RuntimeException("이메일 중복 체크에 실패했습니다. 인터넷 연결을 확인해 주세요.")))
        }
    }

}