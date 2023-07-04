package com.android.mymindnotes.data.repositoryImpls

import com.android.mymindnotes.data.repositoryInterfaces.MemberRemoteRepository
import com.android.mymindnotes.data.dataSources.MemberLocalDataSourceInterface
import com.android.mymindnotes.data.dataSources.MemberRemoteDataSourceInterface
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MemberRemoteRepositoryImpl @Inject constructor(
    private val memberRemoteDataSource: MemberRemoteDataSourceInterface,
    private val memberLocalDataSource: MemberLocalDataSourceInterface
) : MemberRemoteRepository {

    // 로그인
    override suspend fun login(email: String, password: String): Flow<Map<String, Object>> = memberRemoteDataSource.loginResultFlow(email, password)

    // 아이디, 비밀번호 중복 체크
    // 이메일 중복 체크
    override suspend fun checkEmail(emailInput: String): Flow<Map<String, Object>> = memberRemoteDataSource.emailCheckFlow(emailInput)

    // 닉네임 중복 체크
    override suspend fun checkNickName(nickNameInput: String): Flow<Map<String, Object>> = memberRemoteDataSource.nickNameCheckFlow(nickNameInput)

    // 회원가입
    override suspend fun join(email: String, nickname: String, password: String, birthyear: Int): Flow<Map<String, Object>> = memberRemoteDataSource.joinResultFlow(email, nickname, password, birthyear)

    // 회원탈퇴
    override suspend fun deleteUser(): Flow<Map<String, Object>> {
        val userIndex = memberLocalDataSource.getUserIndexfromUserSharedPreferences().first()
        return memberRemoteDataSource.deleteUserResultFlow(userIndex)
    }

    // 회원 정보 가져오기
    // (서버) 회원 정보 가져오기
    override suspend fun getUserInfo(): Flow<Map<String, Object>> {
        val userIndex = memberLocalDataSource.getUserIndexfromUserSharedPreferences().first()
        return memberRemoteDataSource.getUserInfo(userIndex)
    }

    // 회원 정보 수정
    // 닉네임 수정
    override suspend fun changeNickName(nickName: String): Flow<Map<String, Object>> {
        val userIndex = memberLocalDataSource.getUserIndexfromUserSharedPreferences().first()
        return memberRemoteDataSource.changeNickNameFlow(userIndex, nickName)
    }

    // 비밀번호 수정
    override suspend fun changePassword(password: String, originalPassword: String): Flow<Map<String, Object>> {
        val userIndex = memberLocalDataSource.getUserIndexfromUserSharedPreferences().first()
        return memberRemoteDataSource.changePasswordFlow(userIndex, password, originalPassword)
    }

    // 임시 비밀번호로 비밀번호 수정
    override suspend fun changeToTemporaryPassword(email: String, randomPassword: String): Flow<Map<String, Object>> = memberRemoteDataSource.changeToTemporaryPasswordFlow(email, randomPassword)


}