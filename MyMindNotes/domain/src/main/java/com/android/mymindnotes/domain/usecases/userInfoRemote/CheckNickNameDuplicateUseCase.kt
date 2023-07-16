package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CheckNickNameDuplicateUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {

//    // 닉네임
//    // (서버) 닉네임 중복 체크 함수 호출
//    suspend fun checkNickName(nickNameInput: String): Flow<Map<String, Object>> {
//        return repository.checkNickName(nickNameInput)
//    }

    suspend operator fun invoke(nickNameInput: String): Flow<Result<String>> {
        return memberRepository.checkNickName(nickNameInput).map { response ->
            when(response["code"].toString().toDouble()) {
                1003.0 -> Result.failure(RuntimeException(response["msg"] as String))
                1002.0 -> Result.success("Success")
                else -> Result.failure(RuntimeException("닉네임 체크 중 오류 발생"))
            }
        }.catch {
            emit(Result.failure(RuntimeException("닉네임 중복 체크 실패. 인터넷 연결을 확인해 주세요.")))
        }
    }
}