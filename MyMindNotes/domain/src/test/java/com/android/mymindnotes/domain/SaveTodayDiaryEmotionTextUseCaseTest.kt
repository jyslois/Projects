package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryEmotionTextUseCase
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SaveTodayDiaryEmotionTextUseCaseTest {
    private lateinit var saveTodayDiaryEmotionTextUseCase: SaveTodayDiaryEmotionTextUseCase
    private val mockTodayDiaryRepository = mockk<TodayDiaryRepository>()

    @Before
    fun setUp() {
        saveTodayDiaryEmotionTextUseCase = SaveTodayDiaryEmotionTextUseCase(mockTodayDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTodayDiaryRepository)
    }

    // Test data
    private val testEmotionText = "감정 설명"

    @Test
    fun invoke_savingEmotionText() = runTest {
        // Given
        coEvery { mockTodayDiaryRepository.saveEmotionText(any()) } just Runs

        // When
        saveTodayDiaryEmotionTextUseCase(emotionText = testEmotionText)

        // Then
        coVerify { mockTodayDiaryRepository.saveEmotionText(testEmotionText) }
    }

}