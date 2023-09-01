package com.android.mymindnotes.presentation

import com.android.mymindnotes.domain.usecases.diary.today.GetTodayDiaryThoughtUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryThoughtUseCase
import com.android.mymindnotes.presentation.viewmodels.TodayDiaryThoughtViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.clearMocks
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
class TodayDiaryThoughtViewModelTest {
    private lateinit var todayDiaryThoughtViewModel: TodayDiaryThoughtViewModel
    private val mockSaveTodayDiaryThoughtUseCase = mockk<SaveTodayDiaryThoughtUseCase>()
    private val mockGetTodayDiaryThoughtUseCase = mockk<GetTodayDiaryThoughtUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        todayDiaryThoughtViewModel = TodayDiaryThoughtViewModel(mockSaveTodayDiaryThoughtUseCase, mockGetTodayDiaryThoughtUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockSaveTodayDiaryThoughtUseCase, mockGetTodayDiaryThoughtUseCase)
        Dispatchers.resetMain()
    }

    // Test Data
    private val thought = "테스트 생각."

    @Test
    fun nextOrPreviousButtonClickedOrBackPressedOrOnPause_UiStateUpdateToSuccess() = runTest {
        // Given
        coEvery { mockSaveTodayDiaryThoughtUseCase(any()) } just runs

        // When
        todayDiaryThoughtViewModel.nextOrPreviousButtonClickedOrBackPressedOrOnPause(thought)

        // Then
        assertEquals(TodayDiaryThoughtViewModel.TodayDiaryThoughtUiState.Success(thought), todayDiaryThoughtViewModel.uiState.value)
        coVerify { mockSaveTodayDiaryThoughtUseCase(thought) }
    }

    @Test
    fun `init_when thought fetched, UiState should change to Success`() = runTest {
        // Given
        coEvery { mockGetTodayDiaryThoughtUseCase() } returns flowOf(thought)

        // When - init{} block call by re-initializing the viewModel
        todayDiaryThoughtViewModel = TodayDiaryThoughtViewModel(mockSaveTodayDiaryThoughtUseCase, mockGetTodayDiaryThoughtUseCase)

        // wait until all coroutines finish
        advanceUntilIdle()

        // Then
        assertEquals(TodayDiaryThoughtViewModel.TodayDiaryThoughtUiState.Success(thought), todayDiaryThoughtViewModel.uiState.value)
        coVerify { mockGetTodayDiaryThoughtUseCase() }
    }
}