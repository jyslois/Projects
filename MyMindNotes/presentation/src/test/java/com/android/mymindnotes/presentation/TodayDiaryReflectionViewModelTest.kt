package com.android.mymindnotes.presentation

import com.android.mymindnotes.domain.usecases.diary.today.GetTodayDiaryReflectionUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryReflectionUseCase
import com.android.mymindnotes.domain.usecases.diaryRemote.SaveTodayDiaryUseCase
import com.android.mymindnotes.presentation.viewmodels.TodayDiaryReflectionViewModel
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
class TodayDiaryReflectionViewModelTest {
    private lateinit var todayDiaryReflectionViewModel: TodayDiaryReflectionViewModel
    private val mockSaveTodayDiaryUseCase = mockk<SaveTodayDiaryUseCase>()
    private val mockSaveTodayDiaryReflectionUseCase = mockk<SaveTodayDiaryReflectionUseCase>()
    private val mockGetTodayDiaryReflectionUseCase = mockk<GetTodayDiaryReflectionUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        todayDiaryReflectionViewModel = TodayDiaryReflectionViewModel(mockSaveTodayDiaryUseCase, mockSaveTodayDiaryReflectionUseCase, mockGetTodayDiaryReflectionUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockSaveTodayDiaryUseCase, mockSaveTodayDiaryReflectionUseCase, mockGetTodayDiaryReflectionUseCase)
        Dispatchers.resetMain()
    }

    // Test Data
    private val reflection = "테스트 회고."
    private val type = "오늘의 마음 일기"
    private val date = "2023-09-02"
    private val day = "토요일"

    @Test
    fun previousButtonClickedOrBackPressed_UiStateChangedToSuccess() = runTest {
        // Given
        coEvery { mockSaveTodayDiaryReflectionUseCase(any()) } just runs

        // When
        todayDiaryReflectionViewModel.previousButtonClickedOrBackPressed(reflection)

        // Then
        assertEquals(TodayDiaryReflectionViewModel.TodayDiaryReflectionUiState.Success(reflection), todayDiaryReflectionViewModel.uiState.value)
        coVerify { mockSaveTodayDiaryReflectionUseCase(reflection) }
    }

    @Test
    fun saveDiaryButtonClicked_ReturnsSuccessfulResponse_UiStateChangedToDiarySaved() = runTest {
        // Given
        val successResponse = Result.success("일기 쓰기 성공! 오늘 하루도 고생했어요.")
        coEvery { mockSaveTodayDiaryUseCase(reflection, type, date, day) } returns flowOf(successResponse)

        // When
        todayDiaryReflectionViewModel.saveDiaryButtonClicked(reflection, type, date, day)

        // Then
        assertEquals(TodayDiaryReflectionViewModel.TodayDiaryReflectionUiState.DiarySaved, todayDiaryReflectionViewModel.uiState.value)
        coVerify { mockSaveTodayDiaryUseCase(reflection, type, date, day) }
    }

    @Test
    fun saveDiaryButtonClicked_ReturnsUnsuccessfulResponseOrThrowsException_UiStateChangedToErrorAndThenToLoading() = runTest {
        // Given
        val failureResponse: Result<String?> = Result.failure(RuntimeException("일기 저장에 실패했습니다."))
        coEvery { mockSaveTodayDiaryUseCase(reflection, type, date, day) } returns flowOf(failureResponse)

        // When
        todayDiaryReflectionViewModel.saveDiaryButtonClicked(reflection, type, date, day)

        // Then
        assertTrue(todayDiaryReflectionViewModel.errorStateUpdated)
        assertEquals(TodayDiaryReflectionViewModel.TodayDiaryReflectionUiState.Loading, todayDiaryReflectionViewModel.uiState.value)
        coVerify { mockSaveTodayDiaryUseCase(reflection, type, date, day) }
    }

    @Test
    fun `init_when reflection fetched, UiState should change to Success`() = runTest {
        // Given
        coEvery { mockGetTodayDiaryReflectionUseCase() } returns flowOf(reflection)

        // When - init{} 블럭 호출을 위해 뷰모델 초기화
        todayDiaryReflectionViewModel = TodayDiaryReflectionViewModel(mockSaveTodayDiaryUseCase, mockSaveTodayDiaryReflectionUseCase, mockGetTodayDiaryReflectionUseCase)

        // wait until all coroutines finish
        advanceUntilIdle()

        // Then
        assertEquals(TodayDiaryReflectionViewModel.TodayDiaryReflectionUiState.Success(reflection), todayDiaryReflectionViewModel.uiState.value)
        coVerify { mockGetTodayDiaryReflectionUseCase() }
    }

}