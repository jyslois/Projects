package com.android.mymindnotes.presentation

import com.android.mymindnotes.domain.usecases.JoinUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.CheckEmailDuplicateUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.CheckNickNameDuplicateUseCase
import com.android.mymindnotes.presentation.viewmodels.JoinViewModel
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
class JoinViewModelTest {
    private lateinit var joinViewModel: JoinViewModel
    private val mockJoinUseCase = mockk<JoinUseCase>()
    private val mockCheckEmailDuplicateUseCase = mockk<CheckEmailDuplicateUseCase>()
    private val mockCheckNickNameDuplicateUseCase = mockk<CheckNickNameDuplicateUseCase>()

    @Before
    fun setUp() {
        joinViewModel = JoinViewModel(mockJoinUseCase, mockCheckEmailDuplicateUseCase, mockCheckNickNameDuplicateUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockJoinUseCase, mockCheckEmailDuplicateUseCase, mockCheckNickNameDuplicateUseCase)
    }

    // Test Data
    val email = "seolois@hotmail.com"
    val nickname = "로이스"
    val password = "test1234"
    val birthyear = 1991

    @Test
    fun checkEmailButtonClicked_ReturnsSuccessfulResponse_UiStateChangedToEmailDuplicateCheckSucceedAndThenToLoading() = runTest {
        val msg = "사용 가능한 이메일입니다."
        val successResponse = Result.success(msg)
        coEvery { mockCheckEmailDuplicateUseCase(email) } returns flowOf(successResponse)

        joinViewModel.checkEmailButtonClicked(email)

        assertTrue(joinViewModel.emailDuplicateCheckSucceedStateTriggered)
        assertEquals(JoinViewModel.JoinUiState.Loading, joinViewModel.uiState.value)
        coVerify { mockCheckEmailDuplicateUseCase(email) }
    }

    @Test
    fun checkEmailButtonClicked_ReturnsUnsuccessfulResponseOrThrowsException_UiStateChangedToErrorAndThenToLoading() = runTest {
        val errorMsg = "이메일 중복 체크에 실패했습니다."
        val failureResponse = Result.failure<String>(RuntimeException(errorMsg))
        coEvery { mockCheckEmailDuplicateUseCase(email) } returns flowOf(failureResponse)

        joinViewModel.checkEmailButtonClicked(email)

        assertTrue(joinViewModel.errorStateTriggered)
        assertEquals(JoinViewModel.JoinUiState.Loading, joinViewModel.uiState.value)
        coVerify { mockCheckEmailDuplicateUseCase(email) }
    }

    @Test
    fun checkNickNameButtonClicked_ReturnsSuccessfulResponse_UiStateChangedToNicknameDuplicateCheckSucceedAndThenToLoading() = runTest {
        val msg = "사용 가능한 닉네임입니다."
        val successResponse = Result.success(msg)
        coEvery { mockCheckNickNameDuplicateUseCase(nickname) } returns flowOf(successResponse)

        joinViewModel.checkNickNameButtonClicked(nickname)

        assertTrue(joinViewModel.nicknameDuplicateCheckSucceedStateTriggered)
        assertEquals(JoinViewModel.JoinUiState.Loading, joinViewModel.uiState.value)
        coVerify { mockCheckNickNameDuplicateUseCase(nickname) }
    }

    @Test
    fun checkNickNameButtonClicked_ReturnsUnsuccessfulResponseOrThrowsException_UiStateChangedToErrorAndThenToLoading() = runTest {
        val errorMsg = "닉네임 중복 체크 실패."
        val failureResponse = Result.failure<String>(RuntimeException(errorMsg))
        coEvery { mockCheckNickNameDuplicateUseCase(nickname) } returns flowOf(failureResponse)

        joinViewModel.checkNickNameButtonClicked(nickname)

        assertTrue(joinViewModel.errorStateTriggered)
        assertEquals(JoinViewModel.JoinUiState.Loading, joinViewModel.uiState.value)
        coVerify { mockCheckNickNameDuplicateUseCase(nickname) }
    }

    @Test
    fun joinButtonClicked_ReturnsSuccessfulResponse_UiStateChangedToJoinSucceedAndThenToLoading() = runTest {
        val msg = "회원가입 성공"
        val successResponse = Result.success(msg)
        coEvery { mockJoinUseCase(email, nickname, password, birthyear) } returns flowOf(successResponse)

        joinViewModel.joinButtonClicked(email, nickname, password, birthyear)

        assertTrue(joinViewModel.joinSucceedStateTriggered)
        assertEquals(JoinViewModel.JoinUiState.Loading, joinViewModel.uiState.value)
        coVerify { mockJoinUseCase(email, nickname, password, birthyear) }
    }

    @Test
    fun joinButtonClicked_ReturnsUnSuccessfulResponseOrThrowsException_UiStateChangedToErrorAndThenToLoading() = runTest {
        val errorMsg = "회원가입에 실패했습니다."
        val failureResponse = Result.failure<String>(RuntimeException(errorMsg))
        coEvery { mockJoinUseCase(email, nickname, password, birthyear) } returns flowOf(failureResponse)

        joinViewModel.joinButtonClicked(email, nickname, password, birthyear)

        assertTrue(joinViewModel.errorStateTriggered)
        assertEquals(JoinViewModel.JoinUiState.Loading, joinViewModel.uiState.value)
        coVerify { mockJoinUseCase(email, nickname, password, birthyear) }
    }


}