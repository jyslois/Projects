package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryEmotionTitleUseCase
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
class SaveTodayDiaryEmotionTitleUseCaseTest {
    private lateinit var saveTodayDiaryEmotionTitleUseCase: SaveTodayDiaryEmotionTitleUseCase
    private val mockTodayDiaryRepository = mockk<TodayDiaryRepository>()

    // 테스트 데이터
    private val testEmotionTitle = "행복"

    @Before
    fun setUp() {
        saveTodayDiaryEmotionTitleUseCase = SaveTodayDiaryEmotionTitleUseCase(mockTodayDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTodayDiaryRepository)
    }

    @Test
    fun invoke_savingDiaryEmotionTitle() = runTest {
        // Given
        coEvery { mockTodayDiaryRepository.saveEmotion(any()) } just Runs

        // When
        saveTodayDiaryEmotionTitleUseCase(emotion = testEmotionTitle)

        // Then
        coVerify { mockTodayDiaryRepository.saveEmotion(testEmotionTitle) }
    }
}