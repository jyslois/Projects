package com.android.mymindnotes.domain

import com.android.mymindnotes.core.dto.Diary
import com.android.mymindnotes.core.dto.GetDiaryListResponse
import com.android.mymindnotes.data.repositoryInterfaces.DiaryRepository
import com.android.mymindnotes.domain.usecases.diaryRemote.GetDiaryListUseCase
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
class GetDiaryListUseCaseTest {
    private lateinit var getDiaryListUseCase: GetDiaryListUseCase
    private val testDispatcher = StandardTestDispatcher()
    private val mockDiaryRepository = mockk<DiaryRepository>()

    // 테스트 데이터
    private val diary1 = Diary(date = "2023-08-27", day = "일요일", diaryNumber = 1, emotion = "기쁨", emotionDescription = "행복함을 느꼈다", situation = "마제소바를 먹었다", thought = "와, 너무 맛있어!", reflection = "맛있는 음식을 먹으니까 기분이 좋았다", type = "오늘의 마음 일기", userIndex = 1)
    private val diary2 = Diary(date = "2023-08-28", day = "월요일", diaryNumber = 2, emotion = "슬픔", emotionDescription = "체해서 힘들다", situation = "마제소바를 먹은 게 체했나 보다", thought = "맛있긴 정말 맛있었는데", reflection = "다음엔 꼭꼭 씹어서 먹어야겠다", type = "오늘의 마음 일기", userIndex = 1)
    private val diaryList = arrayListOf(diary1, diary2)

    @Before
    fun setUp() {
        getDiaryListUseCase = GetDiaryListUseCase(mockDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockDiaryRepository)
    }

    @Test
    fun invoke_ReturnsNormalResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = GetDiaryListResponse(code = 7000, diaryList = diaryList)
        coEvery { mockDiaryRepository.getDiaryList() } returns flowOf(expectedResponse)

        // When
        val actualResponseFlow = getDiaryListUseCase()

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isSuccess)
            val response = it.getOrNull()
            assertNotNull(response)
            assertEquals(expectedResponse, response, "반환된 응답($response)이 예상된 응답(${expectedResponse})과 다릅니다")
        }
        coVerify { mockDiaryRepository.getDiaryList() }
    }

    @Test
    fun invoke_ThrowsRuntimeException() = runTest(testDispatcher) {
        // Given
        val errorMsg = "일기 목록을 불러오는 데 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요."
        coEvery { mockDiaryRepository.getDiaryList() } throws RuntimeException(errorMsg)

        // When
        val actualResponseFlow = getDiaryListUseCase()

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isFailure)
            val exception = it.exceptionOrNull()
            assertNotNull(exception)
            assertTrue(exception is RuntimeException)
            assertEquals(errorMsg, exception.message, "반환된 에러 메세지(${exception.message})가 예상 에러 메시지(${errorMsg})와 다릅니다.")
        }
        coVerify { mockDiaryRepository.getDiaryList() }
    }
}