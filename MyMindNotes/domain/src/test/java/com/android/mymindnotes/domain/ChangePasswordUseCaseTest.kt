package com.android.mymindnotes.domain

import com.android.mymindnotes.core.dto.ChangePasswordResponse
import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.userInfo.SavePasswordUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.ChangePasswordUseCase
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
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class ChangePasswordUseCaseTest {
    private lateinit var changePasswordUseCase: ChangePasswordUseCase
    private val testDispatcher = StandardTestDispatcher()
    private val mockMemberRepository = mockk<MemberRepository>()
    private val mockSavePasswordUseCase = mockk<SavePasswordUseCase>()

    // 테스트용 데이터
    private val password = "test1234"
    private val originalPassword = "test111"

    @Before
    fun setUp() {
        changePasswordUseCase = ChangePasswordUseCase(mockMemberRepository, mockSavePasswordUseCase)
    }

    @Before
    fun tearDown() {
        clearMocks(mockMemberRepository, mockSavePasswordUseCase)
    }

    @Test
    fun invoke_ReturnsSuccessfulResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = ChangePasswordResponse(code = 3002, msg = "비밀번호가 변경되었습니다")
        coEvery { mockMemberRepository.changePassword(password, originalPassword) } returns flowOf(expectedResponse)
        coEvery { mockSavePasswordUseCase(any()) } just Runs

        // When
        val actualResponseFlow = changePasswordUseCase(password, originalPassword)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isSuccess)
            val msg = it.getOrNull()
            assertNotNull(msg)
            assertEquals(expectedResponse.msg, msg, "반환된 메세지($msg)가 예상 메시지(${expectedResponse.msg})와 다릅니다.")
        }
        coVerify { mockMemberRepository.changePassword(password, originalPassword) }
        coVerify { mockSavePasswordUseCase(password) }
    }

    @Test
    fun invoke_ReturnsUnsuccesfulResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = ChangePasswordResponse(code = 3005, msg = "기존 비밀번호를 확인해 주세요")
        coEvery { mockMemberRepository.changePassword(password, originalPassword) } returns flowOf(expectedResponse)

        // When
        val actualResponseFlow = changePasswordUseCase(password, originalPassword)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isFailure)
            val exception = it.exceptionOrNull()
            assertNotNull(exception)
            assertTrue(exception is RuntimeException)
            assertEquals(expectedResponse.msg, exception.message, "반환된 예외 메세지(${exception.message})가 예상 예외 메시지(${expectedResponse.msg})와 다릅니다.")
        }
        coVerify { mockMemberRepository.changePassword(password, originalPassword) }
    }

    @Test
    fun invoke_ThrowsRuntimeException() = runTest(testDispatcher) {
        // Given
        val errorMsg = "비밀번호 변경 실패. 인터넷 연결을 확인해 주세요."
        coEvery { mockMemberRepository.changePassword(password, originalPassword) } throws RuntimeException(errorMsg)

        // When
        val actualResponseFlow = changePasswordUseCase(password, originalPassword)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isFailure)
            val exception = it.exceptionOrNull()
            assertNotNull(exception)
            assertTrue(exception is RuntimeException)
            assertEquals(errorMsg, exception.message, "반환된 에러 메세지(${exception.message})가 예상 에러 메시지(${errorMsg})와 다릅니다.")
        }
        coVerify { mockMemberRepository.changePassword(password, originalPassword) }
    }
}