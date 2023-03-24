package com.android.mymindnotes.domain.usecase

import com.android.mymindnotes.domain.repositoryinterfaces.MemberRepository
import com.android.mymindnotes.hilt.module.IoDispatcherCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    @IoDispatcherCoroutineScope private val ioDispatcherCoroutineScope: CoroutineScope

) {

    // 회원 정보 불러오기
    suspend fun getUserInfo(): Flow<Map<String, Object>> {
        return memberRepository.getUserInfo()
    }

//    // 회원 정보 값 저장 플로우
//    private val _userInfo = MutableSharedFlow<Map<String, Object>>()
//    val userInfo: SharedFlow<Map<String, Object>> get() = _userInfo.asSharedFlow()
//
//    // 회원 정보 불러오기
//    suspend fun getUserInfo() {
//        memberRepository.getUserInfo()
//    }
//
//    init {
//        ioDispatcherCoroutineScope.launch {
//            // 회원정보 값 collect & emit
//            memberRepository.userInfo.collect {
//                _userInfo.emit(it)
//            }
//        }
//    }

}