package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryEmotionTitleUseCase
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
class SaveTraumaDiaryEmotionTitleUseCaseTest {
    private lateinit var saveTraumaDiaryEmotionTitleUseCase: SaveTraumaDiaryEmotionTitleUseCase
    private val mockTraumaDiaryRepository = mockk<TraumaDiaryRepository>()

    // 테스트 데이터
    private val testEmotionTitle = "슬픔"

    @Before
    fun setUp() {
        saveTraumaDiaryEmotionTitleUseCase = SaveTraumaDiaryEmotionTitleUseCase(mockTraumaDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTraumaDiaryRepository)
    }

    @Test
    fun invoke_savingDiaryEmotionTitle() = runTest {
        // Given
        coEvery { mockTraumaDiaryRepository.saveEmotion(any()) } just Runs

        // When
        saveTraumaDiaryEmotionTitleUseCase(emotion = testEmotionTitle)

        // Then
        coVerify { mockTraumaDiaryRepository.saveEmotion(testEmotionTitle) }
    }
}
