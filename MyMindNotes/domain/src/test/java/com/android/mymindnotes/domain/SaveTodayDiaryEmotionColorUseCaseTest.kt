package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryEmotionColorUseCase
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
class SaveTodayDiaryEmotionColorUseCaseTest {
    private lateinit var saveTodayDiaryEmotionColorUseCase: SaveTodayDiaryEmotionColorUseCase
    private val mockTodayDiaryRepository = mockk<TodayDiaryRepository>()

    @Before
    fun setUp() {
        saveTodayDiaryEmotionColorUseCase = SaveTodayDiaryEmotionColorUseCase(mockTodayDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTodayDiaryRepository)
    }

    // Test Data
    private val testEmotionColor = 0xFFFFFF

    @Test
    fun invoke_savingDiaryEmotionColor() = runTest {
        // Given
        coEvery { mockTodayDiaryRepository.saveEmotionColor(any()) } just Runs

        // When
        saveTodayDiaryEmotionColorUseCase(color = testEmotionColor)

        // Then
        coVerify { mockTodayDiaryRepository.saveEmotionColor(testEmotionColor) }
    }

}