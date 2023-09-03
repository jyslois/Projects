package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryEmotionColorUseCase
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
class SaveTraumaDiaryEmotionColorUseCaseTest {
    private lateinit var saveTraumaDiaryEmotionColorUseCase: SaveTraumaDiaryEmotionColorUseCase
    private val mockTraumaDiaryRepository = mockk<TraumaDiaryRepository>()

    // 테스트 데이터
    private val testColor = 0xFFFFFF

    @Before
    fun setUp() {
        saveTraumaDiaryEmotionColorUseCase = SaveTraumaDiaryEmotionColorUseCase(mockTraumaDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTraumaDiaryRepository)
    }

    @Test
    fun invoke_savingDiaryEmotionColor() = runTest {
        // Given
        coEvery { mockTraumaDiaryRepository.saveEmotionColor(any()) } just Runs

        // When
        saveTraumaDiaryEmotionColorUseCase.invoke(color = testColor)

        // Then
        coVerify { mockTraumaDiaryRepository.saveEmotionColor(testColor) }
    }
}
