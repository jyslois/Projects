package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiaryEmotionUseCase
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
class GetTraumaDiaryEmotionUseCaseTest {
    private lateinit var getTraumaDiaryEmotionUseCase: GetTraumaDiaryEmotionUseCase
    private val mockTraumaDiaryRepository = mockk<TraumaDiaryRepository>()

    // 테스트 데이터
    private val testEmotion = "테스트 감정"

    @Before
    fun setUp() {
        getTraumaDiaryEmotionUseCase = GetTraumaDiaryEmotionUseCase(mockTraumaDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTraumaDiaryRepository)
    }

    @Test
    fun invoke_retrievingDiaryEmotion() = runTest {
        // Given
        coEvery { mockTraumaDiaryRepository.getEmotion() } returns flowOf(testEmotion)

        // When
        val resultFlow = getTraumaDiaryEmotionUseCase()

        // Then
        resultFlow.collect { emotion ->
            assertEquals(testEmotion, emotion)
        }
        coVerify { mockTraumaDiaryRepository.getEmotion() }
    }
}
