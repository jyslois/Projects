package com.android.mymindnotes.presentation

import com.android.mymindnotes.domain.usecases.userInfo.GetFirstTimeStateUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveFirstTimeStateUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.GetUserInfoUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.UserInfo
import com.android.mymindnotes.presentation.viewmodels.MainPageViewModel
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
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class MainPageViewModelTest {
    private lateinit var mainPageViewModel: MainPageViewModel
    private val mockGetUserInfoUseCase = mockk<GetUserInfoUseCase>()
    private val mockGetFirstTimeStateUseCase = mockk<GetFirstTimeStateUseCase>()
    private val mockSaveFirstTimeStateUseCase = mockk<SaveFirstTimeStateUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { mockGetFirstTimeStateUseCase() } returns flowOf(false)
        mainPageViewModel = MainPageViewModel(mockGetUserInfoUseCase, mockGetFirstTimeStateUseCase, mockSaveFirstTimeStateUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockGetUserInfoUseCase, mockGetFirstTimeStateUseCase, mockSaveFirstTimeStateUseCase)
        Dispatchers.resetMain()
    }

    // Test data
    private val nickname = "로이스"
    private val email = "seolois@hotmail.com"
    private val birthyear = 1991

    @Test
    fun getNickNameFromUserInfo_ReturnsSuccessfulResult_UiStateUpdatedToSuccess() = runTest {
        // Given
        val successResponse = UserInfo(nickname = nickname, email = email, birthyear = birthyear)
        coEvery { mockGetUserInfoUseCase() } returns flowOf(Result.success(successResponse))

        // When
        mainPageViewModel.getNickNameFromUserInfo()

        // Then
        assertEquals(MainPageViewModel.MainPageUiState.Success(nickname), mainPageViewModel.uiState.value)
        coVerify { mockGetUserInfoUseCase() }
    }

    @Test
    fun getNickNameFromUserInfo_ThrowsException_UiStateUpdatedToErrorAndThenToLoading() = runTest {
        // Given
        val errorMsg = "서버와의 통신에 실패했습니다. 인터넷 연결을 확인해 주세요."
        coEvery { mockGetUserInfoUseCase() } returns flowOf(Result.failure(RuntimeException(errorMsg)))

        // When
        mainPageViewModel.getNickNameFromUserInfo()

        // Then
        assertTrue(mainPageViewModel.errorStateTriggered)
        assertEquals(MainPageViewModel.MainPageUiState.Loading, mainPageViewModel.uiState.value)
        coVerify { mockGetUserInfoUseCase() }
    }

    @Test
    fun `init_if first time, should change UiState to FirstTime and then to Success`() = runTest {
        // Given
        coEvery { mockGetFirstTimeStateUseCase() } returns flowOf(true)
        val successResponse = UserInfo(nickname = nickname, email = email, birthyear = birthyear)
        coEvery { mockGetUserInfoUseCase() } returns flowOf(Result.success(successResponse))
        coEvery { mockSaveFirstTimeStateUseCase(any()) } just Runs

        // When - init block을 trigger하기 위해 뷰모델 재초기화
        mainPageViewModel = MainPageViewModel(mockGetUserInfoUseCase, mockGetFirstTimeStateUseCase, mockSaveFirstTimeStateUseCase)

        // wait until all coroutine tasks finish
        advanceUntilIdle()

        // Then
        assertTrue(mainPageViewModel.firstTimeStateTriggered)
        assertEquals(MainPageViewModel.MainPageUiState.Success(nickname), mainPageViewModel.uiState.value)
        coVerify { mockGetFirstTimeStateUseCase() }
        coVerify { mockGetUserInfoUseCase() }
        coVerify { mockSaveFirstTimeStateUseCase(false) }
    }

    @Test
    fun `init_if not first time, should change UiState to Success`() = runTest {
        // Given
        coEvery { mockGetFirstTimeStateUseCase() } returns flowOf(false)
        val successResponse = UserInfo(nickname = nickname, email = email, birthyear = birthyear)
        coEvery { mockGetUserInfoUseCase() } returns flowOf(Result.success(successResponse))

        // When - init block을 trigger하기 위해 뷰모델 재초기화
        mainPageViewModel = MainPageViewModel(mockGetUserInfoUseCase, mockGetFirstTimeStateUseCase, mockSaveFirstTimeStateUseCase)

        // wait until all coroutine tasks finish
        advanceUntilIdle()

        // Then
        assertFalse(mainPageViewModel.firstTimeStateTriggered)
        assertEquals(MainPageViewModel.MainPageUiState.Success(nickname), mainPageViewModel.uiState.value)
        coVerify { mockGetFirstTimeStateUseCase() }
        coVerify { mockGetUserInfoUseCase() }
    }
}