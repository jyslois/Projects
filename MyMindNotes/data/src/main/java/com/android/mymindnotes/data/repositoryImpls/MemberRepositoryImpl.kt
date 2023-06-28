package com.android.mymindnotes.data.repositoryImpls

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.data.dataSources.MemberDataSource
import com.android.mymindnotes.data.dataSources.MemberSharedPreferencesDataSource
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val memberDataSource: MemberDataSource,
    private val memberSharedPreferencesDataSource: MemberSharedPreferencesDataSource
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
    override suspend fun deleteUser(): Flow<Map<String, Object>> {
        val userIndex = memberSharedPreferencesDataSource.getUserIndexfromUserSharedPreferences().first()
        return memberDataSource.deleteUserResultFlow(userIndex)
    }

    // 회원 정보 가져오기
    // (서버) 회원 정보 가져오기
    override suspend fun getUserInfo(): Flow<Map<String, Object>> {
        val userIndex = memberSharedPreferencesDataSource.getUserIndexfromUserSharedPreferences().first()
        return memberDataSource.getUserInfo(userIndex)
    }

    // 회원 정보 수정
    // 닉네임 수정
    override suspend fun changeNickName(nickName: String): Flow<Map<String, Object>> {
        val userIndex = memberSharedPreferencesDataSource.getUserIndexfromUserSharedPreferences().first()
        return memberDataSource.changeNickNameFlow(userIndex, nickName)
    }

    // 비밀번호 수정
    override suspend fun changePassword(password: String, originalPassword: String): Flow<Map<String, Object>> {
        val userIndex = memberSharedPreferencesDataSource.getUserIndexfromUserSharedPreferences().first()
        return memberDataSource.changePasswordFlow(userIndex, password, originalPassword)
    }

    // 임시 비밀번호로 비밀번호 수정
    override suspend fun changeToTemporaryPassword(email: String, randomPassword: String): Flow<Map<String, Object>> = memberDataSource.changeToTemporaryPasswordFlow(email, randomPassword)


}