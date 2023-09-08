package com.android.mymindnotes.presentation

import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiaryEmotionTextUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiaryEmotionUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryEmotionUseCase
import com.android.mymindnotes.presentation.viewmodels.TraumaDiaryEmotionViewModel
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class TraumaDiaryEmotionViewModelTest {
    private lateinit var traumaDiaryEmotionViewModel: TraumaDiaryEmotionViewModel
    private val mockSaveTraumaDiaryEmotionUseCase = mockk<SaveTraumaDiaryEmotionUseCase>(relaxed = true)
    private val mockGetTraumaDiaryEmotionUseCase = mockk<GetTraumaDiaryEmotionUseCase>()
    private val mockGetTraumaDiaryEmotionTextUseCase = mockk<GetTraumaDiaryEmotionTextUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { mockGetTraumaDiaryEmotionUseCase() } returns flowOf()
        coEvery { mockGetTraumaDiaryEmotionTextUseCase() } returns flowOf()
        traumaDiaryEmotionViewModel = TraumaDiaryEmotionViewModel(
            mockSaveTraumaDiaryEmotionUseCase,
            mockGetTraumaDiaryEmotionUseCase,
            mockGetTraumaDiaryEmotionTextUseCase
        )
    }

    @After
    fun tearDown() {
        clearMocks(
            mockSaveTraumaDiaryEmotionUseCase,
            mockGetTraumaDiaryEmotionUseCase,
            mockGetTraumaDiaryEmotionTextUseCase
        )
        Dispatchers.resetMain()
    }

    // Test Data
    private val emotionId = R.id.happinessButton
    private val emotionColor = R.drawable.orange_happiness
    private val emotion = "기쁨"
    private val emotionText = "기쁜 마음!"

    @Test
    fun nextOrPreviousButtonClickedOrPaused_CallingSaveTraumaDiaryEmotionUseCase() = runTest {
        // Given
        coEvery { mockSaveTraumaDiaryEmotionUseCase(any(), any(), any()) } just Runs

        // When
        traumaDiaryEmotionViewModel.nextOrPreviousButtonClickedOrPaused(emotionId, emotionText)

        // wait until all coroutine tasks finish
        advanceUntilIdle()

        // Then
        coVerify { mockSaveTraumaDiaryEmotionUseCase(emotion, emotionColor, emotionText) }
    }

    @Test
    fun getEmotionTempRecords_UpdateUiStateToSuccess() = runTest {
        // Given
        coEvery { mockGetTraumaDiaryEmotionUseCase() } returns flowOf(emotion)
        coEvery { mockGetTraumaDiaryEmotionTextUseCase() } returns flowOf(emotionText)

        // When
        traumaDiaryEmotionViewModel.getEmotionTempRecords()

        // Then
        assertEquals(TraumaDiaryEmotionViewModel.TraumaDiaryEmotionUiState.Success(emotion, emotionText), traumaDiaryEmotionViewModel.uiState.value)
        coVerify { mockGetTraumaDiaryEmotionUseCase() }
        coVerify { mockGetTraumaDiaryEmotionTextUseCase() }
    }

    @Test
    fun `init_when emotion temp records fetched, UiState should change to Success`() = runTest {
        // Given
        coEvery { mockGetTraumaDiaryEmotionUseCase() } returns flowOf(emotion)
        coEvery { mockGetTraumaDiaryEmotionTextUseCase() } returns flowOf(emotionText)

        // When: Initialize the ViewModel to trigger the init block with the mock setup
        traumaDiaryEmotionViewModel = TraumaDiaryEmotionViewModel(
            mockSaveTraumaDiaryEmotionUseCase,
            mockGetTraumaDiaryEmotionUseCase,
            mockGetTraumaDiaryEmotionTextUseCase
        )

        // Wait until all coroutine tasks finish
        advanceUntilIdle()

        // Then
        assertEquals(TraumaDiaryEmotionViewModel.TraumaDiaryEmotionUiState.Success(emotion, emotionText), traumaDiaryEmotionViewModel.uiState.value)
        coVerify { mockGetTraumaDiaryEmotionUseCase() }
        coVerify { mockGetTraumaDiaryEmotionTextUseCase() }
    }
}