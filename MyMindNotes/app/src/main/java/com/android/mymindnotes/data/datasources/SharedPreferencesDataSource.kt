package com.android.mymindnotes.data.datasources

import android.content.SharedPreferences
import com.android.mymindnotes.hilt.module.AutoSave
import com.android.mymindnotes.hilt.module.FirstTime
import com.android.mymindnotes.hilt.module.IoDispatcher
import com.android.mymindnotes.hilt.module.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SharedPreferencesDataSource @Inject constructor(
    @AutoSave private val autoSave_sharedPreference: SharedPreferences,
    @User private val user_sharedPreference: SharedPreferences,
    @FirstTime private val firstTime_sharedPreference: SharedPreferences,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    // UserIndex
    // (서버) UserIndex 가져오기
    suspend fun getUserIndexfromUserSharedPreferences(): Flow<Int> = flow {
        emit(user_sharedPreference.getInt("userindex", 0))
    }.flowOn(ioDispatcher)


    // (서버) UserIndex 저장하기
    suspend fun saveUserIndextoUserSharedPreferences(index: Int) {
        withContext(ioDispatcher) {
            user_sharedPreference.edit().putInt("userindex", index).commit()
        }
    }

    // AutoLoginCheck
    // AutoLoginCheck 가져오기
    val getAutoLoginCheckfromAutoSaveSharedPreferences: Flow<Boolean> = flow {
        // dataSource의 SharedPreferences에 접근해서 autoLoginCheck 값을 가져와 SharedFlow에 emit하기
        val autoLoginCheck = autoSave_sharedPreference.getBoolean("autoLoginCheck", false)
        emit(autoLoginCheck)
    }.flowOn(ioDispatcher)

    // AutoLoginCheck 저장하기
    suspend fun saveAutoLoginChecktoAutoSaveSharedPreferences(state: Boolean) {
        withContext(ioDispatcher) {
            autoSave_sharedPreference.edit().putBoolean("autoLoginCheck", state).commit()
        }
    }

    // AutoSaveCheck
    // AutoSaveCheck 가져오기
    val getAutoSaveCheckfromAutoSaveSharedPreferences: Flow<Boolean> = flow {
        val autoSaveCheck = autoSave_sharedPreference.getBoolean("autoSaveCheck", false)
        emit(autoSaveCheck)
    }.flowOn(ioDispatcher)

    // AutoSaveCheck 저장하기
    suspend fun saveAutoSaveChecktoAutoSaveSharedPreferences(state: Boolean) {
        withContext(ioDispatcher) {
            autoSave_sharedPreference.edit().putBoolean("autoSaveCheck", state).commit()
        }
    }

    // Id, Password
    // id 가져오기
    val getIdfromAutoSaveSharedPreferences: Flow<String?> = flow {
        val id = autoSave_sharedPreference.getString("id", null)
        emit(id)
    }.flowOn(ioDispatcher)

    // password 가져오기
    val getPasswordfromAutoSaveSharedPreferences: Flow<String?> = flow {
        val password = autoSave_sharedPreference.getString("password", null)
        emit(password)
    }.flowOn(ioDispatcher)

    // Id와 Password 저장하기
    suspend fun saveIdAndPasswordtoAutoSaveSharedPreferences(id: String?, password: String?) {
        withContext(ioDispatcher) {
            autoSave_sharedPreference.edit().putString("id", id).commit()
            autoSave_sharedPreference.edit().putString("password", password).commit()
        }
    }

    // Clear autoSave SharedPreference
    suspend fun clearAutoSaveSharedPreferences() {
        withContext(ioDispatcher) {
            autoSave_sharedPreference.edit().clear().commit()
        }
    }

    // FirstTime - 최초 로그인 여부
    // FirstTime 가져오기
    val getFirstTimefromFirstTimeSharedPreferences: Flow<Boolean> = flow {
        val firstTime = firstTime_sharedPreference.getBoolean("firstTime", false)
        emit(firstTime)
    }.flowOn(ioDispatcher)

    // FirstTime 저장하기
    suspend fun saveFirstTimetoFirstTimeSharedPreferences(state: Boolean) {
        withContext(ioDispatcher) {
            firstTime_sharedPreference.edit().putBoolean("firstTime", state).commit()
        }
    }


}