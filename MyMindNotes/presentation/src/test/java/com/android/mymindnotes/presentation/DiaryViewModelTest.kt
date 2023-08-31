package com.android.mymindnotes.presentation

import com.android.mymindnotes.core.dto.Diary
import com.android.mymindnotes.core.dto.GetDiaryListResponse
import com.android.mymindnotes.domain.usecases.diaryRemote.GetDiaryListUseCase
import com.android.mymindnotes.presentation.viewmodels.DiaryViewModel
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
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
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class DiaryViewModelTest {
    private lateinit var diaryViewModel: DiaryViewModel
    private val mockGetDiaryListUseCase = mockk<GetDiaryListUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        diaryViewModel = DiaryViewModel(mockGetDiaryListUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockGetDiaryListUseCase)
        Dispatchers.resetMain()
    }

    // Test data
    val diaryNumber = 1
    val sampleDiaryList = arrayListOf(
        Diary(
            date = "2023-08-31",
            day = "목요일",
            diaryNumber = diaryNumber,
            emotion = "기쁨",
            emotionDescription = "Feeling Gret",
            reflection = "It was a good day",
            situation = "Tested the code",
            thought = "It works",
            type = "오늘의 마음 일기",
            userIndex = 1
        )
    )

    @Test
    fun getDiaryList_ReturnSuccessfulResult_UiStateChangedToSuccess() = runTest {
        // Given
        val successResponse = GetDiaryListResponse(code = 7000, diaryList = sampleDiaryList)
        val successResult = Result.success(successResponse)
        coEvery { mockGetDiaryListUseCase() } returns flowOf(successResult)

        // When
        diaryViewModel.getDiaryList()

        // Then
        assertEquals(DiaryViewModel.DiaryUiState.Success(sampleDiaryList), diaryViewModel.uiState.value)
        coVerify { mockGetDiaryListUseCase() }
    }

    @Test
    fun getDiaryList_ThrowsException_UiStateChangedToErrorAndThenToLoading() = runTest {
        // Given
        val errorMsg = "일기를 불러오는 데 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요."
        val failureResult: Result<GetDiaryListResponse> = Result.failure(RuntimeException(errorMsg))
        coEvery { mockGetDiaryListUseCase() } returns flowOf(failureResult)

        // When
        diaryViewModel.getDiaryList()

        // Then
        assertTrue(diaryViewModel.errorStateTriggered)
        assertEquals(DiaryViewModel.DiaryUiState.Loading, diaryViewModel.uiState.value)
        coVerify { mockGetDiaryListUseCase() }
    }

    @Test
    fun `init_when getting diary list is successful, should change UiState to Success`() = runTest {
        // Given
        val successResponse = GetDiaryListResponse(code = 7000, diaryList = sampleDiaryList)
        val successResult = Result.success(successResponse)
        coEvery { mockGetDiaryListUseCase() } returns flowOf(successResult)

        // When: Initialize the ViewModel to trigger the init block with the mock setup
        diaryViewModel = DiaryViewModel(mockGetDiaryListUseCase)

        // Wait until all coroutine tasks finish
        advanceUntilIdle()

        // Then
        assertEquals(DiaryViewModel.DiaryUiState.Success(sampleDiaryList), diaryViewModel.uiState.value)
        coVerify { mockGetDiaryListUseCase() }
    }

    @Test
    fun `init_when getting diary list fails, should change UiState to Error and then to Loading`() = runTest {
        // Given
        val errorMsg = "일기를 불러오는 데 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요."
        val failureResult: Result<GetDiaryListResponse> = Result.failure(RuntimeException(errorMsg))
        coEvery { mockGetDiaryListUseCase() } returns flowOf(failureResult)

        // When: Initialize the ViewModel to trigger the init block with the mock setup
        diaryViewModel = DiaryViewModel(mockGetDiaryListUseCase)

        // Wait until all coroutine tasks finish
        advanceUntilIdle()

        // Then
        assertTrue(diaryViewModel.errorStateTriggered)
        assertEquals(DiaryViewModel.DiaryUiState.Loading, diaryViewModel.uiState.value)
        coVerify { mockGetDiaryListUseCase() }
    }

}