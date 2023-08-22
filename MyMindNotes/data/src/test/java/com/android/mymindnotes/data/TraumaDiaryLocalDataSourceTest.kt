package com.android.mymindnotes.data

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.android.mymindnotes.data.dataSources.TraumaDiaryLocalDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TraumaDiaryLocalDataSourceTest {
    private lateinit var traumaDiaryLocalDataSource: TraumaDiaryLocalDataSource
    private val fakeDataStore = FakeDataStore()
    private val testDispatcher = StandardTestDispatcher()

    // DataStore Keys (TraumaDiary에 해당하는 DataStore keys)
    private val traumaEmotionColorKey = intPreferencesKey("traumaEmotionColor")
    private val traumaEmotionKey = stringPreferencesKey("traumaEmotion")
    private val traumaEmotionTextKey = stringPreferencesKey("traumaEmotionDescription")
    private val traumaSituationKey = stringPreferencesKey("traumaSituation")
    private val traumaThoughtKey = stringPreferencesKey("traumaThought")
    private val traumaReflectionKey = stringPreferencesKey("traumaReflection")
    private val traumaTypeKey = stringPreferencesKey("traumaType")
    private val traumaDateKey = stringPreferencesKey("traumaDate")
    private val traumaDayKey = stringPreferencesKey("traumaDay")

    @Before
    fun setUp() {
        traumaDiaryLocalDataSource = TraumaDiaryLocalDataSource(
            dataStore = fakeDataStore,
            ioDispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        fakeDataStore.clear()
    }

    // Test Save Methods
    // saveEmotionColor
    @Test
    fun saveEmotionColor_SavesExpectedColor() = runTest(testDispatcher) {
        // Given
        val expectedColor = 0xFF5737

        // When
        traumaDiaryLocalDataSource.saveEmotionColor(expectedColor)

        // Then
        val savedColor = fakeDataStore.getValue(traumaEmotionColorKey, -1)
        assertEquals("저장해야 하는 색상($expectedColor)과 실제로 저장된 색상($savedColor)이 다릅니다.", expectedColor, savedColor)
    }

    // saveEmotion
    @Test
    fun saveEmotion_SavesExpectedEmotion() = runTest(testDispatcher) {
        // Given
        val expectedEmotion = "공포"

        // When
        traumaDiaryLocalDataSource.saveEmotion(expectedEmotion)

        // Then
        val savedEmotion = fakeDataStore.getValue(traumaEmotionKey, "")
        assertEquals("저장해야 하는 감정($expectedEmotion)과 실제로 저장된 감정($savedEmotion)이 다릅니다.", expectedEmotion, savedEmotion)
    }

    // saveEmotionText
    @Test
    fun saveEmotionText_SavesExpectedEmotionText() = runTest(testDispatcher) {
        // Given
        val expectedEmotionText = "무섭다"

        // When
        traumaDiaryLocalDataSource.saveEmotionText(expectedEmotionText)

        // Then
        val savedEmotionText = fakeDataStore.getValue(traumaEmotionTextKey, "")
        assertEquals("저장해야 하는 감정 설명($expectedEmotionText)과 실제로 저장된 감정 설명($savedEmotionText)이 다릅니다.", expectedEmotionText, savedEmotionText)
    }

    // saveSituation
    @Test
    fun saveSituation_SavesExpectedSituation() = runTest(testDispatcher) {
        // Given
        val expectedSituation = "공포스러운 경험"

        // When
        traumaDiaryLocalDataSource.saveSituation(expectedSituation)

        // Then
        val savedSituation = fakeDataStore.getValue(traumaSituationKey, "")
        assertEquals("저장해야 하는 상황($expectedSituation)과 실제로 저장된 상황($savedSituation)이 다릅니다.", expectedSituation, savedSituation)
    }

    // saveThought
    @Test
    fun saveThought_SavesExpectedThought() = runTest(testDispatcher) {
        // Given
        val expectedThought = "다치면 어떡하지?"

        // When
        traumaDiaryLocalDataSource.saveThought(expectedThought)

        // Then
        val savedThought = fakeDataStore.getValue(traumaThoughtKey, "")
        assertEquals("저장해야 하는 생각($expectedThought)과 실제로 저장된 생각($savedThought)이 다릅니다.", expectedThought, savedThought)
    }

    // saveReflection
    @Test
    fun saveReflection_SavesExpectedReflection() = runTest(testDispatcher) {
        // Given
        val expectedReflection = "왜 오늘 땀이 났는지 이제 이해가 간다"

        // When
        traumaDiaryLocalDataSource.saveReflection(expectedReflection)

        // Then
        val savedReflection = fakeDataStore.getValue(traumaReflectionKey, "")
        assertEquals("저장해야 하는 회고($expectedReflection)와 실제로 저장된 회고($savedReflection)가 다릅니다.", expectedReflection, savedReflection)
    }

    // saveType
    @Test
    fun saveType_SavesExpectedType() = runTest(testDispatcher) {
        // Given
        val expectedType = "트라우마 일기"

        // When
        traumaDiaryLocalDataSource.saveType(expectedType)

        // Then
        val savedType = fakeDataStore.getValue(traumaTypeKey, "")
        assertEquals("저장해야 하는 유형($expectedType)과 실제로 저장된 유형($savedType)이 다릅니다.", expectedType, savedType)
    }

    // saveDate
    @Test
    fun saveDate_SavesExpectedDate() = runTest(testDispatcher) {
        // Given
        val expectedDate = "2023-08-18"

        // When
        traumaDiaryLocalDataSource.saveDate(expectedDate)

        // Then
        val savedDate = fakeDataStore.getValue(traumaDateKey, "")
        assertEquals("저장해야 하는 날짜($expectedDate)와 실제로 저장된 날짜($savedDate)가 다릅니다.", expectedDate, savedDate)
    }

    // saveDay
    @Test
    fun saveDay_SavesExpectedDay() = runTest(testDispatcher) {
        // Given
        val expectedDay = "금요일"

        // When
        traumaDiaryLocalDataSource.saveDay(expectedDay)

        // Then
        val savedDay = fakeDataStore.getValue(traumaDayKey, "")
        assertEquals("저장해야 하는 요일($expectedDay)과 실제로 저장된 요일($savedDay)이 다릅니다.", expectedDay, savedDay)
    }

    // Test Get Methods
    // getEmotion
    @Test
    fun getEmotion_ReturnsExpectedEmotion() = runTest(testDispatcher) {
        // Given
        val expectedEmotion = "공포"
        fakeDataStore.setValue(traumaEmotionKey, expectedEmotion)

        // When
        val returnedEmotion = traumaDiaryLocalDataSource.getEmotion.first()

        // Then
        assertEquals("반환된 감정($returnedEmotion)이 예상 감정($expectedEmotion)과 다릅니다.", expectedEmotion, returnedEmotion)
    }

    @Test
    fun getEmotion_ReturnsDefaultValueWhenNoData() = runTest(testDispatcher) {
        // When
        val returnedEmotion = traumaDiaryLocalDataSource.getEmotion.first()

        // Then
        Assert.assertNull("반환된 감정($returnedEmotion)이 기본값인 null이 아닙니다.", returnedEmotion)
    }

    // getEmotionText
    @Test
    fun getEmotionText_ReturnsExpectedEmotionText() = runTest(testDispatcher) {
        // Given
        val expectedEmotionText = "무섭다"
        fakeDataStore.setValue(traumaEmotionTextKey, expectedEmotionText)

        // When
        val returnedEmotionText = traumaDiaryLocalDataSource.getEmotionText.first()

        // Then
        assertEquals("반환된 감정 설명($returnedEmotionText)가 예상 감정 텍스트($expectedEmotionText)와 다릅니다.", expectedEmotionText, returnedEmotionText)
    }

    @Test
    fun getEmotionText_ReturnsDefaultValueWhenNoData() = runTest(testDispatcher) {
        // When
        val returnedEmotionText = traumaDiaryLocalDataSource.getEmotionText.first()

        // Then
        Assert.assertNull("반환된 감정 설명($returnedEmotionText)가 기본값인 null이 아닙니다.", returnedEmotionText)
    }

    // getSituation
    @Test
    fun getSituation_ReturnsExpectedSituation() = runTest(testDispatcher) {
        // Given
        val expectedSituation = "공포스러운 경험"
        fakeDataStore.setValue(traumaSituationKey, expectedSituation)

        // When
        val returnedSituation = traumaDiaryLocalDataSource.getSituation.first()

        // Then
        assertEquals("반환된 상황($returnedSituation)이 예상 상황($expectedSituation)과 다릅니다.", expectedSituation, returnedSituation)
    }

    @Test
    fun getSituation_ReturnsDefaultValueWhenNoData() = runTest(testDispatcher) {
        // When
        val returnedSituation = traumaDiaryLocalDataSource.getSituation.first()

        // Then
        Assert.assertNull("반환된 상황($returnedSituation)이 기본값인 null이 아닙니다.", returnedSituation)
    }

    // getThought
    @Test
    fun getThought_ReturnsExpectedThought() = runTest(testDispatcher) {
        // Given
        val expectedThought = "다치면 어떡하지?"
        fakeDataStore.setValue(traumaThoughtKey, expectedThought)

        // When
        val returnedThought = traumaDiaryLocalDataSource.getThought.first()

        // Then
        assertEquals("반환된 생각($returnedThought)이 예상 생각($expectedThought)과 다릅니다.", expectedThought, returnedThought)
    }

    @Test
    fun getThought_ReturnsDefaultValueWhenNoData() = runTest(testDispatcher) {
        // When
        val returnedThought = traumaDiaryLocalDataSource.getThought.first()

        // Then
        Assert.assertNull("반환된 생각($returnedThought)이 기본값인 null이 아닙니다.", returnedThought)
    }

    // getReflection
    @Test
    fun getReflection_ReturnsExpectedReflection() = runTest(testDispatcher) {
        // Given
        val expectedReflection = "왜 오늘 땀이 났는지 이제 이해가 간다"
        fakeDataStore.setValue(traumaReflectionKey, expectedReflection)

        // When
        val returnedReflection = traumaDiaryLocalDataSource.getReflection.first()

        // Then
        assertEquals("반환된 회고($returnedReflection)가 예상 회고($expectedReflection)과 다릅니다.", expectedReflection, returnedReflection)
    }

    @Test
    fun getReflection_ReturnsDefaultValueWhenNoData() = runTest(testDispatcher) {
        // When
        val returnedReflection = traumaDiaryLocalDataSource.getReflection.first()

        // Then
        Assert.assertNull("반환된 회고($returnedReflection)가 기본값인 null이 아닙니다.", returnedReflection)
    }

    // getType
    @Test
    fun getType_ReturnsExpectedType() = runTest(testDispatcher) {
        // Given
        val expectedType = "트라우마 일기"
        fakeDataStore.setValue(traumaTypeKey, expectedType)

        // When
        val returnedType = traumaDiaryLocalDataSource.getType.first()

        // Then
        assertEquals("반환된 타입($returnedType)이 예상 타입($expectedType)과 다릅니다.", expectedType, returnedType)
    }

    @Test
    fun getType_ReturnsDefaultValueWhenNoData() = runTest(testDispatcher) {
        // When
        val returnedType = traumaDiaryLocalDataSource.getType.first()

        // Then
        Assert.assertNull("반환된 타입($returnedType)이 기본값인 null이 아닙니다.", returnedType)
    }

    // getDate
    @Test
    fun getDate_ReturnsExpectedDate() = runTest(testDispatcher) {
        // Given
        val expectedDate = "2023-08-18"
        fakeDataStore.setValue(traumaDateKey, expectedDate)

        // When
        val returnedDate = traumaDiaryLocalDataSource.getDate.first()

        // Then
        assertEquals("반환된 날짜($returnedDate)가 예상 날짜($expectedDate)와 다릅니다.", expectedDate, returnedDate)
    }

    @Test
    fun getDate_ReturnsDefaultValueWhenNoData() = runTest(testDispatcher) {
        // When
        val returnedDate = traumaDiaryLocalDataSource.getDate.first()

        // Then
        Assert.assertNull("반환된 날짜($returnedDate)가 기본값인 null이 아닙니다.", returnedDate)
    }

    // getDay
    @Test
    fun getDay_ReturnsExpectedDay() = runTest(testDispatcher) {
        // Given
        val expectedDay = "금요일"
        fakeDataStore.setValue(traumaDayKey, expectedDay)

        // When
        val returnedDay = traumaDiaryLocalDataSource.getDay.first()

        // Then
        assertEquals("반환된 요일($returnedDay)이 예상 요일($expectedDay)과 다릅니다.", expectedDay, returnedDay)
    }

    @Test
    fun getDay_ReturnsDefaultValueWhenNoData() = runTest(testDispatcher) {
        // When
        val returnedDay = traumaDiaryLocalDataSource.getDay.first()

        // Then
        Assert.assertNull("반환된 요일($returnedDay)이 기본값인 null이 아닙니다.", returnedDay)
    }

    // Test Clear Methods
    // clearTraumaDiaryTempRecords
    @Test
    fun clearTraumaDiaryTempRecords_RemovesExpectedKeys() = runTest(testDispatcher) {
        // Given
        val initialEmotionColor = 0xFF5737
        val initialEmotion = "공포"
        val initialEmotionText = "무섭다"
        val initialSituation = "공포스러운 경험"
        val initialThought = "다치면 어떡하지?"
        val initialReflection = "왜 오늘 땀이 났는지 이제 이해가 간다"
        val initialType = "트라우마 일기"
        val initialDate = "2023-08-18"
        val initialDay = "금요일"
        fakeDataStore.setValue(traumaEmotionColorKey, initialEmotionColor)
        fakeDataStore.setValue(traumaEmotionKey, initialEmotion)
        fakeDataStore.setValue(traumaEmotionTextKey, initialEmotionText)
        fakeDataStore.setValue(traumaSituationKey, initialSituation)
        fakeDataStore.setValue(traumaThoughtKey, initialThought)
        fakeDataStore.setValue(traumaReflectionKey, initialReflection)
        fakeDataStore.setValue(traumaTypeKey, initialType)
        fakeDataStore.setValue(traumaDateKey, initialDate)
        fakeDataStore.setValue(traumaDayKey, initialDay)

        // When
        traumaDiaryLocalDataSource.clearTraumaDiaryTempRecords()

        // Then
        val emotionColor = fakeDataStore.getValue(traumaEmotionColorKey, -1)
        val emotion = fakeDataStore.getValue(traumaEmotionKey, "")
        val emotionText = fakeDataStore.getValue(traumaEmotionTextKey, "")
        val situation = fakeDataStore.getValue(traumaSituationKey, "")
        val thought = fakeDataStore.getValue(traumaThoughtKey, "")
        val reflection = fakeDataStore.getValue(traumaReflectionKey, "")
        val type = fakeDataStore.getValue(traumaTypeKey, "")
        val date = fakeDataStore.getValue(traumaDateKey, "")
        val day = fakeDataStore.getValue(traumaDayKey, "")
        assertEquals("traumaEmotionColorKey가 제거되지 않았습니다.", -1, emotionColor)
        assertEquals("traumaEmotionKey가 제거되지 않았습니다.", "", emotion)
        assertEquals("traumaEmotionTextKey가 제거되지 않았습니다.", "", emotionText)
        assertEquals("traumaSituationKey가 제거되지 않았습니다.", "", situation)
        assertEquals("traumaThoughtKey가 제거되지 않았습니다.", "", thought)
        assertEquals("traumaReflectionKey가 제거되지 않았습니다.", "", reflection)
        assertEquals("traumaTypeKey가 제거되지 않았습니다.", "", type)
        assertEquals("traumaDateKey가 제거되지 않았습니다.", "", date)
        assertEquals("traumaDayKey가 제거되지 않았습니다.", "", day)
    }
}