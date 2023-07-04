package com.android.mymindnotes.data.repositoryImpls

import com.android.mymindnotes.data.dataSources.MemberLocalDataSourceInterface
import com.android.mymindnotes.data.repositoryInterfaces.MemberLocalRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemberLocalRepositoryImpl @Inject constructor(
    private val dataSource: MemberLocalDataSourceInterface
) : MemberLocalRepository {

    // UserIndex
    // UserIndex 저장하기
    override suspend fun saveUserIndex(index: Int) {
        dataSource.saveUserIndextoUserSharedPreferences(index)
    }

    // AutoLoginCheck
    // AutoLoginCheck 가져오기
    override suspend fun getAutoLoginCheck(): Flow<Boolean> = dataSource.getAutoLoginCheckfromAutoSaveSharedPreferences

    // AutoLoginCheck 저장하기
    override suspend fun saveAutoLoginCheck(state: Boolean) {
        dataSource.saveAutoLoginChecktoAutoSaveSharedPreferences(state)
    }

    // AutoSaveCheck
    // AutoSaveCheck 가져오기
    override suspend fun getAutoSaveCheck(): Flow<Boolean> = dataSource.getAutoSaveCheckfromAutoSaveSharedPreferences

    // AutoSaveCheck 저장하기
    override suspend fun saveAutoSaveCheck(state: Boolean) {
        dataSource.saveAutoSaveChecktoAutoSaveSharedPreferences(state)
    }

    // Id, Password
    // Id 가져오기
    override suspend fun getId(): Flow<String?> = dataSource.getIdfromAutoSaveSharedPreferences

    // Password 가져오기
    override suspend fun getPassword(): Flow<String?> = dataSource.getPasswordfromAutoSaveSharedPreferences

    // Id와 Password 저장하기
    override suspend fun saveIdAndPassword(id: String?, password: String?) {
        dataSource.saveIdAndPasswordtoAutoSaveSharedPreferences(id, password)
    }

    // Password 저장하기
    override suspend fun savePassword(password: String?) {
        dataSource.savePasswordtoAutoSaveSharedPreferences(password)
    }

    // FirstTime - 최초 로그인 여부
    // FirstTime 가져오기
    override suspend fun getFirstTime(): Flow<Boolean> = dataSource.getFirstTimefromFirstTimeSharedPreferences

    // FirstTime 저장하기
    override suspend fun saveFirstTime(state: Boolean) {
        dataSource.saveFirstTimetoFirstTimeSharedPreferences(state)
    }

    // Alarm
    // alarm 상태 가져오기
    override suspend fun getAlarmState(): Flow<Boolean> = dataSource.getAlarmStateFromAlarmSharedPreferences

    // time 가져오기
    override suspend fun getTime(): Flow<String?> = dataSource.getTimeFromAlarmSharedPreferences

    // alarm 상태 저장하기
    override suspend fun saveAlarmState(state: Boolean) {
        dataSource.saveAlarmStateToAlarmSharedPreferences(state)
    }

    // time 저장하기
    override suspend fun saveTime(time: String) {
        dataSource.saveTimeToAlarmSharedPreferences(time)
    }

    // 시간 저장하기
    override suspend fun saveHour(hour: Int) {
       dataSource.saveHourToAlarmSharedPreferences(hour)
    }

    // 시간 가져오기
    override suspend fun getHour(): Flow<Int> = dataSource.getHourFromAlarmSharedPreferences


    // 분 저장하기
    override suspend fun saveMinute(minute: Int) {
        dataSource.saveMinuteToAlarmSharedPreferences(minute)
    }

    // 분 가져오기
    override suspend fun getMinute(): Flow<Int> = dataSource.getMinuteFromAlarmSharedPreferences

    // Reboot를 위한 시간 저장하기
    override suspend fun saveRebootTime(time: Long) {
        dataSource.saveRebootTimeToTimeSharedPreferences(time)
    }

    // Reboot 시간 가져오기
    override suspend fun getRebootTime(): Flow<Long> = dataSource.getRebootTimeFromTimeSharedPreferences


    // Clear Preferences
    // Clear autoSave SharedPreference
    override suspend fun clearAutoSaveSharedPreferences() {
        dataSource.clearAutoSaveSharedPreferences()
    }

    // Clear Alarm SharedPreferences
    override suspend fun clearAlarmSharedPreferences() {
        dataSource.clearAlarmSharedPreferences()
    }

    // Clear Time SharedPreferences
    override suspend fun clearTimeSharedPreferences() {
        dataSource.clearTimeSharedPreferences()
    }

}