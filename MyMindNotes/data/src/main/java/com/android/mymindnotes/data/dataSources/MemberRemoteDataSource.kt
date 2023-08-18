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
import javax.inject.Singleton

@Singleton
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
    override suspend fun login(userInfoLogin: UserInfoLogin): Flow<LoginResponse> = flow {
        val result = loginApi.login(userInfoLogin)
        emit(result)
    }.flowOn(ioDispatcher)

    // 이메일 중복 체크
    override suspend fun emailDuplicateCheck(emailInput: String): Flow<DuplicateCheckResponse> = flow {
        val result = checkEmailApi.checkEmail(emailInput)
        emit(result)
    }.flowOn(ioDispatcher)

    // 닉네임 중복 체크
    override suspend fun nickNameDuplicateCheck(nickNameInput: String): Flow<DuplicateCheckResponse> = flow {
        val result = checkNickNameApi.checkNickname(nickNameInput)
        emit(result)
    }.flowOn(ioDispatcher)


    // 회원가입
    override suspend fun join(userInfo: UserInfo): Flow<JoinResponse> = flow {
        val result = joinApi.addUser(userInfo)
        emit(result)
    }.flowOn(ioDispatcher)

    // 회원탈퇴
    override suspend fun deleteUser(userIndex: Int): Flow<DeleteUserResponse> = flow {
        val result = deleteUserApi.deleteUser(userIndex)
        emit(result)
    }.flowOn(ioDispatcher)

    // 회원 정보 수정
    // 닉네임 수정
    override suspend fun changeNickName(nicknameInfo: ChangeUserNickname): Flow<ChangeNicknameResponse> =
        flow {
            val result = changeNicknameApi.updateUserNickname(nicknameInfo)
            emit(result)
        }.flowOn(ioDispatcher)


    // 비밀번호 수정
    override suspend fun changePassword(passwordInfo: ChangeUserPassword): Flow<ChangePasswordResponse> = flow {
        val result = changePasswordApi.updateUserPassword(passwordInfo)
        emit(result)
    }.flowOn(ioDispatcher)



    // 임시 비밀번호로 비밀번호 수정
    override suspend fun changeToTemporaryPassword(temporaryPasswordInfo: ChangeToTemporaryPassword): Flow<ChangeToTemporaryPasswordResponse> = flow {
        val result = changeToTempPasswordApi.toTemPassword(temporaryPasswordInfo)
        emit(result)
    }.flowOn(ioDispatcher)


}

interface MemberRemoteDataSourceInterface {
    suspend fun getUserInfo(userIndex: Int): Flow<GetUserInfoResponse>
    suspend fun login(userInfoLogin: UserInfoLogin): Flow<LoginResponse>
    suspend fun emailDuplicateCheck(emailInput: String): Flow<DuplicateCheckResponse>
    suspend fun nickNameDuplicateCheck(nickNameInput: String): Flow<DuplicateCheckResponse>
    suspend fun join(userInfo: UserInfo): Flow<JoinResponse>
    suspend fun deleteUser(userIndex: Int): Flow<DeleteUserResponse>
    suspend fun changeNickName(nicknameInfo: ChangeUserNickname): Flow<ChangeNicknameResponse>
    suspend fun changePassword(passwordInfo: ChangeUserPassword): Flow<ChangePasswordResponse>
    suspend fun changeToTemporaryPassword(temporaryPasswordInfo: ChangeToTemporaryPassword): Flow<ChangeToTemporaryPasswordResponse>
}