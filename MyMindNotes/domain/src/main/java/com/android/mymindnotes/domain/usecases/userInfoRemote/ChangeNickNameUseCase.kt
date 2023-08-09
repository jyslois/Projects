package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChangeNickNameUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {

//    // 닉네임 바꾸기
//    suspend fun changeNickName(nickName: String): Flow<Map<String, Object>> {
//        return repository.changeNickName(nickName)
//    }
    suspend operator fun invoke(nickName: String): Flow<Result<String?>> {
        return memberRepository.changeNickName(nickName).map { response ->
            when (response.code) {
                3001 -> Result.failure(RuntimeException(response.msg))
                3000 -> Result.success(response.msg)
                else -> Result.failure(RuntimeException("닉네임 변경 중 오류 발생."))
            }
        }.catch {
            emit(Result.failure(RuntimeException("닉네임 변경 실패. 인터넷 연결을 확인해 주세요.")))
        }
    }

}
