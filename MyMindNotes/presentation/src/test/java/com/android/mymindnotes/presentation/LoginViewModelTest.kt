package com.android.mymindnotes.presentation

import com.android.mymindnotes.domain.usecases.LoginUseCase
import com.android.mymindnotes.domain.usecases.loginStates.ChangeAutoSaveBoxStateUseCase
import com.android.mymindnotes.domain.usecases.loginStates.GetAutoLoginStateUseCase
import com.android.mymindnotes.domain.usecases.loginStates.GetAutoSaveStateUseCase
import com.android.mymindnotes.domain.usecases.userInfo.GetIdUseCase
import com.android.mymindnotes.domain.usecases.userInfo.GetPasswordUseCase
import com.android.mymindnotes.presentation.viewmodels.LoginViewModel
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
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
class LoginViewModelTest {
    private lateinit var loginViewModel: LoginViewModel
    private val mockLoginUseCase = mockk<LoginUseCase>()
    private val mockGetIdUseCase = mockk<GetIdUseCase>()
    private val mockGetPasswordUseCase = mockk<GetPasswordUseCase>()
    private val mockGetAutoLoginStateUseCase = mockk<GetAutoLoginStateUseCase>()
    private val mockGetAutoSaveStateUseCase = mockk<GetAutoSaveStateUseCase>()
    private val mockChangeAutoSaveBoxStateUseCase = mockk<ChangeAutoSaveBoxStateUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { mockGetAutoSaveStateUseCase() } returns flowOf()
        coEvery { mockGetAutoLoginStateUseCase() } returns flowOf()
        coEvery { mockGetIdUseCase() } returns flowOf()
        coEvery { mockGetPasswordUseCase() } returns flowOf()
        loginViewModel = LoginViewModel(mockLoginUseCase, mockGetIdUseCase, mockGetPasswordUseCase, mockGetAutoLoginStateUseCase, mockGetAutoSaveStateUseCase, mockChangeAutoSaveBoxStateUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockLoginUseCase, mockGetIdUseCase, mockGetPasswordUseCase, mockGetAutoLoginStateUseCase, mockGetAutoSaveStateUseCase, mockChangeAutoSaveBoxStateUseCase)
        Dispatchers.resetMain()
    }

    // Test data
    private val email = "seolois@hotmail.com"
    private val password = "Test1234"
    private val isAutoLoginChecked = true
    private val isAutoSaveChecked = true

    @Test
    fun loginButtonClicked_ReturnsSuccessfulResponse_UiStateUpdatedToLoginSucceed() = runTest {
        // Given
        val msg = "로그인 성공"
        val successResponse = Result.success(msg)
        coEvery { mockLoginUseCase(email, password, isAutoLoginChecked, isAutoSaveChecked) } returns flowOf(successResponse)

        // When
        loginViewModel.loginButtonClicked(email, password, isAutoLoginChecked, isAutoSaveChecked)

        // Then
        assertEquals(LoginViewModel.LoginUiState.LoginSucceed(msg), loginViewModel.uiState.value)
        coVerify { mockLoginUseCase(email, password, isAutoLoginChecked, isAutoSaveChecked) }
    }

    @Test
    fun loginButtonClicked_ReturnsUnSuccessfulResponseOrThrowsException_UiStateUpdatedToErrorAndThenToLoading() = runTest {
        // Given
        val errorMsg = "로그인에 실패했습니다."
        val failureResult: Result<String?> = Result.failure(RuntimeException(errorMsg))
        coEvery { mockLoginUseCase(email, password, isAutoLoginChecked, isAutoSaveChecked) } returns flowOf(failureResult)

        // When
        loginViewModel.loginButtonClicked(email, password, isAutoLoginChecked, isAutoSaveChecked)

        // Then
        assertTrue(loginViewModel.errorStateTriggered)
        assertEquals(LoginViewModel.LoginUiState.Loading, loginViewModel.uiState.value)
        coVerify { mockLoginUseCase(email, password, isAutoLoginChecked, isAutoSaveChecked) }
    }

    @Test
    fun autoSaveBoxClicked_InvokesChangeAutoSaveBoxStateUseCase() = runTest {
        // Given
        coEvery { mockChangeAutoSaveBoxStateUseCase(any(), any(), any(), any()) } just Runs

        // When
        loginViewModel.autoSaveBoxClicked(isAutoSaveChecked, isAutoLoginChecked, email, password)

        // wait until all coroutine tasks finish
        advanceUntilIdle()

        // Then
        coVerify { mockChangeAutoSaveBoxStateUseCase(isAutoSaveChecked, isAutoLoginChecked, email, password )}
    }

    @Test
    fun `init_when getting required states is successful, should change UiState to Succeed`() = runTest {
        // Given
        coEvery { mockGetAutoSaveStateUseCase() } returns flowOf(isAutoSaveChecked)
        coEvery { mockGetAutoLoginStateUseCase() } returns flowOf(isAutoLoginChecked)
        coEvery { mockGetIdUseCase() } returns flowOf(email)
        coEvery { mockGetPasswordUseCase() } returns flowOf(password)

        // When: Initialize the ViewModel to trigger the init block with the mock setup
        loginViewModel = LoginViewModel(
            mockLoginUseCase, mockGetIdUseCase, mockGetPasswordUseCase,
            mockGetAutoLoginStateUseCase, mockGetAutoSaveStateUseCase, mockChangeAutoSaveBoxStateUseCase
        )

        // Wait until all coroutine tasks finish
        advanceUntilIdle()

        // Then
        assertEquals(
            LoginViewModel.LoginUiState.Succeed(isAutoSaveChecked, isAutoLoginChecked, email, password),
            loginViewModel.uiState.value
        )
        coVerify {
            mockGetAutoSaveStateUseCase()
            mockGetAutoLoginStateUseCase()
            mockGetIdUseCase()
            mockGetPasswordUseCase()
        }
    }
}

