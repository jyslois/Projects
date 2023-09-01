package com.android.mymindnotes.presentation

import com.android.mymindnotes.domain.usecases.userInfoRemote.GetUserInfoUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.UserInfo
import com.android.mymindnotes.presentation.viewmodels.RecordMindChoiceViewModel
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
class RecordMindChoiceViewModelTest {
    private lateinit var recordMindChoiceViewModel: RecordMindChoiceViewModel
    private val mockGetUserInfoUseCase = mockk<GetUserInfoUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        recordMindChoiceViewModel = RecordMindChoiceViewModel(mockGetUserInfoUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockGetUserInfoUseCase)
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
        recordMindChoiceViewModel.getNickNameFromUserInfo()

        // wait until all coroutine tasks finish
        advanceUntilIdle()

        // Then
        assertEquals(RecordMindChoiceViewModel.RecordMindChoiceUiState.Success(nickname), recordMindChoiceViewModel.uiState.value)
        coVerify { mockGetUserInfoUseCase() }
    }

    @Test
    fun getNickNameFromUserInfo_ThrowsException_UiStateUpdatedToErrorAndThenToLoading() = runTest {
        // Given
        val errorMsg = "서버와의 통신에 실패했습니다. 인터넷 연결을 확인해 주세요."
        coEvery { mockGetUserInfoUseCase() } returns flowOf(Result.failure(RuntimeException(errorMsg)))

        // When
        recordMindChoiceViewModel.getNickNameFromUserInfo()

        // wait until all coroutine tasks finish
        advanceUntilIdle()

        // Then
        assertTrue(recordMindChoiceViewModel.errorStateTriggered)
        assertEquals(RecordMindChoiceViewModel.RecordMindChoiceUiState.Loading, recordMindChoiceViewModel.uiState.value)
        coVerify { mockGetUserInfoUseCase() }
    }

    @Test
    fun `init_when getting nickname is successful, should change UiState to Success`() = runTest {
        // Given
        val successResponse = UserInfo(nickname = nickname, email = email, birthyear = birthyear)
        coEvery { mockGetUserInfoUseCase() } returns flowOf(Result.success(successResponse))

        // When: Initialize the ViewModel to trigger the init block with the mock setup
        recordMindChoiceViewModel = RecordMindChoiceViewModel(mockGetUserInfoUseCase)

        // Wait until all coroutine tasks finish
        advanceUntilIdle()

        // Then
        assertEquals(RecordMindChoiceViewModel.RecordMindChoiceUiState.Success(nickname), recordMindChoiceViewModel.uiState.value)
        coVerify { mockGetUserInfoUseCase() }
    }

    @Test
    fun `init_when getting nickname fails, should change UiState to Error and then to Loading`() = runTest {
        // Given
        val errorMsg = "서버와의 통신에 실패했습니다. 인터넷 연결을 확인해 주세요."
        val failureResult: Result<UserInfo> = Result.failure(RuntimeException(errorMsg))
        coEvery { mockGetUserInfoUseCase() } returns flowOf(failureResult)

        // When: Initialize the ViewModel to trigger the init block with the mock setup
        recordMindChoiceViewModel = RecordMindChoiceViewModel(mockGetUserInfoUseCase)

        // Wait until all coroutine tasks finish
        advanceUntilIdle()

        // Then
        assertTrue(recordMindChoiceViewModel.errorStateTriggered)
        assertEquals(RecordMindChoiceViewModel.RecordMindChoiceUiState.Loading, recordMindChoiceViewModel.uiState.value)
        coVerify { mockGetUserInfoUseCase() }
    }
}