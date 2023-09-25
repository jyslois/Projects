package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.today.GetTodayDiaryEmotionTextUseCase
import io.mockk.coEvery
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GetTodayDiaryEmotionTextUseCaseTest {
    private lateinit var getTodayDiaryEmotionTextUseCase: GetTodayDiaryEmotionTextUseCase
    private val mockTodayDiaryRepository = mockk<TodayDiaryRepository>()

    // 테스트 데이터
    private val testEmotionText = "오늘의 테스트 감정 텍스트"

    @Before
    fun setUp() {
        getTodayDiaryEmotionTextUseCase = GetTodayDiaryEmotionTextUseCase(mockTodayDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTodayDiaryRepository)
    }

    @Test
    fun invoke_retrievingTodayDiaryEmotionText() = runTest {
        // Given
        coEvery { mockTodayDiaryRepository.getEmotionText() } returns flowOf(testEmotionText)

        // When
        val resultFlow = getTodayDiaryEmotionTextUseCase()

        // Then
        resultFlow.collect { emotionText ->
            assertEquals(testEmotionText, emotionText)
        }
        coVerify { mockTodayDiaryRepository.getEmotionText() }
    }
}
