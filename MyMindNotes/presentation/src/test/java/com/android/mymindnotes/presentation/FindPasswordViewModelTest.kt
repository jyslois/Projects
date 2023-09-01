package com.android.mymindnotes.presentation

import com.android.mymindnotes.domain.usecases.userInfoRemote.ChangeToTemporaryPasswordUseCase
import com.android.mymindnotes.presentation.viewmodels.FindPasswordViewModel
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
class FindPasswordViewModelTest {
    private lateinit var findPasswordViewModel: FindPasswordViewModel
    private val mockChangeToTemporaryPasswordUseCase = mockk<ChangeToTemporaryPasswordUseCase>()

    @Before
    fun setUp() {
        findPasswordViewModel = FindPasswordViewModel(mockChangeToTemporaryPasswordUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockChangeToTemporaryPasswordUseCase)
    }

    // Test Data
    private val email = "seolois@hotmail.com"
    private val randomPassword = "ABC1234"

    @Test
    fun sendEmailButtonClicked_ReturnsSuccessfulResponse_UiStateChangedToSuccessAndThenToLoading() = runTest {
        // Given
        val msg = "임시 비밀번호가 이메일로 전송되었습니다."
        val successResponse: Result<String?> = Result.success(msg)
        coEvery { mockChangeToTemporaryPasswordUseCase(email, randomPassword) } returns flowOf(successResponse)

        // When
        findPasswordViewModel.sendEmailButtonClicked(email, randomPassword)

        // Then
        assertTrue(findPasswordViewModel.successStateTriggered)
        assertEquals(FindPasswordViewModel.FindPasswordUiState.Loading, findPasswordViewModel.uiState.value)
        coVerify { mockChangeToTemporaryPasswordUseCase(email, randomPassword) }
    }

    @Test
    fun sendEmailButtonClicked_ReturnsUnsuccessfulResponseOrThrowsException_UiStateChangedToErrorAndThenToLoading() = runTest {
        // Given
        val errorMsg = "임시 비밀번호 발송에 실패했습니다."
        val failureResponse: Result<String?> = Result.failure(RuntimeException(errorMsg))
        coEvery { mockChangeToTemporaryPasswordUseCase(email, randomPassword) } returns flowOf(failureResponse)

        // When
        findPasswordViewModel.sendEmailButtonClicked(email, randomPassword)

        // Then
        assertTrue(findPasswordViewModel.errorStateTriggered)
        assertEquals(FindPasswordViewModel.FindPasswordUiState.Loading, findPasswordViewModel.uiState.value)
        coVerify { mockChangeToTemporaryPasswordUseCase(email, randomPassword) }
    }
}