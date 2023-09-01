package com.android.mymindnotes.presentation

import com.android.mymindnotes.domain.usecases.diary.today.ClearTodayDiaryTempRecordsUseCase
import com.android.mymindnotes.domain.usecases.diary.today.GetTodayDiaryEmotionTextUseCase
import com.android.mymindnotes.domain.usecases.diary.today.GetTodayDiaryEmotionUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryEmotionUseCase
import com.android.mymindnotes.presentation.viewmodels.TodayDiaryEmotionViewModel
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
class TodayDiaryEmotionViewModelTest {
    private lateinit var todayDiaryEmotionViewModel: TodayDiaryEmotionViewModel
    private val mockSaveTodayDiaryEmotionUseCase = mockk<SaveTodayDiaryEmotionUseCase>(relaxed = true)
    private val mockGetTodayDiaryEmotionUseCase = mockk<GetTodayDiaryEmotionUseCase>()
    private val mockGetTodayDiaryEmotionTextUseCase = mockk<GetTodayDiaryEmotionTextUseCase>()
    private val mockClearTodayDiaryTempRecordsUseCase = mockk<ClearTodayDiaryTempRecordsUseCase>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        todayDiaryEmotionViewModel = TodayDiaryEmotionViewModel(
            mockSaveTodayDiaryEmotionUseCase,
            mockGetTodayDiaryEmotionUseCase,
            mockGetTodayDiaryEmotionTextUseCase,
            mockClearTodayDiaryTempRecordsUseCase
        )
    }

    @After
    fun tearDown() {
        clearMocks(
            mockSaveTodayDiaryEmotionUseCase,
            mockGetTodayDiaryEmotionUseCase,
            mockGetTodayDiaryEmotionTextUseCase,
            mockClearTodayDiaryTempRecordsUseCase
        )
        Dispatchers.resetMain()
    }

    // Test Data
    private val emotionId = R.id.happinessButton
    private val emotionColor = R.drawable.orange_happiness
    private val emotion = "기쁨"
    private val emotionText = "기쁜 마음!"

    @Test
    fun nextButtonClicked_CallingSaveTodayDiaryEmotionUseCase() = runTest {
        // Given
        coEvery { mockSaveTodayDiaryEmotionUseCase(any(), any(), any()) } just Runs

        // When
        todayDiaryEmotionViewModel.nextButtonClicked(emotionId, emotionText)

        // wait until all coroutine tasks finish
        advanceUntilIdle()

        // Then
        coVerify { mockSaveTodayDiaryEmotionUseCase(emotion, emotionColor, emotionText) }
    }

    @Test
    fun finishButtonClicked_CallingClearTodayDiaryTemRecordsUseCase() = runTest {
        // Given
        coEvery { mockClearTodayDiaryTempRecordsUseCase() } just Runs

        // When
        todayDiaryEmotionViewModel.finishButtonClicked()

        // wait until all coroutine tasks finish
        advanceUntilIdle()

        // Then
        coVerify { mockClearTodayDiaryTempRecordsUseCase() }
    }

    @Test
    fun getEmotionTempRecords_UpdateUiStateToSuccess() = runTest {
        // Given
        coEvery { mockGetTodayDiaryEmotionUseCase() } returns flowOf(emotion)
        coEvery { mockGetTodayDiaryEmotionTextUseCase() } returns flowOf(emotionText)

        // When
        todayDiaryEmotionViewModel.getEmotionTempRecords()

        // Then
        assertEquals(TodayDiaryEmotionViewModel.TodayDiaryEmotionUiState.Success(emotion, emotionText), todayDiaryEmotionViewModel.uiState.value)
        coVerify { mockGetTodayDiaryEmotionUseCase() }
        coVerify { mockGetTodayDiaryEmotionTextUseCase() }
    }

    @Test
    fun `init_when emotion temp records fetched, UiState should change to Success`() = runTest {
        // Given
        coEvery { mockGetTodayDiaryEmotionUseCase() } returns flowOf(emotion)
        coEvery { mockGetTodayDiaryEmotionTextUseCase() } returns flowOf(emotionText)

        // When: Initialize the ViewModel to trigger the init block with the mock setup
        todayDiaryEmotionViewModel = TodayDiaryEmotionViewModel(
            mockSaveTodayDiaryEmotionUseCase,
            mockGetTodayDiaryEmotionUseCase,
            mockGetTodayDiaryEmotionTextUseCase,
            mockClearTodayDiaryTempRecordsUseCase
        )

        // Wait until all coroutine tasks finish
        advanceUntilIdle()

        // Then
        assertEquals(TodayDiaryEmotionViewModel.TodayDiaryEmotionUiState.Success(emotion, emotionText), todayDiaryEmotionViewModel.uiState.value)
        coVerify { mockGetTodayDiaryEmotionUseCase() }
        coVerify { mockGetTodayDiaryEmotionTextUseCase() }
    }
}