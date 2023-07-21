package com.android.mymindnotes.data.dataSources

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.android.mymindnotes.core.hilt.coroutineModules.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MemberLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MemberLocalDataSourceInterface {

    // DataStore keys
    private val userIndexKey = intPreferencesKey("userindex")
    private val autoLoginCheckKey = booleanPreferencesKey("autoLoginCheck")
    private val autoSaveCheckKey = booleanPreferencesKey("autoSaveCheck")
    private val idKey = stringPreferencesKey("id")
    private val passwordKey = stringPreferencesKey("password")
    private val firstTimeKey = booleanPreferencesKey("firstTime")
    private val alarmKey = booleanPreferencesKey("alarm")
    private val timeKey = stringPreferencesKey("time")
    private val hourKey = intPreferencesKey("hour")
    private val minuteKey = intPreferencesKey("minute")
    private val rebootTimeKey = longPreferencesKey("RebootTime")

    // UserIndex
    // UserIndex 가져오기 - 사용자 index를 비동기적으로(Flow) 반환
    //  DataStore에서 "userindex"라는 키를 사용하여 저장된 사용자 인덱스를 비동기적으로 반환하도록 설정
    override val getUserIndexFromDataStore: Flow<Int> =
    // dataStore.data는 DataStore<Preferences>의 모든 데이터를 Flow<Preferences> 형태로 반환 -각 변경시마다 업데이트된 데이터를 내보냄
        // map 연산자는 Flow의 각 아이템에 함수를 적용하고, 그 결과를 반환하는 새 Flow를 생성
        dataStore.data.map { preferences -> //  preferences라는 이름의 Preferences 객체가 입력되고, userIndexKey에 해당하는 값을 찾아 반환
            preferences[userIndexKey] ?: 0 // 만약 userIndexKey에 해당하는 값이 null이라면, 기본값으로 0을 반환
        }.flowOn(ioDispatcher) // 해당 Flow의 컬렉션을 지정된 Dispatcher에서 수행하도록 한다. 이 경우, ioDispatcher는 IO를 처리하는 별도의 스레드에서 이 Flow를 수집하도록 지정. 이렇게 함으로써 메인 스레드가 아닌 별도의 스레드에서 데이터를 처리하게 되므로, 메인 스레드에서 발생할 수 있는 블로킹을 방지하고 앱의 반응성을 유지.


    // UserIndex 저장하기
    // DataStore에 "userindex"라는 키를 사용하여 입력받은 사용자 인덱스를 비동기적으로 저장
    override suspend fun saveUserIndexToDataStore(index: Int) { // suspend 키워드를 가지므로 코루틴으로 처리되며, 비동기 작업을 수행할 수 있다.
        withContext(ioDispatcher) { //  이 함수 내부의 코드를 ioDispatcher에서 실행하도록 함. 이는 IO를 처리하는 별도의 스레드에서 작업을 수행하도록 지정하는 것이며, 메인 스레드에서 발생할 수 있는 블로킹을 방지하고 앱의 반응성을 유지.
            dataStore.edit { preferences -> // 이 함수는 DataStore의 현재 Preferences를 가져와 수정할 수 있게 함. 수정 작업이 끝나면 자동으로 DataStore에 저장
                preferences[userIndexKey] = index // preferences 맵에 userIndexKey라는 키를 사용하여 index 값을 저장
            }
        }
    }

    // AutoLoginCheck
    // AutoLoginCheck 가져오기
    override val getAutoLoginCheckFromDataStore: Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[autoLoginCheckKey] ?: false
    }


    // AutoLoginCheck 저장하기
    override suspend fun saveAutoLoginCheckToDataStore(state: Boolean) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[autoLoginCheckKey] = state
            }
        }
    }

    // AutoSaveCheck
    // AutoSaveCheck 가져오기
    override val getAutoSaveCheckFromDataStore: Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[autoSaveCheckKey] ?: false
        }.flowOn(ioDispatcher)

    // AutoSaveCheck 저장하기
    override suspend fun saveAutoSaveCheckToDataStore(state: Boolean) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[autoSaveCheckKey] = state
            }
        }
    }

    // Id, Password
    // id 가져오기
    override val getIdFromDataStore: Flow<String?> =
        dataStore.data.map { preferences ->
            preferences[idKey]
        }.flowOn(ioDispatcher)

    // password 가져오기
    override val getPasswordFromDataStore: Flow<String?> =
        dataStore.data.map { preferences ->
            preferences[passwordKey]
        }.flowOn(ioDispatcher)


    // Id와 Password 저장하기
    override suspend fun saveIdAndPasswordToDataStore(id: String?, password: String?) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[idKey] = id ?: ""
                preferences[passwordKey] = password ?: ""
            }
        }
    }

    // Password 저장하기
    override suspend fun savePasswordToDataStore(password: String?) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[passwordKey] = password ?: ""
            }
        }
    }


    // FirstTime - 최초 로그인 여부
    // FirstTime 가져오기
    override val getFirstTimeFromDataStore: Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[firstTimeKey] ?: false
        }.flowOn(ioDispatcher)


    // FirstTime 저장하기
    override suspend fun saveFirstTimeToDataAStore(state: Boolean) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[firstTimeKey] = state
            }
        }
    }

    // Alarm SharedPreferences
    // alarm 가져오기
    override val getAlarmStateFromDataStore: Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[alarmKey] ?: false
        }.flowOn(ioDispatcher)

    // alarm 상태 저장하기
    override suspend fun saveAlarmStateToDataStore(state: Boolean) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[alarmKey] = state
            }
        }
    }

    // time 가져오기
    override val getTimeFromDataStore: Flow<String?> =
        dataStore.data.map { preferences ->
            preferences[timeKey]
        }.flowOn(ioDispatcher)

    // time 저장하기
    override suspend fun saveTimeToDataStore(time: String) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[timeKey] = time
            }
        }
    }

    // hour 저장하기
    override suspend fun saveHourToDataStore(hour: Int) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[hourKey] = hour
            }
        }
    }

    // hour 가져오기
    override val getHourToDataStore: Flow<Int> =
        dataStore.data.map { preferences ->
            preferences[hourKey] ?: 22
        }.flowOn(ioDispatcher)


    // minute 저장하기
    override suspend fun saveMinuteToDataStore(minute: Int) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[minuteKey] = minute
            }
        }
    }

    // minute 가져오기
    override val getMinuteToDataStore: Flow<Int> =
        dataStore.data.map { preferences ->
            preferences[minuteKey] ?: 0
        }.flowOn(ioDispatcher)


    // time 부팅시 알람 재설정을 위한 sharedpreferences
    // 부팅 time 가져오기
    override val getRebootTimeFromDataStore: Flow<Long> =
        dataStore.data.map { preferences ->
            preferences[rebootTimeKey] ?: 0L
        }.flowOn(ioDispatcher)

    // 부팅 time 저장하기
    override suspend fun saveRebootTimeToDataStore(time: Long) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[rebootTimeKey] = time
            }
        }
    }


    // Clear Preferences
    // Clear autoSave SharedPreference
    override suspend fun clearLoginStatesRelatedKeys() {
        withContext(ioDispatcher) {
            withContext(ioDispatcher) {
                dataStore.edit { preferences ->
                    preferences.remove(autoLoginCheckKey)
                    preferences.remove(autoSaveCheckKey)
                    preferences.remove(idKey)
                    preferences.remove(passwordKey)
                }
            }
        }
    }

    // Clear Alarm SharedPreferences
    override suspend fun clearAlarmRelatedKeys() {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences.remove(alarmKey)
                preferences.remove(timeKey)
                preferences.remove(hourKey)
                preferences.remove(minuteKey)
            }
        }
    }

    // Clear Time SharedPreferences
    override suspend fun clearRebootTimeKey() {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences.remove(rebootTimeKey)
            }
        }
    }
}

interface MemberLocalDataSourceInterface {
    val getUserIndexFromDataStore: Flow<Int>
    suspend fun saveUserIndexToDataStore(index: Int)

    val getAutoLoginCheckFromDataStore: Flow<Boolean>
    suspend fun saveAutoLoginCheckToDataStore(state: Boolean)

    val getAutoSaveCheckFromDataStore: Flow<Boolean>
    suspend fun saveAutoSaveCheckToDataStore(state: Boolean)

    val getIdFromDataStore: Flow<String?>

    val getPasswordFromDataStore: Flow<String?>
    suspend fun saveIdAndPasswordToDataStore(id: String?, password: String?)
    suspend fun savePasswordToDataStore(password: String?)


    val getFirstTimeFromDataStore: Flow<Boolean>
    suspend fun saveFirstTimeToDataAStore(state: Boolean)
    val getAlarmStateFromDataStore: Flow<Boolean>
    suspend fun saveAlarmStateToDataStore(state: Boolean)
    val getTimeFromDataStore: Flow<String?>
    suspend fun saveTimeToDataStore(time: String)
    suspend fun saveHourToDataStore(hour: Int)
    val getHourToDataStore: Flow<Int>
    suspend fun saveMinuteToDataStore(minute: Int)
    val getMinuteToDataStore: Flow<Int>
    val getRebootTimeFromDataStore: Flow<Long>

    suspend fun saveRebootTimeToDataStore(time: Long)
    suspend fun clearLoginStatesRelatedKeys()
    suspend fun clearAlarmRelatedKeys()
    suspend fun clearRebootTimeKey()

}