package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {


//    // 회원 정보 불러오기
//    suspend fun getUserInfo(): Flow<Map<String, Object>> = memberRemoteRepository.getUserInfo()

    suspend operator fun invoke(): Flow<Result<UserInfo>> {
        return memberRepository.getUserInfo().map { response ->
            val nickname = response["nickname"] as String
            val email = response["email"] as String
            val birthyear = response["birthyear"].toString().toDouble().toInt().toString()
            Result.success(UserInfo(nickname, email, birthyear))
        }.catch {
            emit(Result.failure(RuntimeException("서버와의 통신에 실패했습니다. 인터넷 연결을 확인해 주세요.")))
        }
    }
}

data class UserInfo(val nickname: String, val email: String, val birthyear: String)