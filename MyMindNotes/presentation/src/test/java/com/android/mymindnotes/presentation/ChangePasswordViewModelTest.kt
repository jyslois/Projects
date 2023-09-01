package com.android.mymindnotes.presentation

import com.android.mymindnotes.domain.usecases.userInfoRemote.ChangePasswordUseCase
import com.android.mymindnotes.presentation.viewmodels.ChangePasswordViewModel
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
class ChangePasswordViewModelTest {
    private lateinit var changePasswordViewModel: ChangePasswordViewModel
    private val mockChangePasswordUseCase = mockk<ChangePasswordUseCase>()

    @Before
    fun setUp() {
        changePasswordViewModel = ChangePasswordViewModel(mockChangePasswordUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockChangePasswordUseCase)
    }

    // Test data
    private val password = "newpassword111"
    private val originalPassword = "test111"

    @Test
    fun changePasswordButtonClicked_ReturnsSuceesfulResult_UiStateChangedToSuccess() = runTest {
        // Given
        val msg = "비밀번호가 변경되었습니다."
        val successResponse = Result.success(msg)
        coEvery { mockChangePasswordUseCase(password, originalPassword) } returns flowOf(successResponse)

        // When
        changePasswordViewModel.changePasswordButtonClicked(password, originalPassword)

        // Then
        assertEquals(ChangePasswordViewModel.ChangePasswordUiState.Success(msg), changePasswordViewModel.uiState.value)
        coVerify { mockChangePasswordUseCase(password, originalPassword) }
    }

    @Test
    fun changePasswordButtonClicked_ReturnsUnSuccessfulResultOrThrowsException_UiStatedChangedToErrorAndThenToLoading() = runTest {
        // Given
        val errorMsg = "비밀번호 변경 실패."
        val failureResponse: Result<String?> = Result.failure(RuntimeException(errorMsg))
        coEvery { mockChangePasswordUseCase(password, originalPassword) } returns flowOf(failureResponse)

        // When
        changePasswordViewModel.changePasswordButtonClicked(password, originalPassword)

        // Then
        assertTrue(changePasswordViewModel.errorStateTriggered)
        assertEquals(ChangePasswordViewModel.ChangePasswordUiState.Loading, changePasswordViewModel.uiState.value)
        coVerify { mockChangePasswordUseCase(password, originalPassword) }
    }
}