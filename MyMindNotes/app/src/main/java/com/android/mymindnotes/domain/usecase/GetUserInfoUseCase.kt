package com.android.mymindnotes.domain.usecase

import android.util.Log
import com.android.mymindnotes.domain.repositoryinterfaces.MemberRepository
import com.android.mymindnotes.hilt.module.IoDispatcherCoroutineScope
import com.android.mymindnotes.hilt.module.MainDispatcherCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    @MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
) {

    // 회원 정보 값 저장 플로우
    private val _userInfo = MutableStateFlow<Map<String, Object>>(emptyMap())
    val userInfo = _userInfo.asStateFlow()

    // 회원 정보 불러오기
    suspend fun getUserInfo() {
        memberRepository.getUserInfo()
        Log.e("UserInfoCheck", "UseCase - 함수콜")
    }

    init {
        mainDispatcherCoroutineScope.launch {
            // 회원정보 값 collect & emit
            memberRepository.userInfo.collect {
                _userInfo.value = it
                Log.e("UserInfoCheck", "UseCase - emit $it")
            }
        }
    }

}