package com.android.mymindnotes.domain

import com.android.mymindnotes.core.dto.LoginResponse
import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.LoginUseCase
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoLoginStateUseCase
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoSaveStateUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveIdAndPasswordUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveUserIndexUseCase
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class LoginUseCaseTest {
    private lateinit var loginUseCase: LoginUseCase
    private val mockMemberRepository = mockk<MemberRepository>()
    private val mockSaveIdAndPasswordUseCase = mockk<SaveIdAndPasswordUseCase>()
    private val mockSaveUserIndexUseCase = mockk<SaveUserIndexUseCase>()
    private val mockSaveAutoLoginStateUseCase = mockk<SaveAutoLoginStateUseCase>()
    private val mockSaveAutoSaveStateUseCase = mockk<SaveAutoSaveStateUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    // 테스트 데이터
    private val email = "seolois@hotmail.com"
    private val password = "test111"
    private val isAutoLoginChecked = true
    private val isAutoSaveChecked = true

    @Before
    fun setUp() {
        loginUseCase = LoginUseCase(memberRepository = mockMemberRepository, saveIdAndPasswordUseCase = mockSaveIdAndPasswordUseCase, saveUserIndexUseCase = mockSaveUserIndexUseCase, saveAutoLoginStateUseCase = mockSaveAutoLoginStateUseCase, saveAutoSaveStateUseCase = mockSaveAutoSaveStateUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository, mockSaveIdAndPasswordUseCase, mockSaveUserIndexUseCase, mockSaveAutoLoginStateUseCase, mockSaveAutoSaveStateUseCase)
    }

    @Test
    fun invoke_ReturnsSuccessfulResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = LoginResponse(code = 5000, msg = "로그인 성공. 환영합니다!", nickname = "로이스", userIndex = 1)
        coEvery { mockMemberRepository.login(email, password) } returns flowOf(expectedResponse)
        coEvery { mockSaveIdAndPasswordUseCase.invoke(any(), any())} just Runs
        coEvery { mockSaveUserIndexUseCase.invoke(any()) } just Runs
        coEvery { mockSaveAutoLoginStateUseCase.invoke(any()) } just Runs
        coEvery { mockSaveAutoSaveStateUseCase.invoke(any()) } just Runs

        // When
        val actualResponseFlow = loginUseCase(email, password, isAutoLoginChecked, isAutoSaveChecked)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isSuccess)
            val msg = it.getOrNull()
            assertNotNull(msg)
            assertEquals(expectedResponse.msg, msg, "반환된 메시지($msg)가 예상 메세지(${expectedResponse.msg})와 다릅니다")
        }
        coVerify { mockMemberRepository.login(email, password) }
        coVerify { mockSaveIdAndPasswordUseCase(email, password) }
        coVerify { mockSaveUserIndexUseCase(1) }
        coVerify { mockSaveAutoLoginStateUseCase(true) }
        coVerify { mockSaveAutoSaveStateUseCase(true) }
    }

    @Test
    fun invoke_ReturnsUnsuccessfulResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = LoginResponse(code = 5003, msg = "비밀번호가 틀렸습니다")
        coEvery { mockMemberRepository.login(email, password) } returns flowOf(expectedResponse)

        // When
        val actualResponseFlow = loginUseCase(email, password, isAutoLoginChecked, isAutoSaveChecked)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isFailure)
            val exception = it.exceptionOrNull()
            assertNotNull(exception)
            assertTrue(exception is RuntimeException)
            assertEquals(expectedResponse.msg, exception.message, "반환된 예외 메시지(${exception.message})가 예상 예외 메세지(${expectedResponse.msg})와 다릅니다")
        }
        coVerify { mockMemberRepository.login(email, password) }
    }

    @Test
    fun invoke_ThrowsRuntimeException() = runTest(testDispatcher) {
        // Given
        val msg = "로그인에 실패했습니다. 인터넷 연결을 확인해 주세요."
        coEvery { mockMemberRepository.login(email, password) } throws RuntimeException(msg)

        // When
        val actualResponseFlow = loginUseCase(email, password, isAutoLoginChecked, isAutoSaveChecked)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isFailure)
            val exception = it.exceptionOrNull()
            assertNotNull(exception)
            assertTrue(exception is RuntimeException)
            assertEquals(msg, exception.message, "반환된 에러 메시지(${exception.message})가 예상 에러 메세지(${msg})와 다릅니다")
        }
        coVerify { mockMemberRepository.login(email, password) }
    }
}