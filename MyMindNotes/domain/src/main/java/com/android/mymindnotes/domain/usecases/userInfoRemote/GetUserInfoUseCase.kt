package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRemoteRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val memberRemoteRepository: MemberRemoteRepository
) {


//    // 회원 정보 불러오기
//    suspend fun getUserInfo(): Flow<Map<String, Object>> = memberRemoteRepository.getUserInfo()

    suspend operator fun invoke(): Flow<Map<String, Object>> = memberRemoteRepository.getUserInfo()


}