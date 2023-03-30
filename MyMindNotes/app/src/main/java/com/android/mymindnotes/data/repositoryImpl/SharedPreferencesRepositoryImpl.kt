package com.android.mymindnotes.data.repositoryImpl

import com.android.mymindnotes.data.datasources.SharedPreferencesDataSource
import com.android.mymindnotes.domain.repositoryinterfaces.SharedPreferencesRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesRepositoryImpl @Inject constructor(
    private val dataSource: SharedPreferencesDataSource
) : SharedPreferencesRepository {

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

    // FirstTime - 최초 로그인 여부
    // FirstTime 가져오기
    override suspend fun getFirstTime(): Flow<Boolean> = dataSource.getFirstTimefromFirstTimeSharedPreferences

    // FirstTime 저장하기
    override suspend fun saveFirstTime(state: Boolean) {
        dataSource.saveFirstTimetoFirstTimeSharedPreferences(state)
    }

    // Clear autoSave SharedPreference
    override suspend fun clearAutoSaveSharedPreferences() {
        dataSource.clearAutoSaveSharedPreferences()
    }

}