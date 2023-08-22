package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke(): Flow<Result<UserInfo>> {
        // UserInfo 객체를 담고 있는 성공의 Result 또는 예외를 담고 있는 실패의 Result를 가진 Flow를 비동기적으로 반환한다
        return flow {
            try {
                val response = memberRepository.getUserInfo().first() // GetUserInfoResponse를 가져온다
                val nickname = response.nickname
                val email = response.email
                val birthyear = response.birthyear

                emit(Result.success(UserInfo(nickname, email, birthyear))) // UserInfo 객체를 담고 있는 성공한 Result 객체를 flow에 emit한다
            } catch (e: Exception) {
                emit(Result.failure<UserInfo>(RuntimeException(e.message ?: "서버와의 통신에 실패했습니다. 인터넷 연결을 확인해 주세요."))) // RuntimeException 예외를 담고 있는 실패한 Result 객체를 flow에 emit한다
            }
        }
    }
}

data class UserInfo(val nickname: String, val email: String, val birthyear: Int)