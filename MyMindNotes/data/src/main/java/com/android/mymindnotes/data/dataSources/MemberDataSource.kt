package com.android.mymindnotes.data.dataSources

import com.android.mymindnotes.data.retrofit.api.user.*
import com.android.mymindnotes.data.retrofit.model.user.ChangeToTemporaryPassword
import com.android.mymindnotes.data.retrofit.model.user.ChangeUserNickname
import com.android.mymindnotes.data.retrofit.model.user.ChangeUserPassword
import com.android.mymindnotes.data.retrofit.model.user.UserInfo
import com.android.mymindnotes.data.retrofit.model.user.UserInfoLogin
import com.android.mymindnotes.core.hilt.coroutineModules.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MemberDataSource @Inject constructor(
    private val loginApi: LoginApi,
    private val checkEmailApi: CheckEmailApi,
    private val checkNickNameApi: CheckNickNameApi,
    private val joinApi: JoinApi,
    private val getUserInfoApi: GetUserInfoApi,
    private val deleteUserApi: DeleteUserApi,
    private val changeNicknameApi: ChangeNicknameApi,
    private val changePasswordApi: ChangePasswordApi,
    private val changeToTempPasswordApi: ChangeToTempPasswordApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>(replay = 1)
    val error = _error.asSharedFlow()

    // (서버) 회원 정보 가져오기
    suspend fun getUserInfo(userIndex: Int): Flow<Map<String, Object>> = flow {
        val result = getUserInfoApi.getUserInfo(userIndex)
        emit(result)
    }.flowOn(ioDispatcher)


    // 로그인
    suspend fun loginResultFlow(email: String, password: String): Flow<Map<String, Object>> = flow {
        val user = UserInfoLogin(email, password)
        val result = loginApi.login(user)
        emit(result)
    }.flowOn(ioDispatcher)
        .catch {
            _error.emit(true)
            _error.emit(false)
            // 이렇게 설정해 주어야 로그인을 시도할 때마다 반응하면서도, 화면 재진입 시에 자동으로 다이얼로그가 뜨지 않고(true를 replay해주지 않고), 인터넷이 갑자기 됐을 때 로그인 성공이 돼어 다른 화면으로 넘어가도
            // error가 false로 마지막으로 emit되기 때문에 다른 화면에 영향을 미치지 않는다. (flow는 collect될 때마다 처음부터 차례대로 하나 하나 다 emit함)
        }

    // 이메일 중복 체크
    suspend fun emailCheckFlow(emailInput: String): Flow<Map<String, Object>> = flow {
        val result = checkEmailApi.checkEmail(emailInput)
        emit(result)
    }.flowOn(ioDispatcher)
        .catch {
            _error.emit(true)
            _error.emit(false)
        }

    // 닉네임 중복 체크
    suspend fun nickNameCheckFlow(nickNameInput: String): Flow<Map<String, Object>> = flow {
        val result = checkNickNameApi.checkNickname(nickNameInput)
        emit(result)
    }.flowOn(ioDispatcher)
        .catch {
            _error.emit(true)
            _error.emit(false)
        }

    // 회원가입
    suspend fun joinResultFlow(
        email: String,
        nickname: String,
        password: String,
        birthyear: Int
    ): Flow<Map<String, Object>> = flow {
        val user = UserInfo(
            email,
            nickname,
            password,
            birthyear
        )
        val result = joinApi.addUser(user)
        emit(result)
    }.flowOn(ioDispatcher)
        .catch {
            _error.emit(true)
            _error.emit(false)
        }

    // 회원탈퇴
    suspend fun deleteUserResultFlow(userIndex: Int): Flow<Map<String, Object>> = flow {
        val result = deleteUserApi.deleteUser(userIndex)
        emit(result)
    }.flowOn(ioDispatcher)

    // 회원 정보 수정
    // 닉네임 수정
    suspend fun changeNickNameFlow(userIndex: Int, nickName: String): Flow<Map<String, Object>> =
        flow {
            val user = ChangeUserNickname(
                userIndex,
                nickName
            )
            val result = changeNicknameApi.updateUserNickname(user)
            emit(result)
        }.flowOn(ioDispatcher)
            .catch {
                _error.emit(true)
                _error.emit(false)
            }

    // 비밀번호 수정
    suspend fun changePasswordFlow(
        userIndex: Int,
        password: String,
        originalPassword: String
    ): Flow<Map<String, Object>> = flow {
        val user = ChangeUserPassword(userIndex, password, originalPassword)
        val result = changePasswordApi.updateUserPassword(user)
        emit(result)
    }.flowOn(ioDispatcher)
        .catch {
            _error.emit(true)
            _error.emit(false)
        }


    // 임시 비밀번호로 비밀번호 수정
    suspend fun changeToTemporaryPasswordFlow(
        email: String,
        randomPassword: String
    ): Flow<Map<String, Object>> = flow<Map<String, Object>> {
        val user =
            ChangeToTemporaryPassword(
                email,
                randomPassword
            )
        val result = changeToTempPasswordApi.toTemPassword(user)
        emit(result)
    }.flowOn(ioDispatcher)
        .catch {
            _error.emit(true)
            _error.emit(false)
        }

}