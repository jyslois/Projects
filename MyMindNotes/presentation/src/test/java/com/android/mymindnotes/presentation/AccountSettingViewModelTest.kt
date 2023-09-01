package com.android.mymindnotes.presentation

import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoLoginStateUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.DeleteUserUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.GetUserInfoUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.UserInfo
import com.android.mymindnotes.presentation.viewmodels.AccountSettingViewModel
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
class AccountSettingViewModelTest {
    private lateinit var accountSettingViewModel: AccountSettingViewModel
    private val mockGetUserInfoUseCase = mockk<GetUserInfoUseCase>()
    private val mockDeleteUserUseCase = mockk<DeleteUserUseCase>()
    private val mockSaveAutoLoginStateUseCase = mockk<SaveAutoLoginStateUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    // 테스트 데이터
    private val nickname = "로이스"
    private val email = "seolois@hotmail.com"
    private val birthyear = 1991

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        accountSettingViewModel = AccountSettingViewModel(mockGetUserInfoUseCase, mockDeleteUserUseCase, mockSaveAutoLoginStateUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockGetUserInfoUseCase, mockDeleteUserUseCase, mockSaveAutoLoginStateUseCase)
        Dispatchers.resetMain()
    }

    @Test
    fun logoutButtonClicked_UiStateUpdatedToLogout() = runTest {
        // Given
        coEvery { mockSaveAutoLoginStateUseCase(any()) } just Runs

        // When
        accountSettingViewModel.logoutButtonClicked()

        // Then
        assertEquals(AccountSettingViewModel.AccountSettingUiState.Logout, accountSettingViewModel.uiState.value)
        coVerify { mockSaveAutoLoginStateUseCase(false) }
    }

    @Test
    fun deleteUserButtonClicked_UiStateUpdatedToWithdraw() = runTest {
        // Given
        val msg = "회원 탈퇴가 완료되었습니다."
        val successResult: Result<String?> = Result.success(msg)
        coEvery { mockDeleteUserUseCase() } returns flowOf(successResult)

        // When
        accountSettingViewModel.deleteUserButtonClicked()

        // Then
        assertEquals(AccountSettingViewModel.AccountSettingUiState.Withdraw(msg), accountSettingViewModel.uiState.value)
        coVerify { mockDeleteUserUseCase() }
    }

    @Test
    fun deleteUserButtonClicked_throwsRuntimeException() = runTest {
        // Given
        val errorMsg = "회원 탈퇴 실패. 인터넷 연결을 확인해 주세요."
        val failureResult: Result<String?> = Result.failure(RuntimeException(errorMsg))
        coEvery { mockDeleteUserUseCase() } returns flowOf(failureResult)

        // When
        accountSettingViewModel.deleteUserButtonClicked()

        // Then
        assertTrue(accountSettingViewModel.errorStateTriggered)
        assertEquals(AccountSettingViewModel.AccountSettingUiState.Loading, accountSettingViewModel.uiState.value)
        coVerify { mockDeleteUserUseCase() }
    }

    @Test
    fun init_UiStateUpdatedToSuccess() = runTest {
        // Given
        val successResult: Result<UserInfo> = Result.success(UserInfo(nickname = nickname, email = email, birthyear = birthyear))
        coEvery { mockGetUserInfoUseCase() } returns flowOf(successResult)

        // When: initialize the ViewModel to trigger the init block with the mock setup
        accountSettingViewModel = AccountSettingViewModel(
            mockGetUserInfoUseCase, mockDeleteUserUseCase, mockSaveAutoLoginStateUseCase
        )

        // wait until all coroutine tasks finish
        advanceUntilIdle()

        // Then
        assertEquals(AccountSettingViewModel.AccountSettingUiState.Success(nickname, email, birthyear), accountSettingViewModel.uiState.value)
        coVerify { mockGetUserInfoUseCase() }
    }

    @Test
    fun init_throwsRuntimeException() = runTest {
        // Given
        val errorMsg = "서버와의 통신에 실패했습니다. 인터넷 연결을 확인해 주세요."
        val failureResponse: Result<UserInfo> = Result.failure(RuntimeException(errorMsg))
        coEvery { mockGetUserInfoUseCase() } returns flowOf(failureResponse)

        // When: initialize the ViewModel to trigger the init block with the mock setup
        accountSettingViewModel = AccountSettingViewModel(
            mockGetUserInfoUseCase, mockDeleteUserUseCase, mockSaveAutoLoginStateUseCase
        )

        // wait until all coroutine tasks finish
        advanceUntilIdle()

        // Then
        assertTrue(accountSettingViewModel.errorStateTriggered)
        assertEquals(AccountSettingViewModel.AccountSettingUiState.Loading, accountSettingViewModel.uiState.value)
        coVerify { mockGetUserInfoUseCase() }
    }

}
