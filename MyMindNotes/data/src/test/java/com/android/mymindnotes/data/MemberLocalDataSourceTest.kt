package com.android.mymindnotes.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.android.mymindnotes.data.dataSources.MemberLocalDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MemberLocalDataSourceTest {
    private lateinit var memberLocalDataSource: MemberLocalDataSource
    private val fakeDataStore = FakeDataStore()
    private val ioDispatcher = StandardTestDispatcher()

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

    @Before
    fun setUp() {
        memberLocalDataSource = MemberLocalDataSource(
            dataStore = fakeDataStore,
            ioDispatcher = ioDispatcher
        )
    }

    @After
    fun tearDown() {
        fakeDataStore.clear()
    }

    // UserIndex
    // getUserIndexFromDataStore - 데이터 스토어에서 사용자 인덱스를 불러올 때, 저장된 값이 올바르게 반환되는지 확인
    @Test
    fun getUserIndexFromDataStore_ReturnsExpectedUserIndex() = runTest(ioDispatcher) {
        // Given
        val expectedUserIndex = 42
        fakeDataStore.setValue(userIndexKey, expectedUserIndex)

        // When
        val returnedUserIndex = memberLocalDataSource.getUserIndexFromDataStore.first()

        // Then
        assertEquals(
            "반환된 UserIndex($returnedUserIndex)가 예상 UnderIndex($expectedUserIndex)와 다릅니다.",
            expectedUserIndex,
            returnedUserIndex
        )
    }

    // 데이터 스토어에 사용자 인덱스가 저장되어 있지 않을 때, 함수가 기본값을 올바르게 반환하는지 확인
    @Test
    fun getUserIndexFromDataStore_ReturnsDefaultValueWhenNoUserIndex() = runTest(ioDispatcher) {
        // When
        val returnedUserIndex = memberLocalDataSource.getUserIndexFromDataStore.first()

        // Then
        assertEquals(
            "반환된 UserIndex($returnedUserIndex)가 기본 UserIndex 값인 0과 다릅니다.",
            0,
            returnedUserIndex
        )
    }

    // saveUserIndexToDataStore - 함수가 주어진 사용자 인덱스를 올바르게 데이터 스토어에 저장하는지 확인
    @Test
    fun saveUserIndexToDataStore_SavesExpectedUserIndex() = runTest(ioDispatcher) {
        // Given
        val expectedUserIndex = 42

        // When
        memberLocalDataSource.saveUserIndexToDataStore(expectedUserIndex)

        // Then
        val savedUserIndex = fakeDataStore.getValue(userIndexKey, 0)
        assertEquals("저장해야 하는 값($expectedUserIndex)과 실제로 저장된 값($savedUserIndex)과 다릅니다.", expectedUserIndex, savedUserIndex)
    }

    // AutoLoginCheck
    // getAutoLoginCheckFromDataStore
    @Test
    fun getAutoLoginCheckFromDataStore_ReturnsExpectedState() = runTest(ioDispatcher) {
        // Given
        val expectedState = true
        fakeDataStore.setValue(autoLoginCheckKey, expectedState)

        // When
        val returnedState = memberLocalDataSource.getAutoLoginCheckFromDataStore.first()

        // Then
        assertEquals(
            "반환된 AutoLoginCheck($returnedState)가 예상 값($expectedState)와 다릅니다.",
            expectedState,
            returnedState
        )
    }

    @Test
    fun getAutoLoginCheckFromDataStore_ReturnsDefaultStateWhenNoData() = runTest(ioDispatcher) {
        // When
        val returnedState = memberLocalDataSource.getAutoLoginCheckFromDataStore.first()

        // Then
        assertEquals(
            "반환된 AutoLoginCheck($returnedState)가 기본값인 false와 다릅니다.",
            false,
            returnedState
        )
    }

    // saveAutoLoginCheckToDataStore
    @Test
    fun saveAutoLoginCheckToDataStore_SavesExpectedState() = runTest(ioDispatcher) {
        // Given
        val expectedState = true

        // When
        memberLocalDataSource.saveAutoLoginCheckToDataStore(expectedState)

        // Then
        val savedState = fakeDataStore.getValue(autoLoginCheckKey, false)
        assertEquals("저장해야 하는 값($expectedState)과 실제로 저장된 값($savedState)이 다릅니다.", expectedState, savedState)
    }

    // AutoSaveCheck
    // getAutoSaveCheckFromDataStore
    @Test
    fun getAutoSaveCheckFromDataStore_ReturnsExpectedState() = runTest(ioDispatcher) {
        // Given
        val expectedAutoSaveCheck = true
        fakeDataStore.setValue(autoSaveCheckKey, expectedAutoSaveCheck)

        // When
        val returnedAutoSaveCheck = memberLocalDataSource.getAutoSaveCheckFromDataStore.first()

        // Then
        assertEquals(
            "반환된 AutoSaveCheck($returnedAutoSaveCheck)가 예상 AutoSaveCheck($expectedAutoSaveCheck)와 다릅니다.",
            expectedAutoSaveCheck,
            returnedAutoSaveCheck
        )
    }

    @Test
    fun getAutoSaveCheckFromDataStore_ReturnsDefaultStateWhenNoData() = runTest(ioDispatcher) {
        // When
        val returnedAutoSaveCheck = memberLocalDataSource.getAutoSaveCheckFromDataStore.first()

        // Then
        assertEquals(
            "반환된 AutoSaveCheck($returnedAutoSaveCheck)가 기본값인 false와 다릅니다.",
            false,
            returnedAutoSaveCheck
        )
    }

    // saveAutoSaveCheck
    @Test
    fun saveAutoSaveCheckToDataStore_SavesExpectedState() = runTest(ioDispatcher) {
        // Given
        val expectedAutoSaveCheck = true

        // When
        memberLocalDataSource.saveAutoSaveCheckToDataStore(expectedAutoSaveCheck)

        // Then
        val savedAutoSaveCheck = fakeDataStore.getValue(autoSaveCheckKey, false)
        assertEquals(
            "저장해야 하는 값($expectedAutoSaveCheck)과 실제로 저장된 값($savedAutoSaveCheck)이 다릅니다.",
            expectedAutoSaveCheck,
            savedAutoSaveCheck
        )
    }

    // Id & Password
    // getIdFromDataStore
    @Test
    fun getIdFromDataStore_ReturnsExpectedId() = runTest(ioDispatcher) {
        // Given
        val expectedId = "testId@hotmail.com"
        fakeDataStore.setValue(idKey, expectedId)

        // When
        val returnedId = memberLocalDataSource.getIdFromDataStore.first()

        // Then
        assertEquals(
            "반환된 ID($returnedId)가 예상 ID($expectedId)와 다릅니다.",
            expectedId,
            returnedId
        )
    }

    @Test
    fun getIdFromDataStore_ReturnsDefaultValueWhenNoData() = runTest(ioDispatcher) {
        // When
        val returnedId = memberLocalDataSource.getIdFromDataStore.first()

        // Then
        assertNull("반환된 ID($returnedId)가 기본값인 null이 아닙니다.", returnedId)
    }

    // getPasswordFromDataStore
    @Test
    fun getPasswordFromDataStore_ReturnsExpectedPassword() = runTest(ioDispatcher) {
        // Given
        val expectedPassword = "testPassword11"
        fakeDataStore.setValue(passwordKey, expectedPassword)

        // When
        val returnedPassword = memberLocalDataSource.getPasswordFromDataStore.first()

        // Then
        assertEquals(
            "반환된 Password($returnedPassword)가 예상 Password($expectedPassword)와 다릅니다.",
            expectedPassword,
            returnedPassword
        )
    }

    @Test
    fun getPasswordFromDataStore_ReturnsDefaultValueWhenNoData() = runTest(ioDispatcher) {
        // When
        val returnedPassword = memberLocalDataSource.getPasswordFromDataStore.first()

        // Then
        assertNull("반환된 Password($returnedPassword)가 기본값인 null이 아닙니다.", returnedPassword)
    }

    // saveIdAndPasswordToDataStore
    @Test
    fun saveIdAndPasswordToDataStore_SavesExpectedIdAndPassword() = runTest(ioDispatcher) {
        // Given
        val expectedId = "testId@hotmail.com"
        val expectedPassword = "testPassword11"

        // When
        memberLocalDataSource.saveIdAndPasswordToDataStore(expectedId, expectedPassword)

        // Then
        val savedId = fakeDataStore.getValue(idKey, "")
        val savedPassword = fakeDataStore.getValue(passwordKey, "")
        assertEquals("저장해야 하는 ID 값($expectedId)과 실제로 저장된 ID 값($savedId)이 다릅니다.", expectedId, savedId)
        assertEquals("저장해야 하는 Password 값($expectedPassword)과 실제로 저장된 Password 값($savedPassword)이 다릅니다.", expectedPassword, savedPassword)
    }

    // savePasswordToDataStore
    @Test
    fun savePasswordToDataStore_SavesExpectedPassword() = runTest(ioDispatcher) {
        // Given
        val expectedPassword = "testPassword11"

        // When
        memberLocalDataSource.savePasswordToDataStore(expectedPassword)

        // Then
        val savedPassword = fakeDataStore.getValue(passwordKey, "")
        assertEquals("저장해야 하는 Password 값($expectedPassword)과 실제로 저장된 Password 값($savedPassword)이 다릅니다.", expectedPassword, savedPassword)
    }

    // FirstTime (최초 로그인 여부)
    // getFirstTimeFromDataStore
    @Test
    fun getFirstTimeFromDataStore_ReturnsExpectedState() = runTest(ioDispatcher) {
        // Given
        val expectedFirstTime = true
        fakeDataStore.setValue(firstTimeKey, expectedFirstTime)

        // When
        val returnedFirstTime = memberLocalDataSource.getFirstTimeFromDataStore.first()

        // Then
        assertEquals(
            "반환된 FirstTime($returnedFirstTime)이 예상 FirstTime($expectedFirstTime)과 다릅니다.",
            expectedFirstTime,
            returnedFirstTime
        )
    }

    @Test
    fun getFirstTimeFromDataStore_ReturnsDefaultStateWhenNoData() = runTest(ioDispatcher) {
        // When
        val returnedFirstTime = memberLocalDataSource.getFirstTimeFromDataStore.first()

        // Then
        assertFalse("반환된 FirstTime($returnedFirstTime)이 기본값인 false와 다릅니다.", returnedFirstTime)
    }

    // saveFirstTimeToDataStore
    @Test
    fun saveFirstTimeToDataAStore_SavesExpectedState() = runTest(ioDispatcher) {
        // Given
        val expectedFirstTime = true

        // When
        memberLocalDataSource.saveFirstTimeToDataStore(expectedFirstTime)

        // Then
        val savedFirstTime = fakeDataStore.getValue(firstTimeKey, false)
        assertEquals("저장해야 하는 값($expectedFirstTime)과 실제로 저장된 값($savedFirstTime)과 다릅니다.", expectedFirstTime, savedFirstTime)
    }

    // Alarm
    // getAlarmStateFromDataStore
    @Test
    fun getAlarmStateFromDataStore_ReturnsExpectedState() = runTest(ioDispatcher) {
        // Given
        val expectedAlarmState = true
        fakeDataStore.setValue(alarmKey, expectedAlarmState)

        // When
        val returnedAlarmState = memberLocalDataSource.getAlarmStateFromDataStore.first()

        // Then
        assertEquals(
            "반환된 AlarmState($returnedAlarmState)이 예상 AlarmState($expectedAlarmState)와 다릅니다.",
            expectedAlarmState,
            returnedAlarmState
        )
    }

    @Test
    fun getAlarmStateFromDataStore_ReturnsDefaultStateWhenNoData() = runTest(ioDispatcher) {
        // When
        val returnedAlarmState = memberLocalDataSource.getAlarmStateFromDataStore.first()

        // Then
        assertFalse("반환된 AlarmState($returnedAlarmState)이 기본값인 false와 다릅니다.", returnedAlarmState)
    }

    // saveAlarmStateToDaTaStore
    @Test
    fun saveAlarmStateToDataStore_SavesExpectedState() = runTest(ioDispatcher) {
        // Given
        val expectedAlarmState = true

        // When
        memberLocalDataSource.saveAlarmStateToDataStore(expectedAlarmState)

        // Then
        val savedAlarmState = fakeDataStore.getValue(alarmKey, false)
        assertEquals("저장해야 하는 값($expectedAlarmState)과 실제로 저장된 값($savedAlarmState)과 다릅니다.", expectedAlarmState, savedAlarmState)
    }

    // getTimeFromDataStore
    @Test
    fun getTimeFromDataStore_ReturnsExpectedTime() = runTest(ioDispatcher) {
        // Given
        val expectedTime = "12:34"
        fakeDataStore.setValue(timeKey, expectedTime)

        // When
        val returnedTime = memberLocalDataSource.getTimeFromDataStore.first()

        // Then
        assertEquals(
            "반환된 Time($returnedTime)이 예상 Time($expectedTime)와 다릅니다.",
            expectedTime,
            returnedTime
        )
    }

    @Test
    fun getTimeFromDataStore_ReturnsDefaultTimeWhenNoData() = runTest(ioDispatcher) {
        // When
        val returnedTime = memberLocalDataSource.getTimeFromDataStore.first()

        // Then
        assertNull("반환된 Time($returnedTime)이 기본값인 null과 다릅니다.", returnedTime)
    }

    // saveTimeToDataStore
    @Test
    fun saveTimeToDataStore_SavesExpectedTime() = runTest(ioDispatcher) {
        // Given
        val expectedTime = "12:34"

        // When
        memberLocalDataSource.saveTimeToDataStore(expectedTime)

        // Then
        val savedTime = fakeDataStore.getValue(timeKey, "")
        assertEquals("저장해야 하는 Time($expectedTime)과 실제로 저장된 Time($savedTime)과 다릅니다.", expectedTime, savedTime)
    }

    // getHourFromDataStore
    @Test
    fun getHourFromDataStore_ReturnsExpectedHour() = runTest(ioDispatcher) {
        // Given
        val expectedHour = 11
        fakeDataStore.setValue(hourKey, expectedHour)

        // When
        val returnedHour = memberLocalDataSource.getHourFromDataStore.first()

        // Then
        assertEquals(
            "반환된 Hour($returnedHour)이 예상 Hour($expectedHour)와 다릅니다.",
            expectedHour,
            returnedHour
        )
    }

    @Test
    fun getHourFromDataStore_ReturnsDefaultHourWhenNoData() = runTest(ioDispatcher) {
        // When
        val returnedHour = memberLocalDataSource.getHourFromDataStore.first()

        // Then
        assertEquals("반환된 Hour($returnedHour)가 기본값인 22와 다릅니다.", 22, returnedHour)
    }

    // saveHourToDataStore
    @Test
    fun saveHourToDataStore_SavesExpectedHour() = runTest(ioDispatcher) {
        // Given
        val expectedHour = 15

        // When
        memberLocalDataSource.saveHourToDataStore(expectedHour)

        // Then
        val savedHour = fakeDataStore.getValue(hourKey, 22)
        assertEquals("저장해야 하는 Hour($expectedHour)과 실제로 저장된 Hour($savedHour)과 다릅니다.", expectedHour, savedHour)
    }

    // saveMinuteToDataStore
    @Test
    fun saveMinuteToDataStore_SavesExpectedMinute() = runTest(ioDispatcher) {
        // Given
        val expectedMinute = 30

        // When
        memberLocalDataSource.saveMinuteToDataStore(expectedMinute)

        // Then
        val savedMinute = fakeDataStore.getValue(minuteKey, 0)
        assertEquals("저장해야 하는 Minute($expectedMinute)과 실제로 저장된 Minute($savedMinute)과 다릅니다.", expectedMinute, savedMinute)
    }

    // getMinuteFromDataStore
    @Test
    fun getMinuteFromDataStore_ReturnsExpectedMinute() = runTest(ioDispatcher) {
        // Given
        val expectedMinute = 30
        fakeDataStore.setValue(minuteKey, expectedMinute)

        // When
        val returnedMinute = memberLocalDataSource.getMinuteFromDataStore.first()

        // Then
        assertEquals(
            "반환된 Minute($returnedMinute)이 예상 Minute($expectedMinute)와 다릅니다.",
            expectedMinute,
            returnedMinute
        )
    }

    @Test
    fun getMinuteFromDataStore_ReturnsDefaultMinuteWhenNoData() = runTest(ioDispatcher) {
        // When
        val returnedMinute = memberLocalDataSource.getMinuteFromDataStore.first()

        // Then
        assertEquals("반환된 Minute($returnedMinute)이 기본값인 0과 다릅니다.", 0, returnedMinute)
    }

    // getRebootTimeFromDataStore
    @Test
    fun getRebootTimeFromDataStore_ReturnsExpectedTime() = runTest(ioDispatcher) {
        // Given
        val expectedTime = 1626300000L
        fakeDataStore.setValue(rebootTimeKey, expectedTime)

        // When
        val returnedTime = memberLocalDataSource.getRebootTimeFromDataStore.first()

        // Then
        assertEquals(
            "반환된 Time($returnedTime)이 예상 Time($expectedTime)와 다릅니다.",
            expectedTime,
            returnedTime
        )
    }

    @Test
    fun getRebootTimeFromDataStore_ReturnsDefaultTimeWhenNoData() = runTest(ioDispatcher) {
        // When
        val returnedTime = memberLocalDataSource.getRebootTimeFromDataStore.first()

        // Then
        assertEquals("반환된 Time($returnedTime)이 기본값인 0L과 다릅니다.", 0L, returnedTime)
    }

    // saveRebootTimeToDataStore
    @Test
    fun saveRebootTimeToDataStore_SavesExpectedTime() = runTest(ioDispatcher) {
        // Given
        val expectedTime = 1626300000L

        // When
        memberLocalDataSource.saveRebootTimeToDataStore(expectedTime)

        // Then
        val savedTime = fakeDataStore.getValue(rebootTimeKey, 0L)
        assertEquals("저장해야 하는 Time($expectedTime)과 실제로 저장된 Time($savedTime)과 다릅니다.", expectedTime, savedTime)
    }

    // Test for clearLoginStatesRelatedKeys
    @Test
    fun clearLoginStatesRelatedKeys_RemovesExpectedKeys() = runTest(ioDispatcher) {
        // Given
        val initialAutoLoginCheck = true
        val initialAutoSaveCheck = true
        val initialId = "testId"
        val initialPassword = "testPassword"
        fakeDataStore.setValue(autoLoginCheckKey, initialAutoLoginCheck)
        fakeDataStore.setValue(autoSaveCheckKey, initialAutoSaveCheck)
        fakeDataStore.setValue(idKey, initialId)
        fakeDataStore.setValue(passwordKey, initialPassword)

        // When
        memberLocalDataSource.clearLoginStatesRelatedKeys()

        // Then
        val autoLoginCheck = fakeDataStore.getValue(autoLoginCheckKey, false)
        val autoSaveCheck = fakeDataStore.getValue(autoSaveCheckKey, false)
        val id = fakeDataStore.getValue(idKey, "")
        val password = fakeDataStore.getValue(passwordKey, "")
        assertFalse("autoLoginCheckKey가 제거되지 않았습니다.", autoLoginCheck)
        assertFalse("autoSaveCheckKey가 제거되지 않았습니다.", autoSaveCheck)
        assertEquals("idKey가 제거되지 않았습니다.", "", id)
        assertEquals("passwordKey가 제거되지 않았습니다.", "", password)
    }

    // Test for clearAlarmRelatedKeys
    @Test
    fun clearAlarmRelatedKeys_RemovesExpectedKeys() = runTest(ioDispatcher) {
        // Given
        val initialAlarmState = true
        val initialTime = "12:34"
        val initialHour = 12
        val initialMinute = 34
        fakeDataStore.setValue(alarmKey, initialAlarmState)
        fakeDataStore.setValue(timeKey, initialTime)
        fakeDataStore.setValue(hourKey, initialHour)
        fakeDataStore.setValue(minuteKey, initialMinute)

        // When
        memberLocalDataSource.clearAlarmRelatedKeys()

        // Then
        val alarmState = fakeDataStore.getValue(alarmKey, false)
        val time = fakeDataStore.getValue(timeKey, "")
        val hour = fakeDataStore.getValue(hourKey, 22)
        val minute = fakeDataStore.getValue(minuteKey, 0)
        assertFalse("alarmKey가 제거되지 않았습니다.", alarmState)
        assertEquals("timeKey가 제거되지 않았습니다.", "", time)
        assertEquals("hourKey가 제거되지 않았습니다.", 22, hour)
        assertEquals("minuteKey가 제거되지 않았습니다.", 0, minute)
    }

    // Test for clearRebootTimeKey
    @Test
    fun clearRebootTimeKey_RemovesExpectedKey() = runTest(ioDispatcher) {
        // Given
        val initialRebootTime = 1626300000L
        fakeDataStore.setValue(rebootTimeKey, initialRebootTime)

        // When
        memberLocalDataSource.clearRebootTimeKey()

        // Then
        val rebootTime = fakeDataStore.getValue(rebootTimeKey, 0L)
        assertEquals("rebootTimeKey가 제거되지 않았습니다.", 0L, rebootTime)
    }

}

