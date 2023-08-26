package com.android.mymindnotes.domain

import com.android.mymindnotes.core.dto.ChangeToTemporaryPasswordResponse
import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.userInfoRemote.ChangeToTemporaryPasswordUseCase
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
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
class ChangeToTemporaryPasswordUseCaseTest {
    private lateinit var changeToTemporaryPasswordUseCase: ChangeToTemporaryPasswordUseCase
    private val testDispatcher = StandardTestDispatcher()
    private val mockMemberRepository = mockk<MemberRepository>()

    // 테스트 데이터
    private val email = "seolois@hotmail.com"
    private val randomPassword = "ABC111"

    @Before
    fun setUp() {
        changeToTemporaryPasswordUseCase = ChangeToTemporaryPasswordUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    @Test
    fun invoke_ReturnsSuccessfulResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = ChangeToTemporaryPasswordResponse(code = 3006, msg = "임시 비밀번호 전송 성공. 임시 비밀번호로 비밀번호가 변경되었습니다.")
        coEvery { mockMemberRepository.changeToTemporaryPassword(email, randomPassword) } returns flowOf(expectedResponse)

        // When
        val actualResponseFlow = changeToTemporaryPasswordUseCase(email, randomPassword)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isSuccess)
            val msg = it.getOrNull()
            assertNotNull(msg)
            assertEquals(expectedResponse.msg, msg, "반환된 메세지($msg)가 예상 메시지(${expectedResponse.msg})와 다릅니다.")
        }
        coVerify { mockMemberRepository.changeToTemporaryPassword(email, randomPassword) }
    }

    @Test
    fun invoke_ReturnsUnsuccesfulResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = ChangeToTemporaryPasswordResponse(code = 3007, msg = "가입되어 있지 않은 이메일이므로 임시 비밀번호가 전송되지 않았습니다.")
        coEvery { mockMemberRepository.changeToTemporaryPassword(email, randomPassword) } returns flowOf(expectedResponse)

        // When
        val actualResponseFlow = changeToTemporaryPasswordUseCase(email, randomPassword)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isFailure)
            val exception = it.exceptionOrNull()
            assertNotNull(exception)
            assertTrue(exception is RuntimeException)
            assertEquals(expectedResponse.msg, exception.message, "반환된 예외 메세지(${exception.message})가 예상 예외 메시지(${expectedResponse.msg})와 다릅니다.")
        }
        coVerify { mockMemberRepository.changeToTemporaryPassword(email, randomPassword) }
    }

    @Test
    fun invoke_ThrowsRuntimeException() = runTest(testDispatcher) {
        // Given
        val errorMsg = "임시 비밀번호 발송에 실패했습니다. 인터넷 연결을 확인해 주세요."
        coEvery { mockMemberRepository.changeToTemporaryPassword(email, randomPassword) } throws RuntimeException(errorMsg)

        // When
        val actualResponseFlow = changeToTemporaryPasswordUseCase(email, randomPassword)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isFailure)
            val exception = it.exceptionOrNull()
            assertNotNull(exception)
            assertTrue(exception is RuntimeException)
            assertEquals(errorMsg, exception.message, "반환된 에러 메세지(${exception.message})가 예상 에러 메시지(${errorMsg})와 다릅니다.")
        }
        coVerify { mockMemberRepository.changeToTemporaryPassword(email, randomPassword) }
    }
}