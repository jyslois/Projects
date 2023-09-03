package com.android.mymindnotes.domain

import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryEmotionUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryEmotionTitleUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryEmotionColorUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryEmotionTextUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SaveTraumaDiaryEmotionUseCaseTest {
    private lateinit var saveTraumaDiaryEmotionUseCase: SaveTraumaDiaryEmotionUseCase
    private val mockSaveTraumaDiaryEmotionTitleUseCase = mockk<SaveTraumaDiaryEmotionTitleUseCase>()
    private val mockSaveTraumaDiaryEmotionColorUseCase = mockk<SaveTraumaDiaryEmotionColorUseCase>()
    private val mockSaveTraumaDiaryEmotionTextUseCase = mockk<SaveTraumaDiaryEmotionTextUseCase>()

    // 테스트 데이터
    private val testEmotion = "기쁨"
    private val testEmotionColor = 0xFFFFFF
    private val testEmotionText = "테스트 감정 텍스트"

    @Before
    fun setUp() {
        saveTraumaDiaryEmotionUseCase = SaveTraumaDiaryEmotionUseCase(
            mockSaveTraumaDiaryEmotionTitleUseCase,
            mockSaveTraumaDiaryEmotionColorUseCase,
            mockSaveTraumaDiaryEmotionTextUseCase
        )
    }

    @After
    fun tearDown() {
        clearMocks(
            mockSaveTraumaDiaryEmotionTitleUseCase,
            mockSaveTraumaDiaryEmotionColorUseCase,
            mockSaveTraumaDiaryEmotionTextUseCase
        )
    }

    @Test
    fun invoke_savingDiaryEmotionRelatedData() = runTest {
        // Given
        coEvery { mockSaveTraumaDiaryEmotionTitleUseCase(any()) } just Runs
        coEvery { mockSaveTraumaDiaryEmotionColorUseCase(any()) } just Runs
        coEvery { mockSaveTraumaDiaryEmotionTextUseCase(any()) } just Runs

        // When
        saveTraumaDiaryEmotionUseCase.invoke(emotion = testEmotion, emotionColor = testEmotionColor, emotionText = testEmotionText)

        // Then
        coVerify { mockSaveTraumaDiaryEmotionTitleUseCase(testEmotion) }
        coVerify { mockSaveTraumaDiaryEmotionColorUseCase(testEmotionColor) }
        coVerify { mockSaveTraumaDiaryEmotionTextUseCase(testEmotionText) }
    }
}
