package com.android.mymindnotes.domain

import com.android.mymindnotes.core.dto.SaveDiaryResponse
import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.today.ClearTodayDiaryTempRecordsUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryRecordDateUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryRecordDayUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryReflectionUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryTypeUseCase
import com.android.mymindnotes.domain.usecases.diaryRemote.SaveTodayDiaryUseCase
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class SaveTodayDiaryUseCaseTest {
    private lateinit var saveTodayDiaryUseCase: SaveTodayDiaryUseCase
    private val testDispatcher = StandardTestDispatcher()
    private val mockTodayDiaryRepository = mockk<TodayDiaryRepository>()
    private val mockSaveTodayDiaryReflectionUseCase = mockk<SaveTodayDiaryReflectionUseCase>()
    private val mockSaveTodayDiaryTypeUseCase = mockk<SaveTodayDiaryTypeUseCase>()
    private val mockSaveTodayDiaryRecordDateUseCase = mockk<SaveTodayDiaryRecordDateUseCase>()
    private val mockSaveTodayDiaryRecordDayUseCase = mockk<SaveTodayDiaryRecordDayUseCase>()
    private val mockClearTodayDiaryTempRecordsUseCase = mockk<ClearTodayDiaryTempRecordsUseCase>()

    // 테스트 데이터
    private val reflection = "내가 성장하고 있다는 것을 느끼는 하루였다"
    private val type = "오늘의 마음 일기"
    private val date = "2023-08-27"
    private val day = "일요일"

    @Before
    fun setUp() {
        saveTodayDiaryUseCase = SaveTodayDiaryUseCase(mockTodayDiaryRepository, mockSaveTodayDiaryReflectionUseCase, mockSaveTodayDiaryTypeUseCase, mockSaveTodayDiaryRecordDateUseCase, mockSaveTodayDiaryRecordDayUseCase, mockClearTodayDiaryTempRecordsUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockTodayDiaryRepository, mockSaveTodayDiaryReflectionUseCase, mockSaveTodayDiaryTypeUseCase, mockSaveTodayDiaryRecordDateUseCase, mockSaveTodayDiaryRecordDayUseCase, mockClearTodayDiaryTempRecordsUseCase)
    }

    @Test
    fun invoke_ReturnsSuccessfulResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = SaveDiaryResponse(code = 6000, msg = "일기 저장 성공")
        coEvery { mockTodayDiaryRepository.saveDiary() } returns flowOf(expectedResponse)
        coEvery { mockSaveTodayDiaryReflectionUseCase(any()) } just Runs
        coEvery { mockSaveTodayDiaryTypeUseCase(any()) } just Runs
        coEvery { mockSaveTodayDiaryRecordDateUseCase(any()) } just Runs
        coEvery { mockSaveTodayDiaryRecordDayUseCase(any()) } just Runs
        coEvery { mockClearTodayDiaryTempRecordsUseCase() } just Runs

        // When
        val actualResponseFlow = saveTodayDiaryUseCase(reflection, type, date, day)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isSuccess)
            val msg = it.getOrNull()
            assertNotNull(msg)
            assertEquals(expectedResponse.msg, msg, "반환된 메세지($msg)가 예상 메시지(${expectedResponse.msg})와 다릅니다.")
        }
        coVerify { saveTodayDiaryUseCase(reflection, type, date, day) }
        coVerify { mockSaveTodayDiaryReflectionUseCase(reflection) }
        coVerify { mockSaveTodayDiaryTypeUseCase(type) }
        coVerify { mockSaveTodayDiaryRecordDateUseCase(date) }
        coVerify { mockSaveTodayDiaryRecordDayUseCase(day) }
        coVerify { mockClearTodayDiaryTempRecordsUseCase() }
    }

    @Test
    fun invoke_ReturnsUnsuccessfulResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = SaveDiaryResponse(code = 6001, msg = "양식을 지키지 않아 일기가 저장되지 않았습니다")
        coEvery { mockTodayDiaryRepository.saveDiary() } returns flowOf(expectedResponse)
        coEvery { mockSaveTodayDiaryReflectionUseCase(any()) } just Runs
        coEvery { mockSaveTodayDiaryTypeUseCase(any()) } just Runs
        coEvery { mockSaveTodayDiaryRecordDateUseCase(any()) } just Runs
        coEvery { mockSaveTodayDiaryRecordDayUseCase(any()) } just Runs

        // When
        val actualResponseFlow = saveTodayDiaryUseCase(reflection, type, date, day)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isFailure)
            val exception = it.exceptionOrNull()
            assertNotNull(exception)
            assertTrue(exception is RuntimeException)
            assertEquals(expectedResponse.msg, exception.message, "반환된 예외 메세지(${exception.message})가 예상 예외 메시지(${expectedResponse.msg})와 다릅니다.")
        }
        coVerify { saveTodayDiaryUseCase(reflection, type, date, day) }
        coVerify { mockSaveTodayDiaryReflectionUseCase(reflection) }
        coVerify { mockSaveTodayDiaryTypeUseCase(type) }
        coVerify { mockSaveTodayDiaryRecordDateUseCase(date) }
        coVerify { mockSaveTodayDiaryRecordDayUseCase(day) }
    }

    @Test
    fun invoke_ThrowsRuntimeException() = runTest(testDispatcher) {
        // Given
        val errorMsg = "일기 저장에 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요."
        coEvery { mockTodayDiaryRepository.saveDiary() } throws RuntimeException(errorMsg)
        coEvery { mockSaveTodayDiaryReflectionUseCase(any()) } just Runs
        coEvery { mockSaveTodayDiaryTypeUseCase(any()) } just Runs
        coEvery { mockSaveTodayDiaryRecordDateUseCase(any()) } just Runs
        coEvery { mockSaveTodayDiaryRecordDayUseCase(any()) } just Runs

        // When
        val actualResponseFlow = saveTodayDiaryUseCase(reflection, type, date, day)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isFailure)
            val exception = it.exceptionOrNull()
            assertNotNull(exception)
            assertTrue(exception is RuntimeException)
            assertEquals(errorMsg, exception.message, "반환된 에러 메세지(${exception.message})가 예상 에러 메시지(${errorMsg})와 다릅니다.")
        }
        coVerify { saveTodayDiaryUseCase(reflection, type, date, day) }
        coVerify { mockSaveTodayDiaryReflectionUseCase(reflection) }
        coVerify { mockSaveTodayDiaryTypeUseCase(type) }
        coVerify { mockSaveTodayDiaryRecordDateUseCase(date) }
        coVerify { mockSaveTodayDiaryRecordDayUseCase(day) }
    }
}