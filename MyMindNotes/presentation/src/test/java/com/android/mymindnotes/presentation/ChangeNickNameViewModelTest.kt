package com.android.mymindnotes.presentation

import com.android.mymindnotes.domain.usecases.userInfoRemote.ChangeNickNameUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.CheckNickNameDuplicateUseCase
import com.android.mymindnotes.presentation.viewmodels.ChangeNickNameViewModel
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class ChangeNickNameViewModelTest {
    private lateinit var changeNickNameViewModel: ChangeNickNameViewModel
    private val mockChangeNickNameUseCase = mockk<ChangeNickNameUseCase>()
    private val mockCheckNickNameDuplicateUseCase = mockk<CheckNickNameDuplicateUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    // test data
    private val nickName = "로이스"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        changeNickNameViewModel = ChangeNickNameViewModel(mockChangeNickNameUseCase, mockCheckNickNameDuplicateUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockChangeNickNameUseCase, mockCheckNickNameDuplicateUseCase)
        Dispatchers.resetMain()
    }

    @Test
    fun checkNickNameButtonClicked_ReturnsSuccessfulResult_UiStateUpdatedToNickNameDuplicateCheckedThenToLoading() = runTest {
        // Given
        val msg = "사용 가능한 닉네임입니다."
        val successResult: Result<String?> = Result.success(msg)
        coEvery { mockCheckNickNameDuplicateUseCase(nickName) } returns flowOf(successResult)

        // When
        changeNickNameViewModel.checkNickNameButtonClicked(nickName)

        // Then
        assertTrue(changeNickNameViewModel.nickNameDuplicateCheckedStateTriggered)
        assertEquals(ChangeNickNameViewModel.ChangeNickNameUiState.Loading, changeNickNameViewModel.uiState.value)
        coVerify { mockCheckNickNameDuplicateUseCase(nickName) }
    }

    @Test
    fun checkNickNameButtonClicked_ReturnsUnSuccessfulResultOrThrowsException_UiStateUpdatedToErrorThenToLoading() = runTest {
        // Given
        val errorEsg = "닉네임 중복 체크 실패."
        val exceptionResult: Result<String?> = Result.failure(RuntimeException(errorEsg))
        coEvery { mockCheckNickNameDuplicateUseCase(nickName) } returns flowOf(exceptionResult)

        // When
        changeNickNameViewModel.checkNickNameButtonClicked(nickName)

        // Then
        assertTrue(changeNickNameViewModel.errorStateTriggered)
        assertEquals(ChangeNickNameViewModel.ChangeNickNameUiState.Loading, changeNickNameViewModel.uiState.value)
        coVerify { mockCheckNickNameDuplicateUseCase(nickName) }
    }

    @Test
    fun changeNickNameButtonClicked_ReturnsSuccessfulResult_UiStateUpdatedToNickNameChangedThenToLoading() = runTest {
        // Given
        val msg = "닉네임이 변경되었습니다."
        val successResult: Result<String?> = Result.success(msg)
        coEvery { mockChangeNickNameUseCase(nickName) } returns flowOf(successResult)

        // When
        changeNickNameViewModel.changeNickNameButtonClicked(nickName)

        // Then
        assertTrue(changeNickNameViewModel.nickNameChangedStateTriggered)
        assertEquals(ChangeNickNameViewModel.ChangeNickNameUiState.Loading, changeNickNameViewModel.uiState.value)
        coVerify { mockChangeNickNameUseCase(nickName) }
    }

    @Test
    fun changeNickNameButtonClicked_ReturnsUnSuccessfulResultOrThrowsException_UiStateUpdatedToErrorThenToLoading() = runTest {
        // Given
        val errorMsg = "닉네임 변경 실패."
        val exceptionResult: Result<String?> = Result.failure(RuntimeException(errorMsg))
        coEvery { mockChangeNickNameUseCase(any()) } returns flowOf(exceptionResult)

        // When
        changeNickNameViewModel.changeNickNameButtonClicked(nickName)

        // Then
        assertTrue(changeNickNameViewModel.errorStateTriggered)
        assertEquals(ChangeNickNameViewModel.ChangeNickNameUiState.Loading, changeNickNameViewModel.uiState.value)
        coVerify { mockChangeNickNameUseCase(nickName) }
    }
}