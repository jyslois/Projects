package com.android.mymindnotes.data.datasources

import android.content.SharedPreferences
import com.android.mymindnotes.hilt.module.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MemberSharedPreferencesDataSource @Inject constructor(
    @AutoSave private val autoSave_sharedPreference: SharedPreferences,
    @User private val user_sharedPreference: SharedPreferences,
    @FirstTime private val firstTime_sharedPreference: SharedPreferences,
    @Alarm private val alarm_sharedPreferences: SharedPreferences,
    @Time private val time_sharedPreferences: SharedPreferences,
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

    // Password 저장하기
    suspend fun savePasswordtoAutoSaveSharedPreferences(password: String?) {
        withContext(ioDispatcher) {
            autoSave_sharedPreference.edit().putString("password", password).commit()
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

    // Alarm SharedPreferences
    // alarm 가져오기
    val getAlarmStateFromAlarmSharedPreferences: Flow<Boolean> = flow {
        val alarmState = alarm_sharedPreferences.getBoolean("alarm", false)
        emit(alarmState)
    }.flowOn(ioDispatcher)

    // alarm 상태 저장하기
    suspend fun saveAlarmStateToAlarmSharedPreferences(state: Boolean) {
        withContext(ioDispatcher) {
            alarm_sharedPreferences.edit().putBoolean("alarm", state).commit()
        }
    }

    // time 가져오기
    val getTimeFromAlarmSharedPreferences: Flow<String?> = flow {
        val time = alarm_sharedPreferences.getString("time", "")
        emit(time)
    }.flowOn(ioDispatcher)

    // time 저장하기
    suspend fun saveTimeToAlarmSharedPreferences(time: String) {
        withContext(ioDispatcher) {
            alarm_sharedPreferences.edit().putString("time", time).commit()
        }
    }

    // hour 저장하기
    suspend fun saveHourToAlarmSharedPreferences(hour: Int) {
        withContext(ioDispatcher) {
            alarm_sharedPreferences.edit().putInt("hour", hour).commit()
        }
    }

    // hour 가져오기
    val getHourFromAlarmSharedPreferences: Flow<Int> = flow {
        val hour = alarm_sharedPreferences.getInt("hour", 22)
        emit(hour)
    }.flowOn(ioDispatcher)


    // minute 저장하기
    suspend fun saveMinuteToAlarmSharedPreferences(minute: Int) {
        withContext(ioDispatcher) {
            alarm_sharedPreferences.edit().putInt("minute", minute).commit()
        }
    }

    // minute 가져오기
    val getMinuteFromAlarmSharedPreferences: Flow<Int> = flow {
        val minute = alarm_sharedPreferences.getInt("minute", 0)
        emit(minute)
    }.flowOn(ioDispatcher)

    // time 부팅시 알람 재설정을 위한 sharedpreferences

    // 부팅 time 가져오기
    val getRebootTimeFromTimeSharedPreferences: Flow<Long> = flow {
        val time = time_sharedPreferences.getLong("time", 0L)
        emit(time)
    }.flowOn(ioDispatcher)

    // 부팅 time 저장하기
    suspend fun saveRebootTimeToTimeSharedPreferences(time: Long) {
        withContext(ioDispatcher) {
            time_sharedPreferences.edit().putLong("time", time).commit()
        }
    }


    // Clear Preferences
    // Clear autoSave SharedPreference
    suspend fun clearAutoSaveSharedPreferences() {
        withContext(ioDispatcher) {
            autoSave_sharedPreference.edit().clear().commit()
        }
    }

    // Clear Alarm SharedPreferences
    suspend fun clearAlarmSharedPreferences() {
        withContext(ioDispatcher) {
            alarm_sharedPreferences.edit().clear().commit()
        }
    }

    // Clear Time SharedPreferences
    suspend fun clearTimeSharedPreferences() {
        withContext(ioDispatcher) {
            time_sharedPreferences.edit().clear().commit()
        }
    }

}