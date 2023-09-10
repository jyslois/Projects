package com.android.mymindnotes.presentation

import com.android.mymindnotes.domain.usecases.diary.trauma.ClearTraumaDiaryTempRecordsUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiarySituationUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiarySituationUseCase
import com.android.mymindnotes.presentation.viewmodels.TraumaDiarySituationViewModel
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
class TraumaDiarySituationViewModelTest {

    private lateinit var traumaDiarySituationViewModel: TraumaDiarySituationViewModel
    private val mockSaveTraumaDiarySituationUseCase = mockk<SaveTraumaDiarySituationUseCase>()
    private val mockGetTraumaDiarySituationUseCase = mockk<GetTraumaDiarySituationUseCase>()
    private val mockClearTraumaDiaryTempRecordsUseCase = mockk<ClearTraumaDiaryTempRecordsUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { mockGetTraumaDiarySituationUseCase() } returns flowOf()
        traumaDiarySituationViewModel = TraumaDiarySituationViewModel(
            mockSaveTraumaDiarySituationUseCase,
            mockGetTraumaDiarySituationUseCase,
            mockClearTraumaDiaryTempRecordsUseCase
        )
    }

    @After
    fun tearDown() {
        clearMocks(
            mockSaveTraumaDiarySituationUseCase,
            mockGetTraumaDiarySituationUseCase,
            mockClearTraumaDiaryTempRecordsUseCase
        )
        Dispatchers.resetMain()
    }

    // Test Data
    private val situation = "테스트 상황."

    @Test
    fun nextButtonClicked_UiStateUpdateToSuccess() = runTest {
        // Given
        coEvery { mockSaveTraumaDiarySituationUseCase(any()) } just runs

        // When
        traumaDiarySituationViewModel.nextButtonClicked(situation)

        // Then
        assertEquals(
            TraumaDiarySituationViewModel.TraumaDiarySituationUiState.Success(situation),
            traumaDiarySituationViewModel.uiState.value
        )
        coVerify { mockSaveTraumaDiarySituationUseCase(situation) }
    }

    @Test
    fun onBackPressed_CallingClearTraumaDiaryTempRecordsUseCase() = runTest {
        // Given
        coEvery { mockClearTraumaDiaryTempRecordsUseCase() } just runs

        // When
        traumaDiarySituationViewModel.onBackPressed()

        // Then
        coVerify { mockClearTraumaDiaryTempRecordsUseCase() }
    }

    @Test
    fun `init_when situation fetched, UiState should change to Success`() = runTest {
        // Given
        coEvery { mockGetTraumaDiarySituationUseCase() } returns flowOf(situation)

        // When - init{} block is called by initializing the viewmodel
        traumaDiarySituationViewModel = TraumaDiarySituationViewModel(
            mockSaveTraumaDiarySituationUseCase,
            mockGetTraumaDiarySituationUseCase,
            mockClearTraumaDiaryTempRecordsUseCase
        )

        // wait until all coroutines finish
        advanceUntilIdle()

        // Then
        assertEquals(
            TraumaDiarySituationViewModel.TraumaDiarySituationUiState.Success(situation),
            traumaDiarySituationViewModel.uiState.value
        )
        coVerify { mockGetTraumaDiarySituationUseCase() }
    }

}