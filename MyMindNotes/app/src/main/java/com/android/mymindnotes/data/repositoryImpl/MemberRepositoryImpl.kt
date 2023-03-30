package com.android.mymindnotes.data.repositoryImpl

import android.util.Log
import com.android.mymindnotes.data.datasources.MemberDataSource
import com.android.mymindnotes.data.datasources.SharedPreferencesDataSource
import com.android.mymindnotes.data.retrofit.model.UserInfo
import com.android.mymindnotes.data.retrofit.model.UserInfoLogin
import com.android.mymindnotes.domain.repositoryinterfaces.MemberRepository
import com.android.mymindnotes.hilt.module.IoDispatcher
import com.android.mymindnotes.hilt.module.MainDispatcherCoroutineScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val memberDataSource: MemberDataSource,
    private val sharedPreferencesDataSource: SharedPreferencesDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
) : MemberRepository {

    // 회원 정보 가져오기
    // 받은 회원 정보 저장하는 플로우
    private val _userInfo = MutableStateFlow<Map<String, Object>>(emptyMap())
    override val userInfo = _userInfo.asStateFlow()

    // (서버) 회원 정보 가져오기
    override suspend fun getUserInfo() {
       memberDataSource.getUserInfo()
        Log.e("UserInfoCheck", "Repository - 함수콜")
    }

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

//    override suspend fun getUserInfo(): Flow<Map<String, Object>> = memberDataSource.userInfoFlow

    // 로그인
    override suspend fun login(email: String, password: String): Flow<Map<String, Object>> = memberDataSource.loginFlow(email, password)

    // 아이디, 비밀번호 중복 체크
    // 이메일
    // 이메일 중복 체크 결과 저장 플로우
    private val _emailCheckResult = MutableSharedFlow<Map<String, Object>>()
    override val emailCheckResult = _emailCheckResult.asSharedFlow()

    // (서버) 이메일 중복 체크
    override suspend fun checkEmail(emailInput: String) {
        withContext(ioDispatcher) {
            memberDataSource.checkEmailApi.checkEmail(emailInput).let { _emailCheckResult.emit(it) }
        }
    }

    // 닉네임
    // 닉네임 중복 체크 결과 저장 플로우
    private val _nickNameCheckResult = MutableSharedFlow<Map<String, Object>>()
    override val nickNameCheckResult = _nickNameCheckResult.asSharedFlow()

    // (서버) 닉네임 중복 체크
    override suspend fun checkNickName(nickNameInput: String) {
        withContext(ioDispatcher) {
            memberDataSource.checkNickNameApi.checkNickname(nickNameInput).let { _nickNameCheckResult.emit(it) }
        }
    }

    // 회원가입
    // 회원가입 결과 저장 플로우
    private val _joinResult = MutableSharedFlow<Map<String, Object>>()
    override val joinResult = _joinResult.asSharedFlow()

    // (서버) 회원가입
    override suspend fun join(email: String, nickname: String, password: String, birthyear: Int) {
        val user = UserInfo(email, nickname, password, birthyear)
        withContext(ioDispatcher) {
            memberDataSource.joinApi.addUser(user).let { _joinResult.emit(it) }
        }
    }

    // 회원탈퇴
    // 회원탈퇴 결과 저장 플로우
    private val _deleteUserResult = MutableSharedFlow<Map<String, Object>>()
    override val deleteUserResult = _deleteUserResult.asSharedFlow()

    // (서버) 회원탈퇴
    override suspend fun deleteUser() {
        withContext(ioDispatcher) {
            val userIndex = sharedPreferencesDataSource.sharedPreferenceforUser.getInt("userindex", 0)
            memberDataSource.deleteUserApi.deleteUser(userIndex).let { _deleteUserResult.emit(it) }
        }
    }

    // 에러
    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>(replay = 1)
    override val error = _error.asSharedFlow()

}