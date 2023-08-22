package com.android.mymindnotes.domain

import com.android.mymindnotes.core.dto.JoinResponse
import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.JoinUseCase
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoLoginStateUseCase
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoSaveStateUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveFirstTimeStateUseCase
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
class JoinUseCaseTest {
    private lateinit var joinUseCase: JoinUseCase
    private val mockMemberRepository = mockk<MemberRepository>()
    private val mockSaveIdAndPasswordUseCase = mockk<SaveIdAndPasswordUseCase>()
    private val mockSaveFirstTimeStateUseCase = mockk<SaveFirstTimeStateUseCase>()
    private val mockSaveUserIndexUseCase = mockk<SaveUserIndexUseCase>()
    private val mockSaveAutoLoginStateUseCase = mockk<SaveAutoLoginStateUseCase>()
    private val mockSaveAutoSaveStateUseCase = mockk<SaveAutoSaveStateUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    // 테스트 데이터
    private val email = "seolois@hotmail.com"
    private val nickname = "로이스"
    private val password = "test111"
    private val birthyear = 1991

    @Before
    fun setUp() {
        joinUseCase = JoinUseCase(memberRepository = mockMemberRepository, saveIdAndPasswordUseCase = mockSaveIdAndPasswordUseCase, saveFirstTimeStateUseCase = mockSaveFirstTimeStateUseCase, saveUserIndexUseCase = mockSaveUserIndexUseCase, saveAutoLoginStateUseCase = mockSaveAutoLoginStateUseCase, saveAutoSaveStateUseCase = mockSaveAutoSaveStateUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository, mockSaveIdAndPasswordUseCase, mockSaveFirstTimeStateUseCase, mockSaveUserIndexUseCase, mockSaveAutoLoginStateUseCase, mockSaveAutoSaveStateUseCase)
    }

    @Test
    fun invoke_ReturnsSuccessfulResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = JoinResponse(code = 2000, msg = "회원 가입 성공! 환영합니다", userIndex = 1)
        coEvery { mockMemberRepository.join(email, nickname, password, birthyear) } returns flowOf(expectedResponse)
        // 다른 UseCase의 invoke 호출 시 아무런 동작도 수행하지 않도록 설정
        // 이 테스트의 주요 목적이 JoinUseCase의 동작을 검증하는 것이기 때문에, 다른 UseCase들의 내부 로직은 중요하지 않다. 따라서 다른 UseCase들에 대해서는 그저 호출되는지 여부만 검증하면 되고, 그들의 실제 동작에 대해서는 걱정할 필요가 없다.
        coEvery { mockSaveIdAndPasswordUseCase.invoke(any(), any()) } just Runs
        coEvery { mockSaveFirstTimeStateUseCase.invoke(any()) } just Runs
        coEvery { mockSaveUserIndexUseCase.invoke(any()) } just Runs
        coEvery { mockSaveAutoLoginStateUseCase.invoke(any()) } just Runs
        coEvery { mockSaveAutoSaveStateUseCase.invoke(any()) } just Runs

        // When
        val actualResponseFlow = joinUseCase(email = email, nickname = nickname, password = password, birthyear = birthyear)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isSuccess)
            val msg = it.getOrNull()
            assertNotNull(msg)
            assertEquals(expectedResponse.msg, msg, "반환된 메시지($msg)가 예상 메세지(${expectedResponse.msg})와 다릅니다")
        }
        coVerify { mockMemberRepository.join(email, nickname, password, birthyear) }
        // 다른 UseCase들이 제대로 호출됐는지 검증
        coVerify { mockSaveUserIndexUseCase.invoke(1) }
        coVerify { mockSaveIdAndPasswordUseCase.invoke(email, password) }
        coVerify { mockSaveFirstTimeStateUseCase.invoke(true) }
        coVerify { mockSaveAutoLoginStateUseCase.invoke(true) }
        coVerify { mockSaveAutoSaveStateUseCase.invoke(true) }
    }

    @Test
    fun invoke_ReturnsUnsuccesfulResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = JoinResponse(code = 2001, msg = "양식 오류로 회원가입에 실패했습니다")
        coEvery { mockMemberRepository.join(email = email, nickname = nickname, password = password, birthyear = birthyear) } returns flowOf(expectedResponse)

        // When
        val actualResponseFlow = joinUseCase(email, nickname, password, birthyear)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isFailure)
            val exception = it.exceptionOrNull()
            assertNotNull(exception)
            assertTrue(exception is RuntimeException)
            assertEquals(expectedResponse.msg, exception.message, "반환된 예외 메시지(${exception.message})가 예상 예외 메세지(${expectedResponse.msg})와 다릅니다")
        }
        coVerify { mockMemberRepository.join(email, nickname, password, birthyear) }
    }

    @Test
    fun invoke_throwsRuntimeException() = runTest(testDispatcher) {
        // Given
        val errorMsg = "회원가입에 실패했습니다. 인터넷 연결을 확인해 주세요."
        coEvery { mockMemberRepository.join(email, nickname, password, birthyear) } throws RuntimeException(errorMsg)

        // When
        val actualResponseFlow = joinUseCase(email, nickname, password, birthyear)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isFailure)
            val exception = it.exceptionOrNull()
            assertNotNull(exception)
            assertTrue(exception is RuntimeException)
            assertEquals(errorMsg, exception.message, "반환된 예외 메시지(${exception.message})가 예상 예외 메세지(${errorMsg})와 다릅니다")
        }
        coVerify { mockMemberRepository.join(email, nickname, password, birthyear) }
    }
}