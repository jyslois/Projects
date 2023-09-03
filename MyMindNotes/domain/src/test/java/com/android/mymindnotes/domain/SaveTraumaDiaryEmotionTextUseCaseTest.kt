package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
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
class SaveTraumaDiaryEmotionTextUseCaseTest {
    private lateinit var saveTraumaDiaryEmotionTextUseCase: SaveTraumaDiaryEmotionTextUseCase
    private val mockTraumaDiaryRepository = mockk<TraumaDiaryRepository>()

    // 테스트 데이터
    private val testEmotionText = "테스트 감정 텍스트"

    @Before
    fun setUp() {
        saveTraumaDiaryEmotionTextUseCase = SaveTraumaDiaryEmotionTextUseCase(mockTraumaDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTraumaDiaryRepository)
    }

    @Test
    fun invoke_savingDiaryEmotionText() = runTest {
        // Given
        coEvery { mockTraumaDiaryRepository.saveEmotionText(any()) } just Runs

        // When
        saveTraumaDiaryEmotionTextUseCase.invoke(emotionText = testEmotionText)

        // Then
        coVerify { mockTraumaDiaryRepository.saveEmotionText(testEmotionText) }
    }
}
