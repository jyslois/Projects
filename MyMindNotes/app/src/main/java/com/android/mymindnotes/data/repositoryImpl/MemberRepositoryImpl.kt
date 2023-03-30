package com.android.mymindnotes.data.repositoryImpl

import android.util.Log
import com.android.mymindnotes.data.datasources.MemberDataSource
import com.android.mymindnotes.domain.repositoryinterfaces.MemberRepository
import com.android.mymindnotes.hilt.module.MainDispatcherCoroutineScope
import com.bumptech.glide.Glide.init
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val memberDataSource: MemberDataSource,
    @MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
) : MemberRepository {

    // 로그인
    override suspend fun login(email: String, password: String): Flow<Map<String, Object>> = memberDataSource.loginResultFlow(email, password)

    // 아이디, 비밀번호 중복 체크
    // 이메일 중복 체크
    override suspend fun checkEmail(emailInput: String): Flow<Map<String, Object>> = memberDataSource.emailCheckFlow(emailInput)

    // 닉네임 중복 체크
    override suspend fun checkNickName(nickNameInput: String): Flow<Map<String, Object>> = memberDataSource.nickNameCheckFlow(nickNameInput)

    // 회원가입
    override suspend fun join(email: String, nickname: String, password: String, birthyear: Int): Flow<Map<String, Object>> = memberDataSource.joinResultFlow(email, nickname, password, birthyear)

    // 회원탈퇴
    override suspend fun deleteUser(): Flow<Map<String, Object>> = memberDataSource.deleteUserResultFlow

    // 회원 정보 가져오기
    // 받은 회원 정보 저장하는 플로우
    private val _userInfo = MutableStateFlow<Map<String, Object>>(emptyMap())
    override val userInfo = _userInfo.asStateFlow()

    // (서버) 회원 정보 가져오기
    override suspend fun getUserInfo() {
        memberDataSource.getUserInfo()
        Log.e("UserInfoCheck", "Repository - 함수콜")
    }

    // 에러
    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>(replay = 1)
    override val error = _error.asSharedFlow()

    // 회원 정보 수정
    // 닉네임 수정
    override suspend fun changeNickName(nickName: String): Flow<Map<String, Object>> = memberDataSource.changeNickNameFlow(nickName)

    // 비밀번호 수정
    override suspend fun changePassword(password: String, originalPassword: String): Flow<Map<String, Object>> = memberDataSource.changePasswordFlow(password, originalPassword)

    init {
        mainDispatcherCoroutineScope.launch {
            launch {
                // 회원정보 값 collect & emit
                memberDataSource.userInfo.collect {
                    _userInfo.value = it
                    Log.e("UserInfoCheck", "Repository - emit $it")
                }
            }

            launch {
                // 에러 메시지 collect & emit
                memberDataSource.error.collect {
                    _error.emit(it)
                }
            }
        }
    }

}