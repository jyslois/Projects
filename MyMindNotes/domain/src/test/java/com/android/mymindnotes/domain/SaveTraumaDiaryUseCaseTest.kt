package com.android.mymindnotes.domain

import com.android.mymindnotes.core.dto.SaveDiaryResponse
import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.trauma.ClearTraumaDiaryTempRecordsUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryRecordDateUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryRecordDayUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryReflectionUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryTypeUseCase
import com.android.mymindnotes.domain.usecases.diaryRemote.SaveTraumaDiaryUseCase
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
class SaveTraumaDiaryUseCaseTest {
    private lateinit var saveTraumaDiaryUseCase: SaveTraumaDiaryUseCase
    private val testDispatcher = StandardTestDispatcher()
    private val mockTraumaDiaryRepository = mockk<TraumaDiaryRepository>()
    private val mockSaveTraumaDiaryReflectionUseCase = mockk<SaveTraumaDiaryReflectionUseCase>()
    private val mockSaveTraumaDiaryTypeUseCase = mockk<SaveTraumaDiaryTypeUseCase>()
    private val mockSaveTraumaDiaryRecordDateUseCase = mockk<SaveTraumaDiaryRecordDateUseCase>()
    private val mockSaveTraumaDiaryRecordDayUseCase = mockk<SaveTraumaDiaryRecordDayUseCase>()
    private val mockClearTraumaDiaryTempRecordsUseCase = mockk<ClearTraumaDiaryTempRecordsUseCase>()

    // 테스트 데이터
    private val reflection = "그때 이후로 트라우마가 생긴 것 같다"
    private val type = "트라우마 일기"
    private val date = "2023-08-27"
    private val day = "일요일"

    @Before
    fun setUp() {
        saveTraumaDiaryUseCase = SaveTraumaDiaryUseCase(mockTraumaDiaryRepository, mockSaveTraumaDiaryReflectionUseCase, mockSaveTraumaDiaryTypeUseCase, mockSaveTraumaDiaryRecordDateUseCase, mockSaveTraumaDiaryRecordDayUseCase, mockClearTraumaDiaryTempRecordsUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockTraumaDiaryRepository, mockSaveTraumaDiaryReflectionUseCase, mockSaveTraumaDiaryTypeUseCase, mockSaveTraumaDiaryRecordDateUseCase, mockSaveTraumaDiaryRecordDayUseCase, mockClearTraumaDiaryTempRecordsUseCase)
    }

    @Test
    fun invoke_ReturnsSuccessfulResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = SaveDiaryResponse(code = 6000, msg = "일기 저장 성공")
        coEvery { mockTraumaDiaryRepository.saveDiary() } returns flowOf(expectedResponse)
        coEvery { mockSaveTraumaDiaryReflectionUseCase(any()) } just Runs
        coEvery { mockSaveTraumaDiaryTypeUseCase(any()) } just Runs
        coEvery { mockSaveTraumaDiaryRecordDateUseCase(any()) } just Runs
        coEvery { mockSaveTraumaDiaryRecordDayUseCase(any()) } just Runs
        coEvery { mockClearTraumaDiaryTempRecordsUseCase() } just Runs

        // When
        val actualResponseFlow = saveTraumaDiaryUseCase(reflection, type, date, day)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isSuccess)
            val msg = it.getOrNull()
            assertNotNull(msg)
            assertEquals(expectedResponse.msg, msg, "반환된 메세지($msg)가 예상 메시지(${expectedResponse.msg})와 다릅니다.")
        }
        coVerify { saveTraumaDiaryUseCase(reflection, type, date, day) }
        coVerify { mockSaveTraumaDiaryReflectionUseCase(reflection) }
        coVerify { mockSaveTraumaDiaryTypeUseCase(type) }
        coVerify { mockSaveTraumaDiaryRecordDateUseCase(date) }
        coVerify { mockSaveTraumaDiaryRecordDayUseCase(day) }
        coVerify { mockClearTraumaDiaryTempRecordsUseCase() }
    }

    @Test
    fun invoke_ReturnsUnsuccessfulResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = SaveDiaryResponse(code = 6001, msg = "양식을 지키지 않아 일기가 저장되지 않았습니다")
        coEvery { mockTraumaDiaryRepository.saveDiary() } returns flowOf(expectedResponse)
        coEvery { mockSaveTraumaDiaryReflectionUseCase(any()) } just Runs
        coEvery { mockSaveTraumaDiaryTypeUseCase(any()) } just Runs
        coEvery { mockSaveTraumaDiaryRecordDateUseCase(any()) } just Runs
        coEvery { mockSaveTraumaDiaryRecordDayUseCase(any()) } just Runs

        // When
        val actualResponseFlow = saveTraumaDiaryUseCase(reflection, type, date, day)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isFailure)
            val exception = it.exceptionOrNull()
            assertNotNull(exception)
            assertTrue(exception is RuntimeException)
            assertEquals(expectedResponse.msg, exception.message, "반환된 예외 메세지(${exception.message})가 예상 예외 메시지(${expectedResponse.msg})와 다릅니다.")
        }
        coVerify { saveTraumaDiaryUseCase(reflection, type, date, day) }
        coVerify { mockSaveTraumaDiaryReflectionUseCase(reflection) }
        coVerify { mockSaveTraumaDiaryTypeUseCase(type) }
        coVerify { mockSaveTraumaDiaryRecordDateUseCase(date) }
        coVerify { mockSaveTraumaDiaryRecordDayUseCase(day) }
    }

    @Test
    fun invoke_ThrowsRuntimeException() = runTest(testDispatcher) {
        // Given
        val errorMsg = "일기 저장에 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요."
        coEvery { mockTraumaDiaryRepository.saveDiary() } throws RuntimeException(errorMsg)
        coEvery { mockSaveTraumaDiaryReflectionUseCase(any()) } just Runs
        coEvery { mockSaveTraumaDiaryTypeUseCase(any()) } just Runs
        coEvery { mockSaveTraumaDiaryRecordDateUseCase(any()) } just Runs
        coEvery { mockSaveTraumaDiaryRecordDayUseCase(any()) } just Runs

        // When
        val actualResponseFlow = saveTraumaDiaryUseCase(reflection, type, date, day)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isFailure)
            val exception = it.exceptionOrNull()
            assertNotNull(exception)
            assertTrue(exception is RuntimeException)
            assertEquals(errorMsg, exception.message, "반환된 에러 메세지(${exception.message})가 예상 에러 메시지(${errorMsg})와 다릅니다.")
        }
        coVerify { saveTraumaDiaryUseCase(reflection, type, date, day) }
        coVerify { mockSaveTraumaDiaryReflectionUseCase(reflection) }
        coVerify { mockSaveTraumaDiaryTypeUseCase(type) }
        coVerify { mockSaveTraumaDiaryRecordDateUseCase(date) }
        coVerify { mockSaveTraumaDiaryRecordDayUseCase(day) }
    }
}