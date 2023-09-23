package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.today.GetTodayDiaryEmotionUseCase
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
class GetTodayDiaryEmotionUseCaseTest {
    private lateinit var getTodayDiaryEmotionUseCase: GetTodayDiaryEmotionUseCase
    private val mockTodayDiaryRepository = mockk<TodayDiaryRepository>()

    // 테스트 데이터
    private val testEmotion = "테스트 감정"

    @Before
    fun setUp() {
        getTodayDiaryEmotionUseCase = GetTodayDiaryEmotionUseCase(mockTodayDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTodayDiaryRepository)
    }

    @Test
    fun invoke_retrievingTodayDiaryEmotion() = runTest {
        // Given
        coEvery { mockTodayDiaryRepository.getEmotion() } returns flowOf(testEmotion)

        // When
        val resultFlow = getTodayDiaryEmotionUseCase()

        // Then
        resultFlow.collect { emotion ->
            assertEquals(testEmotion, emotion)
        }
        coVerify { mockTodayDiaryRepository.getEmotion() }
    }
}
