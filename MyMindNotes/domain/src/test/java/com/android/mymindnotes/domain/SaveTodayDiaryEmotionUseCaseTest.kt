package com.android.mymindnotes.domain

import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryEmotionColorUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryEmotionTextUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryEmotionTitleUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryEmotionUseCase
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
class SaveTodayDiaryEmotionUseCaseTest {
    private lateinit var saveTodayDiaryEmotionUseCase: SaveTodayDiaryEmotionUseCase
    private val mockSaveTodayDiaryEmotionTitleUseCase = mockk<SaveTodayDiaryEmotionTitleUseCase>()
    private val mockSaveTodayDiaryEmotionColorUseCase = mockk<SaveTodayDiaryEmotionColorUseCase>()
    private val mockSaveTodayDiaryEmotionTextUseCase = mockk<SaveTodayDiaryEmotionTextUseCase>()

    // 테스트 데이터
    private val testEmotion = "행복"
    private val testEmotionColor = 0xFFFFFF
    private val testEmotionText = "테스트 감정 텍스트"

    @Before
    fun setUp() {
        saveTodayDiaryEmotionUseCase = SaveTodayDiaryEmotionUseCase(
            mockSaveTodayDiaryEmotionTitleUseCase,
            mockSaveTodayDiaryEmotionColorUseCase,
            mockSaveTodayDiaryEmotionTextUseCase
        )
    }

    @After
    fun tearDown() {
        clearMocks(
            mockSaveTodayDiaryEmotionTitleUseCase,
            mockSaveTodayDiaryEmotionColorUseCase,
            mockSaveTodayDiaryEmotionTextUseCase
        )
    }

    @Test
    fun invoke_savingDiaryEmotionRelatedData() = runTest {
        // Given
        coEvery { mockSaveTodayDiaryEmotionTitleUseCase(any()) } just Runs
        coEvery { mockSaveTodayDiaryEmotionColorUseCase(any()) } just Runs
        coEvery { mockSaveTodayDiaryEmotionTextUseCase(any()) } just Runs

        // When
        saveTodayDiaryEmotionUseCase(emotion = testEmotion, emotionColor = testEmotionColor, emotionText = testEmotionText)

        // Then
        coVerify { mockSaveTodayDiaryEmotionTitleUseCase(testEmotion) }
        coVerify { mockSaveTodayDiaryEmotionColorUseCase(testEmotionColor) }
        coVerify { mockSaveTodayDiaryEmotionTextUseCase(testEmotionText) }
    }
}