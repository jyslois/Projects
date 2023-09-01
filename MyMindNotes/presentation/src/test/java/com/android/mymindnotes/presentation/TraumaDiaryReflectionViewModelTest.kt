package com.android.mymindnotes.presentation

import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiaryReflectionUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryReflectionUseCase
import com.android.mymindnotes.domain.usecases.diaryRemote.SaveTraumaDiaryUseCase
import com.android.mymindnotes.presentation.viewmodels.TraumaDiaryReflectionViewModel
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
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class TraumaDiaryReflectionViewModelTest {

    private lateinit var traumaDiaryReflectionViewModel: TraumaDiaryReflectionViewModel
    private val mockSaveTraumaDiaryUseCase = mockk<SaveTraumaDiaryUseCase>()
    private val mockSaveTraumaDiaryReflectionUseCase = mockk<SaveTraumaDiaryReflectionUseCase>()
    private val mockGetTraumaDiaryReflectionUseCase = mockk<GetTraumaDiaryReflectionUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        traumaDiaryReflectionViewModel = TraumaDiaryReflectionViewModel(mockSaveTraumaDiaryUseCase, mockSaveTraumaDiaryReflectionUseCase, mockGetTraumaDiaryReflectionUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockSaveTraumaDiaryUseCase, mockSaveTraumaDiaryReflectionUseCase, mockGetTraumaDiaryReflectionUseCase)
        Dispatchers.resetMain()
    }

    // Test Data
    private val reflection = "테스트 회고."
    private val type = "트라우마 일기"
    private val date = "2023-09-02"
    private val day = "토요일"

    @Test
    fun previousButtonClickedOrBackPressed_UiStateChangedToSuccess() = runTest {
        // Given
        coEvery { mockSaveTraumaDiaryReflectionUseCase(any()) } just runs

        // When
        traumaDiaryReflectionViewModel.previousButtonClickedOrBackPressed(reflection)

        // Then
        assertEquals(TraumaDiaryReflectionViewModel.TraumaDiaryReflectionUiState.Success(reflection), traumaDiaryReflectionViewModel.uiState.value)
        coVerify { mockSaveTraumaDiaryReflectionUseCase(reflection) }
    }

    @Test
    fun saveDiaryButtonClicked_ReturnsSuccessfulResponse_UiStateChangedToDiarySaved() = runTest {
        // Given
        val successResponse = Result.success("일기 쓰기 성공! 오늘 하루도 고생했어요.")
        coEvery { mockSaveTraumaDiaryUseCase(reflection, type, date, day) } returns flowOf(successResponse)

        // When
        traumaDiaryReflectionViewModel.saveDiaryButtonClicked(reflection, type, date, day)

        // Then
        assertEquals(TraumaDiaryReflectionViewModel.TraumaDiaryReflectionUiState.DiarySaved, traumaDiaryReflectionViewModel.uiState.value)
        coVerify { mockSaveTraumaDiaryUseCase(reflection, type, date, day) }
    }

    @Test
    fun saveDiaryButtonClicked_ReturnsUnsuccessfulResponseOrThrowsException_UiStateChangedToErrorAndThenToLoading() = runTest {
        // Given
        val failureResponse: Result<String?> = Result.failure(RuntimeException("일기 저장에 실패했습니다."))
        coEvery { mockSaveTraumaDiaryUseCase(reflection, type, date, day) } returns flowOf(failureResponse)

        // When
        traumaDiaryReflectionViewModel.saveDiaryButtonClicked(reflection, type, date, day)

        // Then
        assertTrue(traumaDiaryReflectionViewModel.errorStateUpdated)
        assertEquals(TraumaDiaryReflectionViewModel.TraumaDiaryReflectionUiState.Loading, traumaDiaryReflectionViewModel.uiState.value)
        coVerify { mockSaveTraumaDiaryUseCase(reflection, type, date, day) }
    }

    @Test
    fun `init_when reflection fetched, UiState should change to Success`() = runTest {
        // Given
        coEvery { mockGetTraumaDiaryReflectionUseCase() } returns flowOf(reflection)

        // When - init{} block triggers viewModel instantiation
        traumaDiaryReflectionViewModel = TraumaDiaryReflectionViewModel(mockSaveTraumaDiaryUseCase, mockSaveTraumaDiaryReflectionUseCase, mockGetTraumaDiaryReflectionUseCase)

        // wait until all coroutines finish
        this.advanceUntilIdle()

        // Then
        assertEquals(TraumaDiaryReflectionViewModel.TraumaDiaryReflectionUiState.Success(reflection), traumaDiaryReflectionViewModel.uiState.value)
        coVerify { mockGetTraumaDiaryReflectionUseCase() }
    }

}