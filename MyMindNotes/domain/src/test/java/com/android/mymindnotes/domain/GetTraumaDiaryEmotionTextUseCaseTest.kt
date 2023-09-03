package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiaryEmotionTextUseCase
import io.mockk.coEvery
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GetTraumaDiaryEmotionTextUseCaseTest {
    private lateinit var getTraumaDiaryEmotionTextUseCase: GetTraumaDiaryEmotionTextUseCase
    private val mockTraumaDiaryRepository = mockk<TraumaDiaryRepository>()

    // 테스트 데이터
    private val testEmotionText = "테스트 감정 텍스트"

    @BeforeTest
    fun setUp() {
        getTraumaDiaryEmotionTextUseCase = GetTraumaDiaryEmotionTextUseCase(mockTraumaDiaryRepository)
    }

    @AfterTest
    fun tearDown() {
        clearMocks(mockTraumaDiaryRepository)
    }

    @Test
    fun invoke_retrievingDiaryEmotionText() = runTest {
        // Given
        coEvery { mockTraumaDiaryRepository.getEmotionText() } returns flowOf(testEmotionText)

        // When
        val resultFlow = getTraumaDiaryEmotionTextUseCase()

        // Then
        resultFlow.collect { emotionText ->
            assertEquals(testEmotionText, emotionText)
        }
        coVerify { mockTraumaDiaryRepository.getEmotionText() }
    }
}
