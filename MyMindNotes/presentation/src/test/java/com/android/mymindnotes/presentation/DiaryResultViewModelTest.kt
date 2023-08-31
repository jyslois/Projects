package com.android.mymindnotes.presentation

import com.android.mymindnotes.core.dto.Diary
import com.android.mymindnotes.core.dto.GetDiaryListResponse
import com.android.mymindnotes.domain.usecases.diaryRemote.DeleteDiaryUseCase
import com.android.mymindnotes.domain.usecases.diaryRemote.GetDiaryListUseCase
import com.android.mymindnotes.presentation.viewmodels.DiaryResultViewModel
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class DiaryResultViewModelTest {
    private lateinit var diaryResultViewModel: DiaryResultViewModel
    private val mockDeleteDiaryUseCase = mockk<DeleteDiaryUseCase>()
    private val mockGetDiaryListUseCase = mockk<GetDiaryListUseCase>()

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

    @Before
    fun setUp() {
        diaryResultViewModel = DiaryResultViewModel(mockDeleteDiaryUseCase, mockGetDiaryListUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockDeleteDiaryUseCase, mockGetDiaryListUseCase)
    }

    @Test
    fun getDiaryList_ReturnSuccessfulResult_UiStateChangedToSuccess() = runTest {
        // Given
        val successResponse = GetDiaryListResponse(code = 7000, diaryList = sampleDiaryList)
        val successResult = Result.success(successResponse)
        coEvery { mockGetDiaryListUseCase() } returns flowOf(successResult)

        // When
        diaryResultViewModel.getDiaryList()

        // Then
        assertEquals(DiaryResultViewModel.DiaryResultUiState.Success(sampleDiaryList), diaryResultViewModel.uiState.value)
        coVerify { mockGetDiaryListUseCase() }
    }

    @Test
    fun getDiaryList_ThrowsException_UiStateChangedToErrorAndThenToLoading() = runTest {
        // Given
        val errorMsg = "일기를 불러오는 데 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요."
        val failureResult: Result<GetDiaryListResponse> = Result.failure(RuntimeException(errorMsg))
        coEvery { mockGetDiaryListUseCase() } returns flowOf(failureResult)

        // When
        diaryResultViewModel.getDiaryList()

        // Then
        assertTrue(diaryResultViewModel.errorStateTriggered)
        assertEquals(DiaryResultViewModel.DiaryResultUiState.Loading, diaryResultViewModel.uiState.value)
        coVerify { mockGetDiaryListUseCase() }
    }

    @Test
    fun deleteDiaryButtonClicked_ReturnSuccessfulResult_UiStateChangedToFinish() = runTest {
        // Given
        val msg = "일기 삭제 완료"
        val successResult = Result.success(msg)
        coEvery { mockDeleteDiaryUseCase(diaryNumber) } returns flowOf(successResult)

        // When
        diaryResultViewModel.deleteDiaryButtonClicked(diaryNumber)

        // Then
        assertEquals(DiaryResultViewModel.DiaryResultUiState.Finish, diaryResultViewModel.uiState.value)
        coVerify { mockDeleteDiaryUseCase(diaryNumber) }
    }

    @Test
    fun deleteDiaryButtonClicked_ThrowsException_UiStateChangedToErrorAndThenToLoading() = runTest {
        // Given
        val errorMsg = "일기 삭제에 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요."
        val failureResult: Result<String?> = Result.failure(RuntimeException(errorMsg))
        coEvery { mockDeleteDiaryUseCase(diaryNumber) } returns flowOf(failureResult)

        // When
        diaryResultViewModel.deleteDiaryButtonClicked(diaryNumber)

        // Then
        assertTrue(diaryResultViewModel.errorStateTriggered)
        assertEquals(DiaryResultViewModel.DiaryResultUiState.Loading, diaryResultViewModel.uiState.value)
        coVerify { mockDeleteDiaryUseCase(diaryNumber) }
    }
}