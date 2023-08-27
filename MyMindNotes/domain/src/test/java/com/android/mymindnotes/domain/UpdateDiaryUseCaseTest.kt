package com.android.mymindnotes.domain

import com.android.mymindnotes.core.dto.UpdateDiaryResponse
import com.android.mymindnotes.data.repositoryInterfaces.DiaryRepository
import com.android.mymindnotes.domain.usecases.diaryRemote.UpdateDiaryUseCase
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
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
class UpdateDiaryUseCaseTest {
    private lateinit var updateDiaryUseCase: UpdateDiaryUseCase
    private val testDispatcher = StandardTestDispatcher()
    private val mockDiaryRepository = mockk<DiaryRepository>()

    // 테스트 데이터
    val diaryNumber = 1
    val situation = "보드게임 벙을 갔다"
    val thought = "새로운 게임을 많이 가르쳐 주네"
    val emotion = "기쁨"
    val emotionDescription = "에너지를 얻는 기분"
    val reflection = "새로운 사람들도 만나고 새로운 게임들에도 도전하니 기쁘고 뿌듯했다"

    @Before
    fun setUp() {
        updateDiaryUseCase = UpdateDiaryUseCase(mockDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockDiaryRepository)
    }

    @Test
    fun invoke_ReturnsSuccessfulResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = UpdateDiaryResponse(code = 8000, msg = "일기 수정 성공")
        coEvery { mockDiaryRepository.updateDiary(diaryNumber, situation, thought, emotion, emotionDescription, reflection) } returns flowOf(expectedResponse)

        // When
        val actualResponseFlow = updateDiaryUseCase(diaryNumber, situation, thought, emotion, emotionDescription, reflection)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isSuccess)
            val msg = it.getOrNull()
            assertNotNull(msg)
            assertEquals(expectedResponse.msg, msg, "반환된 메세지($msg)가 예상 메시지(${expectedResponse.msg})와 다릅니다.")
        }
        coVerify { mockDiaryRepository.updateDiary(diaryNumber, situation, thought, emotion, emotionDescription, reflection) }
    }

    @Test
    fun invoke_ReturnsUnsuccessfulResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = UpdateDiaryResponse(code = 8001, msg = "양식을 지키지 않아 일기가 수정되지 않았습니다")
        coEvery { mockDiaryRepository.updateDiary(diaryNumber, situation, thought, emotion, emotionDescription, reflection) } returns flowOf(expectedResponse)

        // When
        val actualResponseFlow = updateDiaryUseCase(diaryNumber, situation, thought, emotion, emotionDescription, reflection)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isFailure)
            val exception = it.exceptionOrNull()
            assertNotNull(exception)
            assertTrue(exception is RuntimeException)
            assertEquals(expectedResponse.msg, exception.message, "반환된 예외 메세지(${exception.message})가 예상 예외 메시지(${expectedResponse.msg})와 다릅니다.")
        }
        coVerify { mockDiaryRepository.updateDiary(diaryNumber, situation, thought, emotion, emotionDescription, reflection) }
    }

    @Test
    fun invoke_ThrowsRuntimeException() = runTest(testDispatcher) {
        // Given
        val errorMsg = "일기 수정 실패. 인터넷 연결을 확인해 주세요."
        coEvery { mockDiaryRepository.updateDiary(diaryNumber, situation, thought, emotion, emotionDescription, reflection) } throws RuntimeException(errorMsg)


        // When
        val actualResponseFlow = updateDiaryUseCase(diaryNumber, situation, thought, emotion, emotionDescription, reflection)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isFailure)
            val exception = it.exceptionOrNull()
            assertNotNull(exception)
            assertTrue(exception is RuntimeException)
            assertEquals(errorMsg, exception.message, "반환된 에러 메세지(${exception.message})가 예상 에러 메시지(${errorMsg})와 다릅니다.")
        }
        coVerify { mockDiaryRepository.updateDiary(diaryNumber, situation, thought, emotion, emotionDescription, reflection) }
    }
}