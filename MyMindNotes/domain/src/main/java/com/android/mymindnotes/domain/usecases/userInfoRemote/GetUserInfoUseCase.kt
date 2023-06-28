package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {


//    // 회원 정보 불러오기
//    suspend fun getUserInfo(): Flow<Map<String, Object>> = memberRepository.getUserInfo()

    suspend operator fun invoke(): Flow<Map<String, Object>> = memberRepository.getUserInfo()


}