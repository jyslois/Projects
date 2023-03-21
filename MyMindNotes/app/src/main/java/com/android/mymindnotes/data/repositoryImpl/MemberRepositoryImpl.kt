package com.android.mymindnotes.data.repositoryImpl

import com.android.mymindnotes.data.datasources.MemberDataSource
import com.android.mymindnotes.data.datasources.SharedPreferencesDataSource
import com.android.mymindnotes.data.retrofit.model.UserInfo
import com.android.mymindnotes.data.retrofit.model.UserInfoLogin
import com.android.mymindnotes.domain.repositoryinterfaces.MemberRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val memberDataSource: MemberDataSource,
    private val sharedPreferencesDataSource: SharedPreferencesDataSource
): MemberRepository {

    // 회원 정보 가져오기
    // 회원 정보 값 저장 플로우
    val _userInfo = MutableSharedFlow<Map<String, Object>>()
    override val userInfo = _userInfo.asSharedFlow()
    // (서버) 회원 정보 가져오기
    override suspend fun getUserInfo() {
        val userIndex = sharedPreferencesDataSource.sharedPreferenceforUser.getInt("userindex", 0)
        memberDataSource.getUserInfoApi.getUserInfo(userIndex).let { _userInfo.emit(it) }
    }

    // 로그인, 로그아웃
    // 로그인
    // 로그인 결과 저장 플로우
    private val _logInResult = MutableSharedFlow<Map<String, Object>>()
    override val logInResult = _logInResult.asSharedFlow()

    // (서버) 로그인
    override suspend fun login(email: String, password: String) {
        val user = UserInfoLogin(email, password)
        val result = memberDataSource.loginApi.login(user)
        _logInResult.emit(result)

    }

    // 아이디, 비밀번호 중복 체크
    // 이메일
    // 이메일 중복 체크 결과 저장 플로우
    private val _emailCheckResult = MutableSharedFlow<Map<String, Object>>()
    override val emailCheckResult = _emailCheckResult.asSharedFlow()

    // (서버) 이메일 중복 체크
    override suspend fun checkEmail(emailInput: String) {
        val result = memberDataSource.checkEmailApi.checkEmail(emailInput)
        _emailCheckResult.emit(result)
    }

    // 닉네임
    // 닉네임 중복 체크 결과 저장 플로우
    private val _nickNameCheckResult = MutableSharedFlow<Map<String, Object>>()
    override val nickNameCheckResult = _nickNameCheckResult.asSharedFlow()

    // (서버) 닉네임 중복 체크
    override suspend fun checkNickName(nickNameInput: String) {
        val result = memberDataSource.checkNickNameApi.checkNickname(nickNameInput)
        _nickNameCheckResult.emit(result)
    }

    // 회원가입
    // 회원가입 결과 저장 플로우
    private val _joinResult = MutableSharedFlow<Map<String, Object>>()
    override val joinResult = _joinResult.asSharedFlow()

    // (서버) 회원가입
    override suspend fun join(email: String, nickname: String, password: String, birthyear: Int) {
        val user = UserInfo(email, nickname, password, birthyear)
        val result = memberDataSource.joinApi.addUser(user)
        _joinResult.emit(result)
    }


}