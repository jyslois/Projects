package com.android.mymindnotes.presentation

import com.android.mymindnotes.domain.usecases.diary.today.GetTodayDiarySituationUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiarySituationUseCase
import com.android.mymindnotes.presentation.viewmodels.TodayDiarySituationViewModel
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
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
class TodayDiarySituationViewModelTest {
    private lateinit var todayDiarySituationViewModel: TodayDiarySituationViewModel
    private val mockSaveTodayDiarySituationUseCase = mockk<SaveTodayDiarySituationUseCase>()
    private val mockGetTodayDiarySituationUseCase = mockk<GetTodayDiarySituationUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        todayDiarySituationViewModel = TodayDiarySituationViewModel(mockSaveTodayDiarySituationUseCase, mockGetTodayDiarySituationUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockSaveTodayDiarySituationUseCase, mockGetTodayDiarySituationUseCase)
        Dispatchers.resetMain()
    }

    // Test Data
    private val situation = "테스트 상황."

    @Test
    fun nextOrPreviousButtonClickedOrBackPressedOrPaused_UiStateUpdateToSuccess() = runTest {
        // Given
        coEvery { mockSaveTodayDiarySituationUseCase(any()) } just runs

        // When
        todayDiarySituationViewModel.nextOrPreviousButtonClickedOrBackPressedOrPaused(situation)

        // Then
        assertEquals(TodayDiarySituationViewModel.TodayDiarySituationUiState.Success(situation), todayDiarySituationViewModel.uiState.value)
        coVerify { mockSaveTodayDiarySituationUseCase(situation) }
    }

    @Test
    fun `init_when situation fetched, UiState should change to Success`() = runTest {
        // Given
        coEvery { mockGetTodayDiarySituationUseCase() } returns flowOf(situation)

        // When - init{} 블럭 호출을 위해 뷰모델 초기화
        todayDiarySituationViewModel = TodayDiarySituationViewModel(mockSaveTodayDiarySituationUseCase, mockGetTodayDiarySituationUseCase)

        // wait until all coroutines finish
        advanceUntilIdle()

        // Then
        assertEquals(TodayDiarySituationViewModel.TodayDiarySituationUiState.Success(situation), todayDiarySituationViewModel.uiState.value)
        coVerify { mockGetTodayDiarySituationUseCase() }
    }

}