package com.android.mymindnotes.data

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.android.mymindnotes.data.dataSources.TodayDiaryLocalDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class TodayDiaryLocalDataSourceTest {
    private lateinit var todayDiaryLocalDataSource: TodayDiaryLocalDataSource
    private val fakeDataStore = FakeDataStore()
    private val ioDispatcher = StandardTestDispatcher()

    // DataStore Keys
    private val todayEmotionColorKey = intPreferencesKey("todayEmotionColor")
    private val todayEmotionKey = stringPreferencesKey("todayEmotion")
    private val todayEmotionTextKey = stringPreferencesKey("todayEmotionDescription")
    private val todaySituationKey = stringPreferencesKey("todaySituation")
    private val todayThoughtKey = stringPreferencesKey("todayThought")
    private val todayReflectionKey = stringPreferencesKey("todayReflection")
    private val todayTypeKey = stringPreferencesKey("todayType")
    private val todayDateKey = stringPreferencesKey("todayDate")
    private val todayDayKey = stringPreferencesKey("todayDay")

    @Before
    fun setUp() {
        todayDiaryLocalDataSource = TodayDiaryLocalDataSource(
            dataStore = fakeDataStore,
            ioDispatcher = ioDispatcher
        )
    }

    @After
    fun tearDown() {
        fakeDataStore.clear()
    }

    // Test Save Methods
    // saveEmotionColor
    @Test
    fun saveEmotionColor_SavesExpectedColor() = runTest(ioDispatcher) {
        // Given
        val expectedColor = 0xFF5737

        // When
        todayDiaryLocalDataSource.saveEmotionColor(expectedColor)

        // Then
        val savedColor = fakeDataStore.getValue(todayEmotionColorKey, -1)
        assertEquals("저장해야 하는 색상($expectedColor)과 실제로 저장된 색상($savedColor)이 다릅니다.", expectedColor, savedColor)
    }

    // saveEmotion
    @Test
    fun saveEmotion_SavesExpectedEmotion() = runTest(ioDispatcher) {
        // Given
        val expectedEmotion = "기쁨"

        // When
        todayDiaryLocalDataSource.saveEmotion(expectedEmotion)

        // Then
        val savedEmotion = fakeDataStore.getValue(todayEmotionKey, "")
        assertEquals("저장해야 하는 감정($expectedEmotion)과 실제로 저장된 감정($savedEmotion)이 다릅니다.", expectedEmotion, savedEmotion)
    }

    // saveEmotionText
    @Test
    fun saveEmotionText_SavesExpectedEmotionText() = runTest(ioDispatcher) {
        // Given
        val expectedEmotionText = "꽉 찬 행복을 느꼈다"

        // When
        todayDiaryLocalDataSource.saveEmotionText(expectedEmotionText)

        // Then
        val savedEmotionText = fakeDataStore.getValue(todayEmotionTextKey, "")
        assertEquals("저장해야 하는 감정 설명($expectedEmotionText)과 실제로 저장된 감정 설명($savedEmotionText)이 다릅니다.", expectedEmotionText, savedEmotionText)
    }

    // saveSituation
    @Test
    fun saveSituation_SavesExpectedSituation() = runTest(ioDispatcher) {
        // Given
        val expectedSituation = "맛있는 타코를 먹었다"

        // When
        todayDiaryLocalDataSource.saveSituation(expectedSituation)

        // Then
        val savedSituation = fakeDataStore.getValue(todaySituationKey, "")
        assertEquals("저장해야 하는 상황($expectedSituation)과 실제로 저장된 상황($savedSituation)이 다릅니다.", expectedSituation, savedSituation)
    }

    // saveThought
    @Test
    fun saveThought_SavesExpectedThought() = runTest(ioDispatcher) {
        // Given
        val expectedThought = "타코 정말 맛있다"

        // When
        todayDiaryLocalDataSource.saveThought(expectedThought)

        // Then
        val savedThought = fakeDataStore.getValue(todayThoughtKey, "")
        assertEquals("저장해야 하는 생각($expectedThought)과 실제로 저장된 생각($savedThought)이 다릅니다.", expectedThought, savedThought)
    }

    // saveReflection
    @Test
    fun saveReflection_SavesExpectedReflection() = runTest(ioDispatcher) {
        // Given
        val expectedReflection = "맛있는 음식을 더 자주 먹어야지!"

        // When
        todayDiaryLocalDataSource.saveReflection(expectedReflection)

        // Then
        val savedReflection = fakeDataStore.getValue(todayReflectionKey, "")
        assertEquals("저장해야 하는 반성($expectedReflection)과 실제로 저장된 반성($savedReflection)이 다릅니다.", expectedReflection, savedReflection)
    }

    // saveType
    @Test
    fun saveType_SavesExpectedType() = runTest(ioDispatcher) {
        // Given
        val expectedType = "오늘의 마음 일기"

        // When
        todayDiaryLocalDataSource.saveType(expectedType)

        // Then
        val savedType = fakeDataStore.getValue(todayTypeKey, "")
        assertEquals("저장해야 하는 타입($expectedType)과 실제로 저장된 타입($savedType)이 다릅니다.", expectedType, savedType)
    }

    // saveDate
    @Test
    fun saveDate_SavesExpectedDate() = runTest(ioDispatcher) {
        // Given
        val expectedDate = "2023-08-17"

        // When
        todayDiaryLocalDataSource.saveDate(expectedDate)

        // Then
        val savedDate = fakeDataStore.getValue(todayDateKey, "")
        assertEquals("저장해야 하는 날짜($expectedDate)와 실제로 저장된 날짜($savedDate)가 다릅니다.", expectedDate, savedDate)
    }

    // saveDay
    @Test
    fun saveDay_SavesExpectedDay() = runTest(ioDispatcher) {
        // Given
        val expectedDay = "목요일"

        // When
        todayDiaryLocalDataSource.saveDay(expectedDay)

        // Then
        val savedDay = fakeDataStore.getValue(todayDayKey, "")
        assertEquals("저장해야 하는 요일($expectedDay)과 실제로 저장된 요일($savedDay)이 다릅니다.", expectedDay, savedDay)
    }


   // Test Get Methods
   // getEmotion
   @Test
   fun getEmotion_ReturnsExpectedEmotion() = runTest(ioDispatcher) {
       // Given
       val expectedEmotion = "기쁨"
       fakeDataStore.setValue(todayEmotionKey, expectedEmotion)

       // When
       val returnedEmotion = todayDiaryLocalDataSource.getEmotion.first()

       // Then
       assertEquals("반환된 감정($returnedEmotion)이 예상 감정($expectedEmotion)과 다릅니다.", expectedEmotion, returnedEmotion)
   }

    @Test
    fun getEmotion_ReturnsDefaultValueWhenNoData() = runTest(ioDispatcher) {
        // When
        val returnedEmotion = todayDiaryLocalDataSource.getEmotion.first()

        // Then
        assertNull("반환된 감정($returnedEmotion)이 기본값인 null이 아닙니다.", returnedEmotion)
    }

    // getEmotionText
    @Test
    fun getEmotionText_ReturnsExpectedEmotionText() = runTest(ioDispatcher) {
        // Given
        val expectedEmotionText = "꽉 찬 행복을 느꼈다"
        fakeDataStore.setValue(todayEmotionTextKey, expectedEmotionText)

        // When
        val returnedEmotionText = todayDiaryLocalDataSource.getEmotionText.first()

        // Then
        assertEquals("반환된 감정 설명($returnedEmotionText)가 예상 감정 텍스트($expectedEmotionText)와 다릅니다.", expectedEmotionText, returnedEmotionText)
    }

    @Test
    fun getEmotionText_ReturnsDefaultValueWhenNoData() = runTest(ioDispatcher) {
        // When
        val returnedEmotionText = todayDiaryLocalDataSource.getEmotionText.first()

        // Then
        assertNull("반환된 감정 설명($returnedEmotionText)가 기본값인 null이 아닙니다.", returnedEmotionText)
    }

    // getSituation
    @Test
    fun getSituation_ReturnsExpectedSituation() = runTest(ioDispatcher) {
        // Given
        val expectedSituation = "맛있는 타코를 먹었다"
        fakeDataStore.setValue(todaySituationKey, expectedSituation)

        // When
        val returnedSituation = todayDiaryLocalDataSource.getSituation.first()

        // Then
        assertEquals("반환된 상황($returnedSituation)이 예상 상황($expectedSituation)과 다릅니다.", expectedSituation, returnedSituation)
    }

    @Test
    fun getSituation_ReturnsDefaultValueWhenNoData() = runTest(ioDispatcher) {
        // When
        val returnedSituation = todayDiaryLocalDataSource.getSituation.first()

        // Then
        assertNull("반환된 상황($returnedSituation)이 기본값인 null이 아닙니다.", returnedSituation)
    }

    // getThought
    @Test
    fun getThought_ReturnsExpectedThought() = runTest(ioDispatcher) {
        // Given
        val expectedThought = "타코 정말 맛있다."
        fakeDataStore.setValue(todayThoughtKey, expectedThought)

        // When
        val returnedThought = todayDiaryLocalDataSource.getThought.first()

        // Then
        assertEquals("반환된 생각($returnedThought)이 예상 생각($expectedThought)과 다릅니다.", expectedThought, returnedThought)
    }

    @Test
    fun getThought_ReturnsDefaultValueWhenNoData() = runTest(ioDispatcher) {
        // When
        val returnedThought = todayDiaryLocalDataSource.getThought.first()

        // Then
        assertNull("반환된 생각($returnedThought)이 기본값인 null이 아닙니다.", returnedThought)
    }

    // getReflection
    @Test
    fun getReflection_ReturnsExpectedReflection() = runTest(ioDispatcher) {
        // Given
        val expectedReflection = "맛있는 음식을 더 자주 먹어야지!"
        fakeDataStore.setValue(todayReflectionKey, expectedReflection)

        // When
        val returnedReflection = todayDiaryLocalDataSource.getReflection.first()

        // Then
        assertEquals("반환된 회고($returnedReflection)가 예상 회고($expectedReflection)과 다릅니다.", expectedReflection, returnedReflection)
    }

    @Test
    fun getReflection_ReturnsDefaultValueWhenNoData() = runTest(ioDispatcher) {
        // When
        val returnedReflection = todayDiaryLocalDataSource.getReflection.first()

        // Then
        assertNull("반환된 회고($returnedReflection)가 기본값인 null이 아닙니다.", returnedReflection)
    }

    // getType
    @Test
    fun getType_ReturnsExpectedType() = runTest(ioDispatcher) {
        // Given
        val expectedType = "오늘의 마음 일기"
        fakeDataStore.setValue(todayTypeKey, expectedType)

        // When
        val returnedType = todayDiaryLocalDataSource.getType.first()

        // Then
        assertEquals("반환된 타입($returnedType)이 예상 타입($expectedType)과 다릅니다.", expectedType, returnedType)
    }

    @Test
    fun getType_ReturnsDefaultValueWhenNoData() = runTest(ioDispatcher) {
        // When
        val returnedType = todayDiaryLocalDataSource.getType.first()

        // Then
        assertNull("반환된 타입($returnedType)이 기본값인 null이 아닙니다.", returnedType)
    }

    // getDate
    @Test
    fun getDate_ReturnsExpectedDate() = runTest(ioDispatcher) {
        // Given
        val expectedDate = "2023-08-17"
        fakeDataStore.setValue(todayDateKey, expectedDate)

        // When
        val returnedDate = todayDiaryLocalDataSource.getDate.first()

        // Then
        assertEquals("반환된 날짜($returnedDate)가 예상 날짜($expectedDate)와 다릅니다.", expectedDate, returnedDate)
    }

    @Test
    fun getDate_ReturnsDefaultValueWhenNoData() = runTest(ioDispatcher) {
        // When
        val returnedDate = todayDiaryLocalDataSource.getDate.first()

        // Then
        assertNull("반환된 날짜($returnedDate)가 기본값인 null이 아닙니다.", returnedDate)
    }

    // getDay
    @Test
    fun getDay_ReturnsExpectedDay() = runTest(ioDispatcher) {
        // Given
        val expectedDay = "목요일"
        fakeDataStore.setValue(todayDayKey, expectedDay)

        // When
        val returnedDay = todayDiaryLocalDataSource.getDay.first()

        // Then
        assertEquals("반환된 요일($returnedDay)이 예상 요일($expectedDay)과 다릅니다.", expectedDay, returnedDay)
    }

    @Test
    fun getDay_ReturnsDefaultValueWhenNoData() = runTest(ioDispatcher) {
        // When
        val returnedDay = todayDiaryLocalDataSource.getDay.first()

        // Then
        assertNull("반환된 요일($returnedDay)이 기본값인 null이 아닙니다.", returnedDay)
    }

    // Test Clear Method
    // clearTodayDiaryTempRecords
    @Test
    fun clearTodayDiaryTempRecords_RemovesExpectedKeys() = runTest(ioDispatcher) {
        // Given
        val initialEmotionColor = 0xFF5737
        val initialEmotion = "기쁨"
        val initialEmotionText = "꽉 찬 행복을 느꼈다"
        val initialSituation = "맛있는 타코를 먹었다"
        val initialThought = "타코 정말 맛있다"
        val initialReflection = "맛있는 음식을 더 자주 먹어야지!"
        val initialType = "오늘의 마음 일기"
        val initialDate = "2023-08-17"
        val initialDay = "목요일"
        fakeDataStore.setValue(todayEmotionColorKey, initialEmotionColor)
        fakeDataStore.setValue(todayEmotionKey, initialEmotion)
        fakeDataStore.setValue(todayEmotionTextKey, initialEmotionText)
        fakeDataStore.setValue(todaySituationKey, initialSituation)
        fakeDataStore.setValue(todayThoughtKey, initialThought)
        fakeDataStore.setValue(todayReflectionKey, initialReflection)
        fakeDataStore.setValue(todayTypeKey, initialType)
        fakeDataStore.setValue(todayDateKey, initialDate)
        fakeDataStore.setValue(todayDayKey, initialDay)

        // When
        todayDiaryLocalDataSource.clearTodayDiaryTempRecords()

        // Then
        val emotionColor = fakeDataStore.getValue(todayEmotionColorKey, -1)
        val emotion = fakeDataStore.getValue(todayEmotionKey, "")
        val emotionText = fakeDataStore.getValue(todayEmotionTextKey, "")
        val situation = fakeDataStore.getValue(todaySituationKey, "")
        val thought = fakeDataStore.getValue(todayThoughtKey, "")
        val reflection = fakeDataStore.getValue(todayReflectionKey, "")
        val type = fakeDataStore.getValue(todayTypeKey, "")
        val date = fakeDataStore.getValue(todayDateKey, "")
        val day = fakeDataStore.getValue(todayDayKey, "")
        assertEquals("todayEmotionColorKey가 제거되지 않았습니다.", -1, emotionColor)
        assertEquals("todayEmotionKey가 제거되지 않았습니다.", "", emotion)
        assertEquals("todayEmotionTextKey가 제거되지 않았습니다.", "", emotionText)
        assertEquals("todaySituationKey가 제거되지 않았습니다.", "", situation)
        assertEquals("todayThoughtKey가 제거되지 않았습니다.", "", thought)
        assertEquals("todayReflectionKey가 제거되지 않았습니다.", "", reflection)
        assertEquals("todayTypeKey가 제거되지 않았습니다.", "", type)
        assertEquals("todayDateKey가 제거되지 않았습니다.", "", date)
        assertEquals("todayDayKey가 제거되지 않았습니다.", "", day)
    }
}