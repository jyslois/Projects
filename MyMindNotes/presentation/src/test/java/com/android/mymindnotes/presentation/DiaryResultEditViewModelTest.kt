package com.android.mymindnotes.presentation

import com.android.mymindnotes.domain.usecases.diaryRemote.UpdateDiaryUseCase
import com.android.mymindnotes.presentation.viewmodels.DiaryResultEditViewModel
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
class DiaryResultEditViewModelTest {
    private lateinit var diaryResultEditViewModel: DiaryResultEditViewModel
    private val mockUpdateDiaryUseCase = mockk<UpdateDiaryUseCase>()

    // Test data
    private val diaryNumber = 1
    private val situation = "테스트 상황"
    private val thought = "테스트 생각"
    private val emotion = "기쁨"
    private val emotionDescription = "테스트 감정 묘사"
    private val reflection = "테스트 회고"

    @Before
    fun setUp() {
        diaryResultEditViewModel = DiaryResultEditViewModel(mockUpdateDiaryUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockUpdateDiaryUseCase)
    }

    @Test
    fun updateDiaryButtonClicked_ReturnSuccessfulResult_UiStateChangedToSuccess() = runTest {
        // Given
        val msg = "일기 수정 완료"
        val successResult = Result.success(msg)
        coEvery { mockUpdateDiaryUseCase(diaryNumber, situation, thought, emotion, emotionDescription, reflection) } returns flowOf(successResult)

        // When
        diaryResultEditViewModel.updateDiaryButtonClicked(diaryNumber, situation, thought, emotion, emotionDescription, reflection)

        // Then
        assertEquals(DiaryResultEditViewModel.DiaryResultEditUiState.Success(msg), diaryResultEditViewModel.uiState.value)
        coVerify { mockUpdateDiaryUseCase(diaryNumber, situation, thought, emotion, emotionDescription, reflection) }
    }

    @Test
    fun updateDiaryButtonClicked_ReturnsUnUnsuccessfulResultOrThrowsException_UiStateChangedToErrorAndThenToLoading() = runTest {
        // Given
        val errorMsg = "일기 수정 실패."
        val failureResult: Result<String?> = Result.failure(RuntimeException(errorMsg))
        coEvery { mockUpdateDiaryUseCase(diaryNumber, situation, thought, emotion, emotionDescription, reflection) } returns flowOf(failureResult)

        // When
        diaryResultEditViewModel.updateDiaryButtonClicked(diaryNumber, situation, thought, emotion, emotionDescription, reflection)

        // Then
        assertTrue(diaryResultEditViewModel.errorStateTriggered)
        assertEquals(DiaryResultEditViewModel.DiaryResultEditUiState.Loading, diaryResultEditViewModel.uiState.value)
        coVerify { mockUpdateDiaryUseCase(diaryNumber, situation, thought, emotion, emotionDescription, reflection) }
    }
}