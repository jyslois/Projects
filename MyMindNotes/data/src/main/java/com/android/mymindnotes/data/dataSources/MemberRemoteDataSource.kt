package com.android.mymindnotes.data.dataSources

import com.android.mymindnotes.core.dto.ChangeNicknameResponse
import com.android.mymindnotes.core.dto.ChangePasswordResponse
import com.android.mymindnotes.core.dto.DeleteUserResponse
import com.android.mymindnotes.core.dto.DuplicateCheckResponse
import com.android.mymindnotes.core.dto.GetUserInfoResponse
import com.android.mymindnotes.core.dto.JoinResponse
import com.android.mymindnotes.core.dto.LoginResponse
import com.android.mymindnotes.data.retrofit.api.user.*
import com.android.mymindnotes.core.dto.ChangeToTemporaryPassword
import com.android.mymindnotes.core.dto.ChangeToTemporaryPasswordResponse
import com.android.mymindnotes.core.dto.ChangeUserNickname
import com.android.mymindnotes.core.dto.ChangeUserPassword
import com.android.mymindnotes.core.dto.UserInfo
import com.android.mymindnotes.core.dto.UserInfoLogin
import com.android.mymindnotes.core.hilt.coroutineModules.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MemberRemoteDataSource @Inject constructor(
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
): MemberRemoteDataSourceInterface {

    // (서버) 회원 정보 가져오기
    override suspend fun getUserInfo(userIndex: Int): Flow<GetUserInfoResponse> = flow {
        val result = getUserInfoApi.getUserInfo(userIndex)
        emit(result)
    }.flowOn(ioDispatcher)


    // 로그인
    override suspend fun loginResultFlow(email: String, password: String): Flow<LoginResponse> = flow {
        val user = UserInfoLogin(email, password)
        val result = loginApi.login(user)
        emit(result)
    }.flowOn(ioDispatcher)

    // 이메일 중복 체크
    override suspend fun emailCheckFlow(emailInput: String): Flow<DuplicateCheckResponse> = flow {
        val result = checkEmailApi.checkEmail(emailInput)
        emit(result)
    }.flowOn(ioDispatcher)

    // 닉네임 중복 체크
    override suspend fun nickNameCheckFlow(nickNameInput: String): Flow<DuplicateCheckResponse> = flow {
        val result = checkNickNameApi.checkNickname(nickNameInput)
        emit(result)
    }.flowOn(ioDispatcher)


    // 회원가입
    override suspend fun joinResultFlow(
        email: String,
        nickname: String,
        password: String,
        birthyear: Int
    ): Flow<JoinResponse> = flow {
        val user = UserInfo(
            email,
            nickname,
            password,
            birthyear
        )
        val result = joinApi.addUser(user)
        emit(result)
    }.flowOn(ioDispatcher)

    // 회원탈퇴
    override suspend fun deleteUserResultFlow(userIndex: Int): Flow<DeleteUserResponse> = flow {
        val result = deleteUserApi.deleteUser(userIndex)
        emit(result)
    }.flowOn(ioDispatcher)

    // 회원 정보 수정
    // 닉네임 수정
    override suspend fun changeNickNameFlow(userIndex: Int, nickName: String): Flow<ChangeNicknameResponse> =
        flow {
            val user = ChangeUserNickname(
                userIndex,
                nickName
            )
            val result = changeNicknameApi.updateUserNickname(user)
            emit(result)
        }.flowOn(ioDispatcher)


    // 비밀번호 수정
    override suspend fun changePasswordFlow(
        userIndex: Int,
        password: String,
        originalPassword: String
    ): Flow<ChangePasswordResponse> = flow {
        val user = ChangeUserPassword(userIndex, password, originalPassword)
        val result = changePasswordApi.updateUserPassword(user)
        emit(result)
    }.flowOn(ioDispatcher)



    // 임시 비밀번호로 비밀번호 수정
    override suspend fun changeToTemporaryPasswordFlow(
        email: String,
        randomPassword: String
    ): Flow<ChangeToTemporaryPasswordResponse> = flow {
        val user =
            ChangeToTemporaryPassword(
                email,
                randomPassword
            )
        val result = changeToTempPasswordApi.toTemPassword(user)
        emit(result)
    }.flowOn(ioDispatcher)


}

interface MemberRemoteDataSourceInterface {
    suspend fun getUserInfo(userIndex: Int): Flow<GetUserInfoResponse>
    suspend fun loginResultFlow(email: String, password: String): Flow<LoginResponse>
    suspend fun emailCheckFlow(emailInput: String): Flow<DuplicateCheckResponse>
    suspend fun nickNameCheckFlow(nickNameInput: String): Flow<DuplicateCheckResponse>
    suspend fun joinResultFlow(
        email: String,
        nickname: String,
        password: String,
        birthyear: Int
    ): Flow<JoinResponse>
    suspend fun deleteUserResultFlow(userIndex: Int): Flow<DeleteUserResponse>
    suspend fun changeNickNameFlow(userIndex: Int, nickName: String): Flow<ChangeNicknameResponse>
    suspend fun changePasswordFlow(
        userIndex: Int,
        password: String,
        originalPassword: String
    ): Flow<ChangePasswordResponse>
    suspend fun changeToTemporaryPasswordFlow(
        email: String,
        randomPassword: String
    ): Flow<ChangeToTemporaryPasswordResponse>
}