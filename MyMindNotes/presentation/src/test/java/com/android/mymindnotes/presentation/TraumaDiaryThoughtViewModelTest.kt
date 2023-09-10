package com.android.mymindnotes.presentation

import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiaryThoughtUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryThoughtUseCase
import com.android.mymindnotes.presentation.viewmodels.TraumaDiaryThoughtViewModel
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
class TraumaDiaryThoughtViewModelTest {
    private lateinit var traumaDiaryThoughtViewModel: TraumaDiaryThoughtViewModel
    private val mockSaveTraumaDiaryThoughtUseCase = mockk<SaveTraumaDiaryThoughtUseCase>()
    private val mockGetTraumaDiaryThoughtUseCase = mockk<GetTraumaDiaryThoughtUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { mockGetTraumaDiaryThoughtUseCase() } returns flowOf()
        traumaDiaryThoughtViewModel = TraumaDiaryThoughtViewModel(mockSaveTraumaDiaryThoughtUseCase, mockGetTraumaDiaryThoughtUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockSaveTraumaDiaryThoughtUseCase, mockGetTraumaDiaryThoughtUseCase)
        Dispatchers.resetMain()
    }

    // Test Data
    private val thought = "테스트 생각."

    @Test
    fun previousOrNextButtonClickedOrBackPressed_UiStateUpdateToSuccess() = runTest {
        // Given
        coEvery { mockSaveTraumaDiaryThoughtUseCase(any()) } just runs

        // When
        traumaDiaryThoughtViewModel.previousOrNextButtonClickedOrBackPressed(thought)

        // Then
        assertEquals(TraumaDiaryThoughtViewModel.TraumaDiaryThoughtUiState.Success(thought), traumaDiaryThoughtViewModel.uiState.value)
        coVerify { mockSaveTraumaDiaryThoughtUseCase(thought) }
    }

    @Test
    fun `init_when thought fetched, UiState should change to Success`() = runTest {
        // Given
        coEvery { mockGetTraumaDiaryThoughtUseCase() } returns flowOf(thought)

        // When - init{} block call by re-initializing the viewModel
        traumaDiaryThoughtViewModel = TraumaDiaryThoughtViewModel(mockSaveTraumaDiaryThoughtUseCase, mockGetTraumaDiaryThoughtUseCase)

        // wait until all coroutines finish
        advanceUntilIdle()

        // Then
        assertEquals(TraumaDiaryThoughtViewModel.TraumaDiaryThoughtUiState.Success(thought), traumaDiaryThoughtViewModel.uiState.value)
        coVerify { mockGetTraumaDiaryThoughtUseCase() }
    }
}