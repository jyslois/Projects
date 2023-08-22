package com.android.mymindnotes.data

import com.android.mymindnotes.core.dto.SaveDiaryResponse
import com.android.mymindnotes.core.dto.UserDiary
import com.android.mymindnotes.data.dataSources.TodayDiaryRemoteDataSource
import com.android.mymindnotes.data.retrofit.api.diary.SaveDiaryApi
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class TodayDiaryRemoteDataSourceTest {
    private lateinit var todayDiaryRemoteDataSource: TodayDiaryRemoteDataSource
    private val mockSaveDiaryApi = mockk<SaveDiaryApi>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        todayDiaryRemoteDataSource = TodayDiaryRemoteDataSource(
            saveDiaryApi = mockSaveDiaryApi,
            ioDispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        clearMocks(mockSaveDiaryApi)
    }

    // saveDiary
    @Test
    fun saveDiary_ReturnsExpectedResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = SaveDiaryResponse(code = 6000, msg = "일기 저장 성공")
        val diaryInfo = UserDiary(
            user_index = 1,
            type = "오늘의 마음 일기",
            date = "2023-08-19",
            day = "토요일",
            emotion = "기쁨",
            emotionDescription = "너무도 뿌듯했다.",
            situation = "할 일을 무사히 마쳤다.",
            thought = "잘 이겨냈다.",
            reflection = "앞으로도 힘 내 보자!"
        )
        coEvery { mockSaveDiaryApi.addDiary(diaryInfo) } returns expectedResponse

        // When
        val returnedResponse = todayDiaryRemoteDataSource.saveDiary(diaryInfo)

        // Then
        returnedResponse.collect {
            Assert.assertEquals(
                "반환된 결과(${it})가 예상(${expectedResponse})과 다릅니다.",
                expectedResponse,
                it
            )
        }
        coVerify { mockSaveDiaryApi.addDiary(diaryInfo) }
    }

    @Test
    fun saveDiary_ReturnsUnsuccuesfulCode() = runTest(testDispatcher) {
        // Given
        val expectedResponse = SaveDiaryResponse(code = 6001, msg = "일기 양식을 준수하지 않아서 일기가 저장되지 않았습니다.")
        val diaryInfo = UserDiary(
            user_index = 1,
            type = "오늘의 마음 일기",
            date = "2023-08-19",
            day = "토요일",
            emotion = "",
            emotionDescription = "너무도 뿌듯했다.",
            situation = "할 일을 무사히 마쳤다.",
            thought = "잘 이겨냈다.",
            reflection = "앞으로도 힘 내 보자!"
        )
        coEvery { mockSaveDiaryApi.addDiary(diaryInfo) } returns expectedResponse

        // When
        val returnedResponse = todayDiaryRemoteDataSource.saveDiary(diaryInfo)

        // Then
        returnedResponse.collect {
            Assert.assertEquals(
                "반환된 코드(${it.code})가 예상(${expectedResponse.code})과 다릅니다.",
                expectedResponse.code,
                it.code
            )
        }
        coVerify { mockSaveDiaryApi.addDiary(diaryInfo) }
    }

    @Test
    fun saveDiary_ReturnsRuntimeException() = runTest(testDispatcher) {
        // Given
        val diaryInfo = UserDiary(
            user_index = 1,
            type = "오늘의 마음 일기",
            date = "2023-08-19",
            day = "토요일",
            emotion = "기쁨",
            emotionDescription = "너무도 뿌듯했다.",
            situation = "할 일을 무사히 마쳤다.",
            thought = "잘 이겨냈다.",
            reflection = "앞으로도 힘 내 보자!"
        )
        coEvery { mockSaveDiaryApi.addDiary(diaryInfo) } throws RuntimeException("일기 저장 중 오류 발생")

        // When
        val returnedResponse = todayDiaryRemoteDataSource.saveDiary(diaryInfo)

        // Then
        assertFailsWith<RuntimeException> {
            returnedResponse.first()
        }
        coVerify { mockSaveDiaryApi.addDiary(diaryInfo) }
    }


}