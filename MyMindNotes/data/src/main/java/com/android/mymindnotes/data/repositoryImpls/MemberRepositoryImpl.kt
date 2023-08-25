package com.android.mymindnotes.data.repositoryImpls

import com.android.mymindnotes.core.dto.ChangeNicknameResponse
import com.android.mymindnotes.core.dto.ChangePasswordResponse
import com.android.mymindnotes.core.dto.ChangeToTemporaryPassword
import com.android.mymindnotes.core.dto.ChangeToTemporaryPasswordResponse
import com.android.mymindnotes.core.dto.ChangeUserNickname
import com.android.mymindnotes.core.dto.ChangeUserPassword
import com.android.mymindnotes.core.dto.DeleteUserResponse
import com.android.mymindnotes.core.dto.DuplicateCheckResponse
import com.android.mymindnotes.core.dto.GetUserInfoResponse
import com.android.mymindnotes.core.dto.JoinResponse
import com.android.mymindnotes.core.dto.LoginResponse
import com.android.mymindnotes.core.dto.UserInfo
import com.android.mymindnotes.core.dto.UserInfoLogin
import com.android.mymindnotes.data.dataSources.MemberLocalDataSourceInterface
import com.android.mymindnotes.data.dataSources.MemberRemoteDataSourceInterface
import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemberRepositoryImpl @Inject constructor(
    private val memberRemoteDataSource: MemberRemoteDataSourceInterface,
    private val memberLocalDataSource: MemberLocalDataSourceInterface
) : MemberRepository {


    // Local
    // UserIndex
    // UserIndex 저장하기
    override suspend fun saveUserIndex(index: Int) {
        memberLocalDataSource.saveUserIndexToDataStore(index)
    }

    // AutoLoginCheck
    // AutoLoginCheck 가져오기
    override suspend fun getAutoLoginCheck(): Flow<Boolean> =
        memberLocalDataSource.getAutoLoginCheckFromDataStore

    // AutoLoginCheck 저장하기
    override suspend fun saveAutoLoginCheck(state: Boolean) {
        memberLocalDataSource.saveAutoLoginCheckToDataStore(state)
    }

    // AutoSaveCheck
    // AutoSaveCheck 가져오기
    override suspend fun getAutoSaveCheck(): Flow<Boolean> =
        memberLocalDataSource.getAutoSaveCheckFromDataStore

    // AutoSaveCheck 저장하기
    override suspend fun saveAutoSaveCheck(state: Boolean) {
        memberLocalDataSource.saveAutoSaveCheckToDataStore(state)
    }

    // Id, Password
    // Id 가져오기
    override suspend fun getId(): Flow<String?> = memberLocalDataSource.getIdFromDataStore

    // Password 가져오기
    override suspend fun getPassword(): Flow<String?> =
        memberLocalDataSource.getPasswordFromDataStore

    // Id와 Password 저장하기
    override suspend fun saveIdAndPassword(id: String?, password: String?) {
        memberLocalDataSource.saveIdAndPasswordToDataStore(id, password)
    }

    // Password 저장하기
    override suspend fun savePassword(password: String?) {
        memberLocalDataSource.savePasswordToDataStore(password)
    }

    // FirstTime - 최초 로그인 여부
    // FirstTime 가져오기
    override suspend fun getFirstTime(): Flow<Boolean> =
        memberLocalDataSource.getFirstTimeFromDataStore

    // FirstTime 저장하기
    override suspend fun saveFirstTime(state: Boolean) {
        memberLocalDataSource.saveFirstTimeToDataStore(state)
    }

    // Alarm
    // alarm 상태 가져오기
    override suspend fun getAlarmState(): Flow<Boolean> =
        memberLocalDataSource.getAlarmStateFromDataStore

    // time 가져오기
    override suspend fun getTime(): Flow<String?> = memberLocalDataSource.getTimeFromDataStore

    // alarm 상태 저장하기
    override suspend fun saveAlarmState(state: Boolean) {
        memberLocalDataSource.saveAlarmStateToDataStore(state)
    }

    // time 저장하기
    override suspend fun saveTime(time: String) {
        memberLocalDataSource.saveTimeToDataStore(time)
    }

    // 시간 저장하기
    override suspend fun saveHour(hour: Int) {
        memberLocalDataSource.saveHourToDataStore(hour)
    }

    // 시간 가져오기
    override suspend fun getHour(): Flow<Int> = memberLocalDataSource.getHourFromDataStore


    // 분 저장하기
    override suspend fun saveMinute(minute: Int) {
        memberLocalDataSource.saveMinuteToDataStore(minute)
    }

    // 분 가져오기
    override suspend fun getMinute(): Flow<Int> = memberLocalDataSource.getMinuteFromDataStore

    // Reboot를 위한 시간 저장하기
    override suspend fun saveRebootTime(time: Long) {
        memberLocalDataSource.saveRebootTimeToDataStore(time)
    }

    // Reboot 시간 가져오기
    override suspend fun getRebootTime(): Flow<Long> =
        memberLocalDataSource.getRebootTimeFromDataStore


    // Clear Preferences
    // Clear autoSave SharedPreference
    override suspend fun clearLoginStatesRelatedKeys() {
        memberLocalDataSource.clearLoginStatesRelatedKeys()
    }

    // Clear Alarm SharedPreferences
    override suspend fun clearAlarmRelatedKeys() {
        memberLocalDataSource.clearAlarmRelatedKeys()
    }

    // Clear Time SharedPreferences
    override suspend fun clearRebootTimeKey() {
        memberLocalDataSource.clearRebootTimeKey()
    }


    // Remote
    // 로그인
    override suspend fun login(email: String, password: String): Flow<LoginResponse> {
        val userInfoLogin = UserInfoLogin(email, password)
        return memberRemoteDataSource.login(userInfoLogin)
    }


    // 아이디, 비밀번호 중복 체크
    // 이메일 중복 체크
    override suspend fun checkEmail(emailInput: String): Flow<DuplicateCheckResponse> =
        memberRemoteDataSource.emailDuplicateCheck(emailInput)

    // 닉네임 중복 체크
    override suspend fun checkNickName(nickNameInput: String): Flow<DuplicateCheckResponse> =
        memberRemoteDataSource.nickNameDuplicateCheck(nickNameInput)

    // 회원가입
    override suspend fun join(email: String, nickname: String, password: String, birthyear: Int): Flow<JoinResponse> {
        val userInfo = UserInfo(email, nickname, password, birthyear)
        return memberRemoteDataSource.join(userInfo)
    }

    // 회원탈퇴
    override suspend fun deleteUser(): Flow<DeleteUserResponse> {
        val userIndex = memberLocalDataSource.getUserIndexFromDataStore.first()
        return memberRemoteDataSource.deleteUser(userIndex)
    }

    // 회원 정보 가져오기
    // (서버) 회원 정보 가져오기
    override suspend fun getUserInfo(): Flow<GetUserInfoResponse> {
        val userIndex = memberLocalDataSource.getUserIndexFromDataStore.first()
        return memberRemoteDataSource.getUserInfo(userIndex)
    }

    // 회원 정보 수정
    // 닉네임 수정
    override suspend fun changeNickName(nickName: String): Flow<ChangeNicknameResponse> {
        val userIndex = memberLocalDataSource.getUserIndexFromDataStore.first()
        val nicknameInfo = ChangeUserNickname(userIndex, nickName)
        return memberRemoteDataSource.changeNickName(nicknameInfo)
    }

    // 비밀번호 수정
    override suspend fun changePassword(
        password: String,
        originalPassword: String
    ): Flow<ChangePasswordResponse> {
        val userIndex = memberLocalDataSource.getUserIndexFromDataStore.first()
        val passwordInfo = ChangeUserPassword(userIndex, password, originalPassword)
        return memberRemoteDataSource.changePassword(passwordInfo)
    }

    // 임시 비밀번호로 비밀번호 수정
    override suspend fun changeToTemporaryPassword(email: String, randomPassword: String): Flow<ChangeToTemporaryPasswordResponse> {
        val temporaryPasswordInfo = ChangeToTemporaryPassword(email, randomPassword)
        return memberRemoteDataSource.changeToTemporaryPassword(temporaryPasswordInfo)
    }


}