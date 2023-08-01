package com.android.mymindnotes.data.repositoryImpls

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
    override suspend fun getAutoLoginCheck(): Flow<Boolean> = memberLocalDataSource.getAutoLoginCheckFromDataStore

    // AutoLoginCheck 저장하기
    override suspend fun saveAutoLoginCheck(state: Boolean) {
        memberLocalDataSource.saveAutoLoginCheckToDataStore(state)
    }

    // AutoSaveCheck
    // AutoSaveCheck 가져오기
    override suspend fun getAutoSaveCheck(): Flow<Boolean> = memberLocalDataSource.getAutoSaveCheckFromDataStore

    // AutoSaveCheck 저장하기
    override suspend fun saveAutoSaveCheck(state: Boolean) {
        memberLocalDataSource.saveAutoSaveCheckToDataStore(state)
    }

    // Id, Password
    // Id 가져오기
    override suspend fun getId(): Flow<String?> = memberLocalDataSource.getIdFromDataStore

    // Password 가져오기
    override suspend fun getPassword(): Flow<String?> = memberLocalDataSource.getPasswordFromDataStore

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
    override suspend fun getFirstTime(): Flow<Boolean> = memberLocalDataSource.getFirstTimeFromDataStore

    // FirstTime 저장하기
    override suspend fun saveFirstTime(state: Boolean) {
        memberLocalDataSource.saveFirstTimeToDataAStore(state)
    }

    // Alarm
    // alarm 상태 가져오기
    override suspend fun getAlarmState(): Flow<Boolean> = memberLocalDataSource.getAlarmStateFromDataStore

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
    override suspend fun getHour(): Flow<Int> = memberLocalDataSource.getHourToDataStore


    // 분 저장하기
    override suspend fun saveMinute(minute: Int) {
        memberLocalDataSource.saveMinuteToDataStore(minute)
    }

    // 분 가져오기
    override suspend fun getMinute(): Flow<Int> = memberLocalDataSource.getMinuteToDataStore

    // Reboot를 위한 시간 저장하기
    override suspend fun saveRebootTime(time: Long) {
        memberLocalDataSource.saveRebootTimeToDataStore(time)
    }

    // Reboot 시간 가져오기
    override suspend fun getRebootTime(): Flow<Long> = memberLocalDataSource.getRebootTimeFromDataStore


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
        val userIndex = memberLocalDataSource.getUserIndexFromDataStore.first()
        return memberRemoteDataSource.deleteUserResultFlow(userIndex)
    }

    // 회원 정보 가져오기
    // (서버) 회원 정보 가져오기
    override suspend fun getUserInfo(): Flow<Map<String, Object>> {
        val userIndex = memberLocalDataSource.getUserIndexFromDataStore.first()
        return memberRemoteDataSource.getUserInfo(userIndex)
    }

    // 회원 정보 수정
    // 닉네임 수정
    override suspend fun changeNickName(nickName: String): Flow<Map<String, Object>> {
        val userIndex = memberLocalDataSource.getUserIndexFromDataStore.first()
        return memberRemoteDataSource.changeNickNameFlow(userIndex, nickName)
    }

    // 비밀번호 수정
    override suspend fun changePassword(password: String, originalPassword: String): Flow<Map<String, Object>> {
        val userIndex = memberLocalDataSource.getUserIndexFromDataStore.first()
        return memberRemoteDataSource.changePasswordFlow(userIndex, password, originalPassword)
    }

    // 임시 비밀번호로 비밀번호 수정
    override suspend fun changeToTemporaryPassword(email: String, randomPassword: String): Flow<Map<String, Object>> = memberRemoteDataSource.changeToTemporaryPasswordFlow(email, randomPassword)


}